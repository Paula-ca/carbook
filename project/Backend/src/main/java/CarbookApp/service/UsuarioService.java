package CarbookApp.service;

import CarbookApp.entity.Usuario;
import CarbookApp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario add(Usuario usuario) {
        if(usuario.getNombre() == null
                || usuario.getApellido() == null
                || usuario.getEmail() == null
                || usuario.getContrasenia() == null
                || usuario.getCiudad() == null) {
            return null;
        }
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario1 : usuarios) {
            if (usuario.getEmail().equals(usuario1.getEmail())) {
                return null;
            }
        }
        return usuarioRepository.save(usuario);
    }
    public Usuario find(Long id){
        return usuarioRepository.findById(id).orElse(null);
    }

    public List<Usuario> list() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> userByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Boolean existsUserByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

}