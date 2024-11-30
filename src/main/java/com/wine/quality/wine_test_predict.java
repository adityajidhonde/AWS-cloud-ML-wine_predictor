package com.wine.quality;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import java.io.File;
import java.nio.file.Paths;

import static org.apache.spark.sql.functions.col;

public class wine_test_predict {
    // Clean dataset: Remove quotes and cast columns to DoubleType
    public static Dataset<Row> cleanData(Dataset<Row> df) {
        for (String colName : df.columns()) {
            String cleanColName = colName.replace("\"", ""); // Remove quotes
            df = df.withColumn(cleanColName, col(colName).cast(DataTypes.DoubleType)); // Cast to DoubleType
            if (!cleanColName.equals(colName)) {
                df = df.drop(colName); // Drop old column if renamed
            }
        }
        return df;
    }

    public static void main(String[] args) {
        // Initialize SparkSession
        SparkSession spark = SparkSession.builder()
                .appName("wine_quality_model_testing")
                .master("local[*]")
                .getOrCreate();

        // Set log level
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
        sc.setLogLevel("ERROR");

        // Paths for input data and models directory
        String inputPath = "./src/main/dataset/ValidationDataset.csv";
        String modelsDirPath = "./src/main/model/";

        if (args.length > 0) {
            inputPath = args[0];
        }
        if (args.length > 1) {
            modelsDirPath = args[1];
        }

        System.out.println("Validation Dataset Path: " + inputPath);
        System.out.println("Models Directory Path: " + modelsDirPath);

        // Read CSV file into DataFrame
        Dataset<Row> validationData = spark.read()
                .format("csv")
                .option("header", "true")
                .option("sep", ";")
                .option("inferschema", "true")
                .load(inputPath);

        // Clean the data
        Dataset<Row> cleanedValidationData = cleanData(validationData);

        // Get all model directories
        File modelsDir = new File(modelsDirPath);
        if (!modelsDir.exists() || !modelsDir.isDirectory()) {
            System.err.println("Models directory does not exist or is not a directory: " + modelsDirPath);
            System.exit(-1);
        }

        File[] modelFiles = modelsDir.listFiles();
        if (modelFiles == null || modelFiles.length == 0) {
            System.err.println("No models found in directory: " + modelsDirPath);
            System.exit(-1);
        }

        // Iterate through models and evaluate each
        for (File modelFile : modelFiles) {
            if (modelFile.isDirectory()) {
                System.out.println("Testing model: " + modelFile.getName());
                try {
                    // Load the model
                    PipelineModel model = PipelineModel.load(modelFile.getAbsolutePath());

                    // Make predictions
                    Dataset<Row> predictions = model.transform(cleanedValidationData);

                    // Evaluate predictions
                    MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                            .setLabelCol("label")
                            .setPredictionCol("prediction");

                    double accuracy = evaluator.setMetricName("accuracy").evaluate(predictions);
                    double f1Score = evaluator.setMetricName("f1").evaluate(predictions);

                    // Output results
                    System.out.printf("Model: %s | Accuracy: %.4f | F1 Score: %.4f%n",
                            modelFile.getName(), accuracy, f1Score);
                } catch (Exception e) {
                    System.err.println("Error testing model " + modelFile.getName() + ": " + e.getMessage());
                }
            }
        }

        // Stop Spark session
        spark.stop();
    }
}
