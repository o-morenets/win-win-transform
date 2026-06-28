-- V1__init_schema.sql

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- -------------------------------------------------------
-- users
-- -------------------------------------------------------
CREATE TABLE users (
                       id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- -------------------------------------------------------
-- processing_log
-- -------------------------------------------------------
CREATE TABLE processing_log (
                                id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                                user_id      UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                input_text   TEXT        NOT NULL,
                                output_text  TEXT        NOT NULL,
                                created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_processing_log_user_id ON processing_log(user_id);
CREATE INDEX idx_processing_log_created_at ON processing_log(created_at);