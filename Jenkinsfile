pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-2'
        AWS_ACCOUNT_ID = '856021348734'
        ECR_REPOSITORY = 'kafka-learning-auth'
        IMAGE_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                bat '.\\mvnw.cmd test'
            }
        }

        stage('Package') {
            steps {
                bat '.\\mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t kafka-learning-auth:latest .'
            }
        }

        stage('Login to ECR') {
            steps {
                withCredentials([
                        string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY')
                ]) {
                    bat 'aws ecr get-login-password --region %AWS_REGION% | docker login --username AWS --password-stdin %AWS_ACCOUNT_ID%.dkr.ecr.%AWS_REGION%.amazonaws.com'
                }
            }
        }

        stage('Tag Docker Image') {
            steps {
                bat 'docker tag kafka-learning-auth:latest %IMAGE_URI%:latest'
            }
        }

        stage('Push Docker Image') {
            steps {
                bat 'docker push %IMAGE_URI%:latest'
            }
        }
        stage('Deploy to ECS') {
            steps {
                withCredentials([
                        string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                        string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY')
                ]) {
                    bat 'aws ecs update-service --cluster fortune-cluster --service kafka-learning-auth-service --force-new-deployment --region %AWS_REGION%'
                }
            }
        }

    }
}
