package springboot.infrastructure.security.adapters.in.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springboot.application.security.dto.AssignRoleRequest;
import springboot.application.security.dto.UserResponse;
import springboot.application.security.usecase.AssignRoleUseCase;
import springboot.infrastructure.security.config.OpenApiConfig;

@RestController
@RequestMapping("/users")
@Tag(name = "Gestión de Usuarios", description = "Endpoints para la administración de usuarios y asignación de roles")
public class UserController {

    private final AssignRoleUseCase assignRoleUseCase;

    public UserController(AssignRoleUseCase assignRoleUseCase) {
        this.assignRoleUseCase = assignRoleUseCase;
    }

    @Operation(summary = "Asignar rol a usuario", description = "Permite a un administrador asignar un rol (USER, ADMIN, MODERATOR) a un usuario específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol asignado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado (se requiere rol ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/roles")
    public ResponseEntity<UserResponse> assignRole(
            @PathVariable String userId,
            @RequestBody AssignRoleRequest request) {
        UserResponse response = assignRoleUseCase.execute(userId, request);
        return ResponseEntity.ok(response);
    }
}
