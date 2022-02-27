package ro.tuc.ds2020.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class LoginDTO {
    private String role;
    private UUID uuid;
}
