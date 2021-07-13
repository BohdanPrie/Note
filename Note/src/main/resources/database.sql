-- Database: notesapp

-- DROP DATABASE notesapp;

CREATE DATABASE notesapp
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.utf8'
    LC_CTYPE = 'Russian_Russia.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
    
    
-- SCHEMA: notes

-- DROP SCHEMA notes ;

CREATE SCHEMA notes
    AUTHORIZATION postgres;
    
    
    
-- Table: notes.notes

-- DROP TABLE notes.notes;

CREATE TABLE notes.notes
(
    user_login character varying COLLATE pg_catalog."default" NOT NULL,
    title character varying COLLATE pg_catalog."default",
    body character varying COLLATE pg_catalog."default",
    id integer,
    time_creation timestamp without time zone NOT NULL,
    time_change timestamp without time zone NOT NULL,
    CONSTRAINT notes_user_login_fkey FOREIGN KEY (user_login)
        REFERENCES notes.users (login) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE notes.notes
    OWNER to postgres;
    
    
    
-- Table: notes.to_do

-- DROP TABLE notes.to_do;

CREATE TABLE notes.to_do
(
    user_login character varying COLLATE pg_catalog."default" NOT NULL,
    body character varying COLLATE pg_catalog."default",
    id integer,
    line_id integer,
    marked boolean,
    CONSTRAINT notes_user_login_fkey FOREIGN KEY (user_login)
        REFERENCES notes.users (login) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE notes.to_do
    OWNER to postgres;
    
    
-- Table: notes.to_do_line

-- DROP TABLE notes.to_do_line;

CREATE TABLE notes.to_do_line
(
    user_login character varying COLLATE pg_catalog."default" NOT NULL,
    id integer NOT NULL,
    time_creation timestamp without time zone NOT NULL,
    time_change timestamp without time zone NOT NULL,
    title character varying COLLATE pg_catalog."default",
    CONSTRAINT notes_user_login_fkey FOREIGN KEY (user_login)
        REFERENCES notes.users (login) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE notes.to_do_line
    OWNER to postgres;
    
    
    
-- Table: notes.users

-- DROP TABLE notes.users;

CREATE TABLE notes.users
(
    login character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default",
    CONSTRAINT "Notes_pkey" PRIMARY KEY (login)
)

TABLESPACE pg_default;

ALTER TABLE notes.users
    OWNER to postgres;