#!/bin/bash

# Define default paths for dataset and models
DEFAULT_VALIDATION_DATASET="./src/main/dataset/ValidationDataset.csv"
DEFAULT_MODELS_DIR="./src/main/models/"

# Use provided dataset path or fallback to default
VALIDATION_DATASET=${1:-$DEFAULT_VALIDATION_DATASET}

# Check if the dataset file exists
if [ ! -f "$VALIDATION_DATASET" ]; then
    echo "Error: Validation dataset file not found at $VALIDATION_DATASET"
    exit 1
fi

# Define the JAR file path
JAR_FILE="./target/mljavademo-1.0-SNAPSHOT.jar"

# Check if the JAR file exists
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    echo "Make sure the Maven build step completed successfully."
    exit 1
fi

# Run the Spark application
/opt/spark/bin/spark-submit \
    --class com.wine.quality.wine_test_predict \
    --master local[*] \
    "$JAR_FILE" \
    "$VALIDATION_DATASET" \
    "$DEFAULT_MODELS_DIR"
