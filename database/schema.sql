-- ============================================================
--  NIT Mizoram Appointment System — Database Schema
--  Run this once to set up the database.
--  MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS nit_appointment
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE nit_appointment;

-- ----------------------------------------------------------------
-- 1. USERS  (authorities + students)
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,          -- store hashed in production
    role        ENUM('student','authority') NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------------------------------------------
-- 2. SLOTS  (available appointment windows per authority)
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS slots (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    authority   VARCHAR(80)  NOT NULL,
    slot_date   DATE         NOT NULL,
    slot_time   VARCHAR(20)  NOT NULL,          -- e.g. "10:00 - 10:15"
    available   BOOLEAN      DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_slot (authority, slot_date, slot_time)
);

-- ----------------------------------------------------------------
-- 3. APPOINTMENTS
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS appointments (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    slip_id         VARCHAR(30)  NOT NULL UNIQUE,  -- "APT-xxxxxxxx"
    enrollment_no   VARCHAR(20)  NOT NULL,
    authority       VARCHAR(80)  NOT NULL,
    slot_id         INT          NOT NULL,
    booked_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (slot_id) REFERENCES slots(id)
);

-- ================================================================
--  SEED DATA
-- ================================================================

-- Authority logins (password stored as plain text here for demo;
-- use BCrypt in production — see DBConnection.java notes)
INSERT IGNORE INTO users (username, password, role) VALUES
    ('director',    'admin123',   'authority'),
    ('registrar',   'admin123',   'authority'),
    ('admin',       'admin123',   'authority'),
    ('finance',     'admin123',   'authority'),
    ('erp',         'admin123',   'authority'),
    ('dean',        'admin123',   'authority');

-- A few sample student accounts
INSERT IGNORE INTO users (username, password, role) VALUES
    ('BT23CS001',   'student123', 'student'),
    ('BT23CS002',   'student123', 'student'),
    ('BT24EE010',   'student123', 'student');
