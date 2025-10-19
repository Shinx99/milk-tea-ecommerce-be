-- Migration: password_reset_tokens - create_table
-- Created: Fri Oct 17 10:17:12 PM +07 2025
-- Author: mango

-- Add your SQL statements below:

CREATE TABLE password_reset_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    token VARCHAR(255) UNIQUE NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);


CREATE INDEX idx_password_reset_token_user_id ON password_reset_tokens(user_id);
CREATE INDEX idx_password_reset_token_token_expiry ON password_reset_tokens(token, expiry_date);

