package Grupo7.Autitos.security.Jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = Logger.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        logger.error("Fallo en el metodo commence");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No esta autorizado");
    }

}