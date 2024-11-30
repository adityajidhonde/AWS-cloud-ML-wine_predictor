# Use Amazon Linux 2 as the base image
FROM amazonlinux:2

# Set working directory
WORKDIR /app

# Update system packages and install necessary tools
RUN yum update -y && \
    yum install -y tar wget git

# Install Amazon Corretto 11 using the AL2 RPM package (64-bit)
RUN yum install -y https://corretto.aws/downloads/resources/11.0.25.9.1/java-11-amazon-corretto-devel-11.0.25.9-1.aarch64.rpm

# Set JAVA_HOME environment variable and update PATH
ENV JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto
ENV PATH="$JAVA_HOME/bin:$PATH"

# Install Apache Maven
RUN wget https://downloads.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz && \
    tar -xzf apache-maven-3.9.4-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.4/bin/mvn /usr/bin/mvn && \
    rm apache-maven-3.9.4-bin.tar.gz

# Install Apache Spark
RUN wget https://dlcdn.apache.org/spark/spark-3.5.3/spark-3.5.3-bin-hadoop3.tgz && \
    tar xvf spark-3.5.3-bin-hadoop3.tgz && \
    mv spark-3.5.3-bin-hadoop3 /opt/spark && \
    rm spark-3.5.3-bin-hadoop3.tgz

# Copy the entire project including the dataset
COPY . /app

# Set permissions for the script
RUN chmod +x run.sh

# Build the JAR file from your Java files
RUN mvn clean package -DskipTests

# Run the application
ENTRYPOINT ["bash", "run.sh"]
