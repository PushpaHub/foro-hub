package com.ananda.forohub.infra.errores;

public class ValidationDeRoles extends RuntimeException {
    public ValidationDeRoles (String s) {
        super(s);
    }
}