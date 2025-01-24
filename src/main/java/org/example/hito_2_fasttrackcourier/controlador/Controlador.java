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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        ModelAndView mv = new ModelAndView("admin_informe");

        try {
            long totalPedidos = entregaService.countTotalEntregas();
            long totalRecibido = entregaService.countByEstado(Entrega.EstadoEntrega.recibido);
            long totalPreparado = entregaService.countByEstado(Entrega.EstadoEntrega.preparado);
            long totalEnCamino = entregaService.countByEstado(Entrega.EstadoEntrega.en_camino);
            long totalEntregado = entregaService.countByEstado(Entrega.EstadoEntrega.entregado);

            Map<String, Long> entregasPorConductor = entregaService.countEntregasPorConductor();

            mv.addObject("totalPedidos", totalPedidos);
            mv.addObject("totalRecibido", totalRecibido);
            mv.addObject("totalPreparado", totalPreparado);
            mv.addObject("totalEnCamino", totalEnCamino);
            mv.addObject("totalEntregado", totalEntregado);
            mv.addObject("entregasPorConductor", entregasPorConductor);

        } catch (Exception e) {
            logger.error("Error al generar el informe", e);
            mv.setViewName("error");
        }

        return mv;
    }

    @GetMapping("/miDesempeno")
    public ModelAndView mostrarDesempenoConductor(Authentication aut) {
        ModelAndView mv = new ModelAndView("conductor_desempeno");
        try {
            String dniConductor = aut.getName();

            Map<String, Object> metricas = entregaService.obtenerMetricasConductor(dniConductor);
            mv.addObject("metricas", metricas);

        } catch (Exception e) {
            logger.error("Error al obtener métricas de desempeño", e);
            mv.setViewName("redirect:/?error=desempeno");
        }
        return mv;
    }

    @RequestMapping(value = "/historial", method = RequestMethod.GET)
    public ModelAndView peticionHistorial() {
        ModelAndView mv = new ModelAndView("admin_historial");
        try {
            List<Entrega> todasEntregas = (List<Entrega>) entregaRepositories.findAll();

            Map<String, List<Entrega>> entregasPorConductor = todasEntregas.stream()
                    .collect(Collectors.groupingBy(
                            Entrega::getDniConductor,
                            LinkedHashMap::new, // Para mantener el orden
                            Collectors.toList()
                    ));

            mv.addObject("entregasPorConductor", entregasPorConductor);

        } catch (Exception e) {
            logger.error("Error al generar el historial", e);
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
                List<Entrega> entregas = entregaRepositories.findByDniConductorAndEstadoNot(
                        dniConductor,
                        Entrega.EstadoEntrega.entregado
                );

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

    @RequestMapping(value = "/conductorHistorial", method = RequestMethod.GET)
    public ModelAndView verHistorialConductor(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        try {
            if (aut != null) {
                String dniConductor = aut.getName();
                List<Entrega> historial = entregaRepositories.findByDniConductor(dniConductor);

                mv.addObject("historial", historial);
                mv.setViewName("conductor_historial");
            } else {
                mv.setViewName("redirect:/conductor");
            }
        } catch (Exception e) {
            logger.error("Error al obtener el historial", e);
            mv.setViewName("error");
        }
        return mv;
    }

    @GetMapping("/actualizarEntrega/{id}")
    public ModelAndView mostrarFormularioActualizacion(@PathVariable("id") Integer id, Authentication aut) {
        ModelAndView mv = new ModelAndView();
        try {
            Optional<Entrega> entregaOpt = entregaRepositories.findById(id);

            if (entregaOpt.isPresent()) {
                Entrega entrega = entregaOpt.get();
                // Verificar que el conductor es el asignado
                if (aut != null && entrega.getDniConductor().equals(aut.getName())) {
                    mv.addObject("entrega", entrega);
                    mv.setViewName("actualizar_entrega");
                } else {
                    mv.setViewName("redirect:/paquetesAsignados?error=no_autorizado");
                }
            } else {
                mv.setViewName("redirect:/paquetesAsignados?error=no_encontrado");
            }
        } catch (Exception e) {
            logger.error("Error al obtener la entrega", e);
            mv.setViewName("error");
        }
        return mv;
    }

    @PostMapping("/actualizarEntrega/{id}")
    public ModelAndView actualizarEstadoEntrega(
            @PathVariable("id") Integer id,
            @RequestParam("nuevoEstado") Entrega.EstadoEntrega nuevoEstado,
            Authentication aut) {

        ModelAndView mv = new ModelAndView();
        try {
            Optional<Entrega> entregaOpt = entregaRepositories.findById(id);

            if (entregaOpt.isPresent()) {
                Entrega entrega = entregaOpt.get();

                // Verificar autorización
                if (aut != null && entrega.getDniConductor().equals(aut.getName())) {
                    // Actualizar estado
                    entrega.setEstado(nuevoEstado);

                    // Registrar fecha/hora de entrega si corresponde
                    if (nuevoEstado == Entrega.EstadoEntrega.entregado) {
                        entrega.setFechaHoraEntrega(LocalDateTime.now());
                    }

                    entregaRepositories.save(entrega);
                    mv.setViewName("redirect:/paquetesAsignados?exito=true");
                } else {
                    mv.setViewName("redirect:/paquetesAsignados?error=no_autorizado");
                }
            } else {
                mv.setViewName("redirect:/paquetesAsignados?error=no_encontrado");
            }
        } catch (Exception e) {
            logger.error("Error al actualizar la entrega", e);
            mv.setViewName("error");
        }
        return mv;
    }


}
