CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    mensaje VARCHAR(400) NOT NULL,
    fecha DATETIME NOT NULL,
    estatus VARCHAR(20) NOT NULL, -- Ajusta el tamaño según tus necesidades

    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_topico_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id),
    CONSTRAINT fk_topico_curso FOREIGN KEY (curso_id) REFERENCES cursos(id)
);
