# CS 643 859, Cloud Computing Programming Assignment 2 (ad225)

## Source Code

* [Git URL](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/tree/main) - https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/

## Docker Images

* [Prediction Image](https://hub.docker.com/repository/docker/adityadhonde007/wine-quality-predictor/general) - https://hub.docker.com/repository/docker/adityadhonde007/wine-quality-predictor/general

## Overview
* [Setup Instructions and Navigation](#setup-instructions-and-navigation)
* [Running Examples](#running-examples)
* [Parallel training implementation](#parallel-training-implementation)
  - [EMR Cluster Setup](#emr-cluster-setup)
  - [Submitting Spark Job For Parallel Training](#submitting-spark-job-for-parallel-training)
  - [Verifying Model Output On S3 Bucket](#submitting-spark-job-for-parallel-training)
* [Single Machine Prediction Application](#single-machine-prediction-application)
  - [EC2 Setup](#ec2-setup)
  - [Running Prediction Application](#running-prediction-application)

## Setup Instructions and Navigation

### Technical Requirements

This project is built on following software requirements: <br/> <br/>
  •	[IntelliJ IDEA][intellij] <br/>
  •	[Java JDK][jdk]<br/>
  •	[Apache Spark][spark]<br/>
  •	[Maven][maven]<br/>
  •	[Docker][docker]<br/>
  •	[AWS][aws]<br/>
  •	[Hadoop-aws][hadoop-aws]<br/>

## Getting Started

#### Install or update Java #### 
Install oracle jdk 8 or openjdk 8 and configure the JAVA_HOME environment variable. and update PATH variable to %JAVA_HOME%\bin;
JAVA_HOME: 
PATH:
open the command prompt and check the java version.
```
C:\>java -version
java version "21.0.2" 2024-01-16 LTS
Java(TM) SE Runtime Environment (build 21.0.2+13-LTS-58)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.2+13-LTS-58, mixed mode, sharing) 
  ```
#### Install Maven #### 
Install apache maven.
```
C:\>mvn -version
Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
Maven home: /Applications/apache-maven-3.9.6
Java version: 21.0.2, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "15.1.1", arch: "aarch64", family: "mac"
```

#### Install Docker #### 
Install docker for mac.

```
C:\>docker --version
Docker version 27.1.1, build 6312585
```

#### Install Spark for Standalone cluster setup #### 

```
C:\>spark-shell --version
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.4.5
      /_/

Using Scala version 2.11.12, Java HotSpot(TM) 64-Bit Server VM, 1.8.0_201
Branch HEAD
Compiled by user centos on 2020-02-02T19:38:06Z
Revision cee4ecbb16917fa85f02c635925e2687400aa56b
Url https://gitbox.apache.org/repos/asf/spark.git
Type --help for more information.
```
  
## Running Examples

- Download the zip or clone the Git repository.
- Unzip the zip file (if you downloaded one)
- Open Command Prompt and Change directory (cd) to folder containing pom.xml
- run mvn package
- Open Intellij and run project.

## Parallel training implementation
This project is implemented with Spark data frames api and MLib libraries, with this Native spark implementation application is automatically parallelized and distributed natively.
<br />
Partitions and caching is implemented in code to speed up the process.

- Login into AWS Console.
- Create S3 Bucket
   - upload the TestDataset.csv, TrainingDataset.csv and https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/target/mljavademo-1.0-SNAPSHOT.jar
   - [![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/s3bucket.png)]
   
### EMR Cluster Setup
- Search for EMR Service & Create Cluster.
   - Provide the cluster name
   - Launch Mode cluster
   - Vendor Amazon
   - Release emr-7.5.0
   - Select spark application radio button
   - Hardware Configurations 
     - select the instance type
     - number of instances to train the model
   - select the ec2 key pair/ generate one to access the master node.
   - click on create cluster.

[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/ssh-login.png)]
[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/emr-start.png)]
[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/emr-hardware.png)]
[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/emr-success.png)]


### Submitting Spark Job For Parallel Training
- Verify Successful Cluster Setup process on Aws console

[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/output.png)]

### Verifying Model Output On S3 Bucket
- Verify all models are created on S3 bucket on Aws console.

[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/output.png)]

- Before implementation s3 bucket:

[[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/s3-input.png)

- After implementation s3 bucket:

[[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/s3-output.png)


### EC2 Setup
  [[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/ec2-instance.png)

### Running Prediction Application AWS
   - Install java : yum install -y https://corretto.aws/downloads/resources/11.0.25.9.1/java-11-amazon-corretto-devel-11.0.25.9-1.aarch64.rpm
   - Change java version : alternatives --config java
   - Install maven: yum install maven -y
   - Submit job: spark-submit --class com.wine.quality.Main --master yarn s3://adityastores/mljavademo-1.0-SNAPSHOT.jar
   - Below is spark output:
     
[[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/spark-history.png)
[[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/sprak-jobs.png)
 
## Running Prediction Application using Docker:
  - Clone the Repository using : git clone <repository-url>
cd <repository-directory>
  -  Build the Docker Image: docker build -t wine-quality-predictor .
  -  Run the Docker Container: docker run --rm \
    -v $(pwd)/src/main/dataset:/app/src/main/dataset \
    -e AWS_ACCESS_KEY_ID=<your-aws-access-key> \
    -e AWS_SECRET_ACCESS_KEY=<your-aws-secret-key> \
    wine-quality-predictor

[[![Image](https://github.com/adityajidhonde/AWS-cloud-ML-wine_predictor/blob/main/docker.png)


## Model Evaluation
The application evaluates multiple machine learning models. The top 5 models are selected based on their performance metrics:

- Multilayer Perceptron Classifier(Accuracy:0.56)
- Random Forest Classifier (Accuracy: 0.55)
- Decision Tree Classifier (Accuracy: 0.48)
- Naive-Bayes (Accuracy: 0.45)
- Logistic Regression (Accuracy: 0.41)

Each model's results, including accuracy, F1 score, and confusion matrices, are logged and saved for comparison.


