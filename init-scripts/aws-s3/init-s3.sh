#!/bin/bash
set -e

echo "Initializing S3 bucket..."
awslocal s3 mb s3://ms-audio-bucket
echo "S3 bucket initialized successfully."