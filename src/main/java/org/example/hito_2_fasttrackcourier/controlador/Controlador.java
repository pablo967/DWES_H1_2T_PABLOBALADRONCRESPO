package org.example.hito_2_fasttrackcourier.controlador;

import org.example.hito_2_fasttrackcourier.modelo.Entrega;
import org.example.hito_2_fasttrackcourier.modelo.Usuario;
import org.example.hito_2_fasttrackcourier.repositories.EntregaRepositories;
import org.example.hito_2_fasttrackcourier.repositories.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Controller
public class Controlador {
    @Autowired
    UserRepositories userRepositories;

    @Autowired
    EntregaRepositories entregaRepositories;

    @RequestMapping("/")
    public ModelAndView peticion1(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        String usuario;
        if (aut == null) {
            usuario = "Sin inicio de sesión";
            mv.setViewName("index");
        } else {
            Optional<Usuario> userOpt = userRepositories.findById(aut.getName());
            if (userOpt.isPresent()) {
                Usuario user = userOpt.get();
                usuario = user.getDni() + " - " + user.getNombre() + " Rol: " + user.getRol();
                mv.addObject("usuario", usuario);

                // Redirección basada en el rol
                if (user.getRol().equalsIgnoreCase("administrador")) {
                    mv.setViewName("admin");
                } else if (user.getRol().equalsIgnoreCase("conductor")) {
                    mv.setViewName("conductor");
                }
            } else {
                mv.addObject("usuario", "Usuario no encontrado");
                mv.setViewName("index");
            }
        }
        return mv;
    }

    @RequestMapping("/informe")
    public ModelAndView peticionInforme() {
        ModelAndView mv = new ModelAndView();

        int totalPedidos = (int) entregaRepositories.count();
        List<Entrega> entregas = entregaRepositories.findAll();

        mv.addObject("totalPedidos", totalPedidos);
        mv.addObject("entregas", entregas);
        mv.setViewName("admin_informe");

        return mv;
    }
    @RequestMapping("/login")
    public ModelAndView peticionLogin() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        //mv.setViewName("admin_informe");

        return mv;
    }

    @PostMapping("/nuevoRegistro")
    public ModelAndView registrarEntrega(
            @RequestParam String domicilio,
            @RequestParam String dniCliente,
            @RequestParam String nombreCliente,
            @RequestParam Entrega.EstadoEntrega estado,
            @RequestParam String dniAdministrador,
            @RequestParam String dniConductor,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Timestamp fechaHoraRegistro,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Timestamp fechaHoraSalida,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaEntregaPrevista,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Timestamp fechaHoraEntrega
    ) {
        Entrega nuevaEntrega = new Entrega(
                domicilio,
                dniCliente,
                nombreCliente,
                estado,
                dniAdministrador,
                dniConductor,
                fechaHoraRegistro,
                fechaHoraSalida,
                fechaEntregaPrevista,
                fechaHoraEntrega
        );

        entregaRepositories.save(nuevaEntrega);
        return new ModelAndView("perfil");
    }
}
