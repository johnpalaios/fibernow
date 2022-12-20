package eu.advantage.fibernow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class UserCredentialsDto implements Serializable {
    @NotNull
    private String username;
    @NotNull
    private String password;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCredentialsDto userCredentialsDto = (UserCredentialsDto) o;
        return Objects.equals(getUsername(),
                userCredentialsDto.getUsername()) &&
                Objects.equals(getPassword(),
                        userCredentialsDto.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(),
                getPassword());
    }
}
