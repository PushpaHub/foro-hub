CREATE TABLE respuestas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensaje VARCHAR(400) NOT NULL,
    topico_id BIGINT NOT NULL,
    fecha DATETIME NOT NULL,
    autor_id BIGINT NOT NULL,
    solucion VARCHAR(20) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_respuesta_topico FOREIGN KEY (topico_id) REFERENCES topicos(id),
    CONSTRAINT fk_respuesta_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);
