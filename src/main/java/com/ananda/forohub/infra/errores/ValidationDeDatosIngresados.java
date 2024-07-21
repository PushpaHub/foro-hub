package com.ananda.forohub.infra.errores;

public class ValidationDeDatosIngresados extends RuntimeException {
    public ValidationDeDatosIngresados (String s) {
        super(s);
    }
}