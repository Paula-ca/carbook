package CarbookApp.controller;

import CarbookApp.entity.Rol;
import CarbookApp.entity.Usuario;
import CarbookApp.entity.UsuarioResponse;
import CarbookApp.security.Jwt.JwtProvider;
import CarbookApp.security.Payload.LoginRequest;
import CarbookApp.security.Payload.LoginResponse;
import CarbookApp.security.Payload.RegisterRequest;
import CarbookApp.service.UsuarioService;
import jakarta.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    JwtProvider jwtProvider;

    public static final Logger logger = Logger.getLogger(AuthController.class);

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {

        logger.debug("Registrando usuario...");

        if (bindingResult.hasErrors()) {
            logger.error(bindingResult.getFieldError());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos invalidos");
        }
        if (usuarioService.existsUserByEmail(registerRequest.getEmail())) {
            logger.error("Correo ya registrado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este correo ya se encuentra registrado");
        }

        Usuario usuario = new Usuario(registerRequest.getNombre(),
                registerRequest.getApellido(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getContrasenia()),
                "");

        // rol por defecto: USER
        Rol rol = new Rol();
        rol.setId(3L);
        usuario.setRol(rol);

        usuarioService.add(usuario);
        logger.info("Usuario registrado: " + usuario.getEmail());

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        logger.debug("Logeando usuario...");

        if (bindingResult.hasErrors()) {
            logger.error("Error al logear");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getContrasenia()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        LoginResponse token = new LoginResponse(jwt);

        logger.info("Usuario logeado");

        Optional<Usuario> usuarioLogin = usuarioService.userByEmail(loginRequest.getEmail());

        UsuarioResponse usuario = new UsuarioResponse();
        usuario.setId(usuarioLogin.get().getId());
        usuario.setNombre(usuarioLogin.get().getNombre());
        usuario.setApellido(usuarioLogin.get().getApellido());
        usuario.setEmail(usuarioLogin.get().getEmail());
        usuario.setCiudad(usuarioLogin.get().getCiudad());
        usuario.setRol(usuarioLogin.get().getRol().getNombre().name());
        usuario.setToken(token.getToken());

        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

}