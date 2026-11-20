DROP TABLE IF EXISTS storage;
CREATE TABLE IF NOT EXISTS storage(
    id BIGSERIAL PRIMARY KEY,
    storage_type VARCHAR(100) NOT NULL,
    bucket VARCHAR(100) NOT NULL,
    path VARCHAR(100) NOT NULL
);

INSERT INTO storage (storage_type, bucket, path)
VALUES
    ('STAGING', 'staging-bucket', '/staging'),
    ('PERMANENT', 'permanent-bucket', '/permanent');