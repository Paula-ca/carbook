package Grupo7.Autitos.security;

import Grupo7.Autitos.entity.Usuario;
import Grupo7.Autitos.repository.UsuarioRepository;
import Grupo7.Autitos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRol().getNombre().name());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getContrasenia(),
                List.of(authority)  // Spring Security expects a Collection
        );
    }

}