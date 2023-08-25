package com.gmail.kuznetsov.msg.sender.emailsender.repositories.models;

import com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Objects;

/**
 * Модель, отображающая информацию о пользователе, для ее передачи по сети.
 */
@Getter
@Setter
public class UserDto {
    private Long id;
    @NotNull
    @Email
    @Pattern(regexp = "^(([a-zA-Z0-9_-]+\\.)*[a-zA-Z0-9_-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-z]{2,6})?$")
    private String email;
    private String password;
    private Collection<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return email.equals(userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
