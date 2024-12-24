package com.mycompany.myapp.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String CLIENTE = "ROLE_CLIENTE";

    public static final String BODEGA = "ROLE_BODEGA";

    public static final String FINANZAS = "ROLE_FINANZAS";

    public static final String COCINA = "ROLE_COCINA";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
