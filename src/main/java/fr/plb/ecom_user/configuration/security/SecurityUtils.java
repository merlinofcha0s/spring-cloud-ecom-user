package fr.plb.ecom_user.configuration.security;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

public class SecurityUtils {

    public static final String AUTHORITIES_KEY = "auth";
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

}
