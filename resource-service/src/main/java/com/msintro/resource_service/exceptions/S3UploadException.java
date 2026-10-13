package com.msintro.resource_service.exceptions;

public class S3UploadException extends RuntimeException{
    public S3UploadException (){
        super("S3 uploading exception");
    }
}
