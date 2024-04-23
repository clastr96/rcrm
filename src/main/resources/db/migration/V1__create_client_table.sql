CREATE TABLE client (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    reg_number VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE (reg_number)
);

CREATE INDEX idx_client_reg_number ON client (reg_number);