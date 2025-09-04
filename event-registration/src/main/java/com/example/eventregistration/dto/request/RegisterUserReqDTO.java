package com.example.eventregistration.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserReqDTO {

    @NotNull(message = "Username is required")
    String username;

    @NotNull(message = "Password is required")
    String password;
}
