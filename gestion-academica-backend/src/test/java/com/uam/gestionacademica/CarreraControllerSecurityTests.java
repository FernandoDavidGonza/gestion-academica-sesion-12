package com.uam.gestionacademica;

import com.uam.gestionacademica.application.service.CarreraService;
import com.uam.gestionacademica.infrastructure.web.CarreraController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(CarreraControllerSecurityTests.Config.class)
class CarreraControllerSecurityTests {
    @Configuration
    @EnableMethodSecurity
    static class Config {
        @Bean CarreraService carreraService() {
            CarreraService service = mock(CarreraService.class);
            when(service.getAllCarreras()).thenReturn(List.of());
            return service;
        }
        @Bean CarreraController carreraController(CarreraService service) {
            return new CarreraController(service);
        }
    }
    @Autowired CarreraController controller;
    @Test @WithMockUser(roles = "ADMIN")
    void administradorPuedeListar() { assertEquals(200, controller.getAll().getStatusCode().value()); }
    @Test @WithMockUser(roles = "DOCENTE")
    void docentePuedeListar() { assertEquals(200, controller.getAll().getStatusCode().value()); }
    @Test @WithMockUser(roles = "ESTUDIANTE")
    void estudiantePuedeListar() { assertEquals(List.of(), controller.getAll().getBody()); }
    @Test @WithMockUser(roles = "INVITADO")
    void otroRolNoPuedeListar() { assertThrows(AccessDeniedException.class, () -> controller.getAll()); }
}
