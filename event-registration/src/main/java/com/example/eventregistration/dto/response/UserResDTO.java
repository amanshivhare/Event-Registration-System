package com.example.eventregistration.dto.response;

import com.example.eventregistration.model.Authority;
import com.example.eventregistration.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResDTO {
    private Long id;
    private String username;
    private boolean enabled;
    private Set<String> authorities;

    public UserResDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.authorities = user.getAuthorities()
                .stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());
    }
}
