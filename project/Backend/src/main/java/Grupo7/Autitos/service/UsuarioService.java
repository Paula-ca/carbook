package Grupo7.Autitos.service;

import Grupo7.Autitos.entity.Producto;
import Grupo7.Autitos.entity.Usuario;
import Grupo7.Autitos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario add(Usuario usuario) {
        if(usuario.getNombre() != null
                && usuario.getApellido() != null
                && usuario.getEmail() != null
                && usuario.getContrasenia() != null
                && usuario.getCiudad() != null) {
            return usuarioRepository.save(usuario);
        } else {
            return null;
        }
    }
    public Usuario find(Long id){
        return usuarioRepository.findById(id).orElse(null);
    }

    public List<Usuario> list() {
        return usuarioRepository.findAll();
    }

    public Usuario userByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Boolean existsUserByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

}