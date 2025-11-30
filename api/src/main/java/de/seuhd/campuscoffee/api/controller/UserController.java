package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.ports.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import de.seuhd.campuscoffee.api.exceptions.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.seuhd.campuscoffee.api.util.ControllerUtils.getLocation;

@Tag(name = "Users", description = "Operations related to user management.")
@Controller
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    //TODO: Implement user controller
    @Operation (
            summary = "Get all Users.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = UserDto.class)
                            ),
                            description = "All users as a json array"
                    )
            }
    )
    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(
                userService.getAll().stream()
                        .map(userDtoMapper::fromDomain)
                        .toList()
        );
    }

    @Operation(
            summary = "Get a user by ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            ),
                            description = "User with the provided id as a json object"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "No user with provided ID found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userDtoMapper.fromDomain(userService.getById(id))
        );
    }

    @Operation(
            summary = "Get a user by login name",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            ),
                            description = "User with the provided login name as a json object"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "No user with provided login name found"
                    )

            }
    )
    @GetMapping("/filter")
    public ResponseEntity<UserDto> filter(@RequestParam("login_name") String loginName) {
        return ResponseEntity.ok(
                userDtoMapper.fromDomain(userService.getByLoginName(loginName))
        );
    }

    @Operation(
            summary = "Create a new user",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            ),
                            description = "The new user as a json object."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "Failed to create user"
                    )
            }
    )
    @PostMapping("")
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDto) {
        UserDto created = upsert(userDto);
        return ResponseEntity
                .created(getLocation(created.id()))
                .body(created);
    }

    @Operation(
            summary = "Update an existing user by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            ),
                            description = "Updated user as a json object"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "Validation Error"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "User not found."
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        if(!id.equals(userDto.id())) {
            throw new IllegalArgumentException("User ID in path and body do not match.");
        }
        return ResponseEntity.ok(
                upsert(userDto)
        );
    }

    @Operation(
            summary = "Delete an existing user by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "The user was successfully deleted."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "The user with the provided ID couldn't be found."
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto upsert(UserDto userDto) {
        return userDtoMapper.fromDomain(
                userService.upsert(
                        userDtoMapper.toDomain(userDto)
                )
        );
    }
}
