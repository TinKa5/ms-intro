
DROP TABLE IF EXISTS audio_record;
CREATE TABLE IF NOT EXISTS audio_record (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    audio_data BYTEA
)
