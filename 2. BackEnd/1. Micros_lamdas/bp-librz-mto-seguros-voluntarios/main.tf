terraform {
  backend "s3" {
    bucket = "s3-libranza-mto-terraform-dev"
    key    = "state/#name/terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = "us-east-1"
}

# Recurso IAM Role
resource "aws_iam_role" "lambda_role" {
  name = "#name"

  assume_role_policy = jsonencode({
    Version   = "2012-10-17",
    Statement = [
      {
        Effect    = "Allow",
        Principal = {
          Service = [ "lambda.amazonaws.com","apigateway.amazonaws.com"]
        },
        Action    = "sts:AssumeRole"
      }
    ]
  })

  tags = {
    Project = "Libranza MTO"
  }
}

resource "aws_iam_role_policy_attachment" "ec2_attachment" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2FullAccess"
}

resource "aws_iam_role_policy_attachment" "network_attachment" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSNetworkManagerFullAccess"
}

resource "aws_iam_role_policy_attachment" "sqs" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSQSFullAccess"
}


resource "aws_iam_policy" "lambda_secrets_policy" {
  name        = "#name-lambda-secrets-policy"
  description = "IAM policy for accessing Secrets Manager"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect   = "Allow",
        Action   = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret",
        ],
        Resource = "*",
      }
    ],
  })
}

resource "aws_iam_policy" "lambda_invoke_policy" {
  name        = "#name-lambda-invoke"
  description = "IAM policy for invoking Lambda Function"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect   = "Allow",
        Action   = "lambda:InvokeFunction",
        Resource = "*",
      }
    ],
  })
}

resource "aws_iam_role_policy_attachment" "lambda_policy" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_iam_role_policy_attachment" "logs_attachment" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchLogsFullAccess"
}

resource "aws_iam_role_policy_attachment" "lambda_secrets_attachment" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = aws_iam_policy.lambda_secrets_policy.arn
}
resource "aws_iam_role_policy_attachment" "lambda_invoke_attachment" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = aws_iam_policy.lambda_invoke_policy.arn
}


# Recurso Lambda Function
resource "aws_lambda_function" "my_lambda_function" {
  function_name    = "#name"
  role             = aws_iam_role.lambda_role.arn
  handler          = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
  runtime          = "java17"
  filename         = "build/bp-librz-mto-params-segurosvoluntarios-0.0.1-SNAPSHOT-aws.jar"
  timeout          = 60
  vpc_config {
    subnet_ids         = ["#subnet1", "#subnet2"]  # IDs de tus subnets existentes
    security_group_ids = ["#sg"]  # ID de tu security group existente
  }

  environment {
    variables = {
      ENVIRONMENT = "#env"
    }
  }

  tags = {
    Project = "Libranza MTO"
  }
}

# Recurso API Gateway
resource "aws_api_gateway_rest_api" "my_api" {
  name        = "#name"

  endpoint_configuration {
    types = ["REGIONAL"]
  }

  tags = {
    project = "Libranza MTO"
  }
}