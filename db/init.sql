-- Recomendado: tudo em schema público mesmo, por enquanto
CREATE TABLE professor (
  id          BIGSERIAL PRIMARY KEY,
  nome        VARCHAR(100),
  email       VARCHAR(100) NOT NULL UNIQUE,
  senha       VARCHAR(255) NOT NULL
);

CREATE TABLE aluno (
  id          BIGSERIAL PRIMARY KEY,
  nome        VARCHAR(100),
  email       VARCHAR(100) NOT NULL UNIQUE,
  matricula   VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE administrador (
  id          BIGSERIAL PRIMARY KEY,
  nome        VARCHAR(100),
  email       VARCHAR(100) NOT NULL UNIQUE,
  senha       VARCHAR(255) NOT NULL
);

CREATE TABLE recurso (
  id          BIGSERIAL PRIMARY KEY,
  nome        VARCHAR(100),
  descricao   TEXT,
  disponivel  BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE turma (
  id            BIGSERIAL PRIMARY KEY,
  nome          VARCHAR(100),
  curso         VARCHAR(100),
  horario       VARCHAR(50),
  professor_id  BIGINT REFERENCES professor(id) ON DELETE SET NULL
);

CREATE TABLE evento (
  id            BIGSERIAL PRIMARY KEY,
  titulo        VARCHAR(100),
  descricao     TEXT,
  data_inicio   TIMESTAMP NOT NULL,
  data_fim      TIMESTAMP NOT NULL,
  professor_id  BIGINT REFERENCES professor(id) ON DELETE SET NULL
);

CREATE TABLE reserva (
  id         BIGSERIAL PRIMARY KEY,
  status     VARCHAR(50),
  evento_id  BIGINT NOT NULL REFERENCES evento(id)   ON DELETE CASCADE,
  recurso_id BIGINT NOT NULL REFERENCES recurso(id)  ON DELETE RESTRICT
);

-- Índices úteis
CREATE INDEX idx_evento_professor   ON evento(professor_id);
CREATE INDEX idx_turma_professor    ON turma(professor_id);
CREATE INDEX idx_reserva_evento     ON reserva(evento_id);
CREATE INDEX idx_reserva_recurso    ON reserva(recurso_id);
