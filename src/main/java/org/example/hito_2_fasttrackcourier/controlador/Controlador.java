package org.example.hito_2_fasttrackcourier.controlador;

import org.example.hito_2_fasttrackcourier.modelo.Entrega;
import org.example.hito_2_fasttrackcourier.modelo.Usuario;
import org.example.hito_2_fasttrackcourier.repositories.EntregaRepository;
import org.example.hito_2_fasttrackcourier.repositories.UsuarioRepository;
import org.example.hito_2_fasttrackcourier.servicios.EntregaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class Controlador {
    private static final Logger logger = LoggerFactory.getLogger(Controlador.class);

    @Autowired
    UsuarioRepository userRepositories;

    @Autowired
    EntregaRepository entregaRepositories;

    @Autowired
    private EntregaService entregaService;

    @RequestMapping("/")
    public ModelAndView peticion1(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if (aut == null) {
            mv.setViewName("redirect:/login");
        } else {
            Optional<Usuario> userOpt = userRepositories.findById(aut.getName());
            if (userOpt.isPresent()) {
                Usuario user = userOpt.get();
                String usuario = user.getDni() + " - " + user.getNombre() + " Rol: " + user.getRol();
                mv.addObject("usuario", usuario);

                // Redirección basada en el rol
                if (user.getRol().equalsIgnoreCase("administrador")) {
                    mv.setViewName("admin");
                } else if (user.getRol().equalsIgnoreCase("conductor")) {
                    mv.setViewName("conductor");
                }
            } else {
                mv.addObject("usuario", "Usuario no encontrado");
                mv.setViewName("login");
            }
        }
        return mv;
    }

    @RequestMapping(value = "/informe", method = RequestMethod.GET)
    public ModelAndView peticionInforme() {
        ModelAndView mv = new ModelAndView();

        try {
            int totalPedidos = (int) entregaRepositories.count();
            List<Entrega> entregas = (List<Entrega>) entregaRepositories.findAll();

            mv.addObject("totalPedidos", totalPedidos);
            mv.addObject("entregas", entregas);
            mv.setViewName("admin_informe");
        } catch (Exception e) {
            logger.error("Error al generar el informe", e);
            mv.setViewName("error");
        }

        return mv;
    }

    @RequestMapping("/login")
    public ModelAndView peticionLogin() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        //mv.setViewName("admin_informe");

        return mv;
    }

    @GetMapping("/registro")
    public ModelAndView mostrarFormularioRegistro(Authentication aut) {
        ModelAndView mv = new ModelAndView("admin_registro");


        List<Usuario> conductores = userRepositories.findByRol("conductor");
        mv.addObject("conductores", conductores);

        if (aut != null) {
            Optional<Usuario> userOpt = userRepositories.findById(aut.getName());
            if (userOpt.isPresent()) {
                Usuario user = userOpt.get();
                mv.addObject("dniAdministrador", user.getDni());
            }
        }

        return mv;
    }

    @RequestMapping("/perfil")
    public ModelAndView peticionPerfil() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("perfil");
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
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fechaHoraRegistro,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fechaHoraSalida,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaEntregaPrevista,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime fechaHoraEntrega


    ) {

        System.out.println(dniCliente +"mondongo");
        try {
            if (dniCliente == null || dniCliente.isEmpty()) {
                throw new IllegalArgumentException("DNI del cliente no puede ser nulo o vacío");
            }

            logger.info("Registrando entrega con los siguientes datos: domicilio={}, dniCliente={}, nombreCliente={}, estado={}, dniAdministrador={}, dniConductor={}, fechaHoraRegistro={}, fechaHoraSalida={}, fechaEntregaPrevista={}, fechaHoraEntrega={}",
                    domicilio, dniCliente, nombreCliente, estado, dniAdministrador, dniConductor, fechaHoraRegistro, fechaHoraSalida, fechaEntregaPrevista, fechaHoraEntrega);

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
            ModelAndView mv = new ModelAndView();
            mv.setViewName("admin");
            return mv;
        } catch (Exception e) {
            logger.error("Error al registrar la entrega", e);
            return new ModelAndView("error");
        }
    }

    @RequestMapping(value = "/paquetesAsignados", method = RequestMethod.GET)
    public ModelAndView verPaquetesAsignados(Authentication aut) {
        ModelAndView mv = new ModelAndView();

        try {
            if (aut != null) {
                String dniConductor = aut.getName();
                List<Entrega> entregas = entregaRepositories.findByDniConductor(dniConductor);

                mv.addObject("entregas", entregas);
                mv.setViewName("conductor_entregas");
            } else {
                mv.setViewName("redirect:/conductor");
            }
        } catch (Exception e) {
            logger.error("Error al obtener los paquetes asignados", e);
            mv.setViewName("error");
        }

        return mv;
    }
}
