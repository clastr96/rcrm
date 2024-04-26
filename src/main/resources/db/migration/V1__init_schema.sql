DROP TABLE IF EXISTS client;

CREATE TABLE client (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    reg_number VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    email VARCHAR(255) NULL,
    phone VARCHAR(255) NULL,
    upsert_date DATETIME NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_client_reg_number ON client (reg_number);



DROP TABLE IF EXISTS cron_lock;

CREATE TABLE cron_lock (
   id BIGINT(20) NOT NULL AUTO_INCREMENT,
   locked_at DATETIME NULL,
   PRIMARY KEY (id)
);

CREATE INDEX idx_locked_at ON cron_lock (locked_at);
