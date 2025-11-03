-- Recomendado: tudo em schema público mesmo, por enquanto
CREATE TABLE professor (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(100),
  email       VARCHAR(100) NOT NULL UNIQUE,
  password       VARCHAR(255) NOT NULL
);

CREATE TABLE student (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(100),
  email       VARCHAR(100) NOT NULL UNIQUE,
  registry_number   VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE admin (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(100),
  email       VARCHAR(100) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL
);

CREATE TABLE resource (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(100),
  description   TEXT,
  available  BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE class (
  id            BIGSERIAL PRIMARY KEY,
  name          VARCHAR(100),
  course        VARCHAR(100),
  schedule      VARCHAR(50),
  professor_id  BIGINT REFERENCES professor(id) ON DELETE SET NULL
);

CREATE TABLE event (
  id            BIGSERIAL PRIMARY KEY,
  title        VARCHAR(100),
  description     TEXT,
  begin_date   TIMESTAMP NOT NULL,
  end_date      TIMESTAMP NOT NULL,
  professor_id  BIGINT REFERENCES professor(id) ON DELETE SET NULL
);

CREATE TABLE reservation (
  id         BIGSERIAL PRIMARY KEY,
  status     VARCHAR(50),
  event_id  BIGINT NOT NULL REFERENCES event(id)   ON DELETE CASCADE,
  resource_id BIGINT NOT NULL REFERENCES resource(id)  ON DELETE RESTRICT
);

-- Índices úteis
CREATE INDEX idx_event_professor   ON event(professor_id);
CREATE INDEX idx_class_professor    ON class(professor_id);
CREATE INDEX idx_reservation_event     ON reservation(event_id);
CREATE INDEX idx_reservation_resource    ON reservation(resource_id);

INSERT INTO admin (name, email, password)
VALUES (
           'Default Administrator',
           'admin@example.com',
           '$2a$10$bOzuoH5zifz9OJQAbjZrZOB5C2ehQ17u2yCP/zx0HFk7LVa9oX/4G' --admin123
       );