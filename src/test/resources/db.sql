CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(250) NOT NULL,
    password VARCHAR(80)
);

CREATE TABLE IF NOT EXISTS roles(
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS users_roles(
    user_id BIGSERIAL,
    role_id BIGSERIAL,
    CONSTRAINT users_roles_pk PRIMARY KEY (user_id, role_id),
      CONSTRAINT FK_users
          FOREIGN KEY (user_id) REFERENCES users(id),
      CONSTRAINT FK_roles
          FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS messages(
    id BIGSERIAL PRIMARY KEY,
    email_to VARCHAR(250),
    message TEXT,
    subject VARCHAR(50),
    filename VARCHAR(50),
    attachment VARCHAR(450),
    template_location VARCHAR(450),
    status VARCHAR(14) NOT NULL DEFAULT 'READY_TO_SEND',
    send_date DATE NOT NULL,
    send_time TIME NOT NULL
);

INSERT INTO users(email, password) VALUES
('vajiro2902@fulwark.com', '$2a$12$to.c0WLU.TTdqS5qHX9jJuR6ZCjKrM56697nYha2vw6okf.RKaYwC');

INSERT INTO roles(role_name) VALUES('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO users_roles(user_id, role_id) VALUES(1,1);

INSERT INTO messages(filename, attachment, send_date, send_time) VALUES
('cryptographic.pdf', 'src/main/resources/static/documents/cryptographic.pdf', '2023-06-24', '11:16');

