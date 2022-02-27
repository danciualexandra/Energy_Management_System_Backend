package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ApiExceptionResponse;
import ro.tuc.ds2020.dtos.CredentialsDTO;
import ro.tuc.ds2020.dtos.LoginDTO;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.Collections;


@Service
public class LoginService {
    @Autowired
    private UserService userService;

    public LoginDTO login(CredentialsDTO dto) throws ApiExceptionResponse {
        User user = userService.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        LoginDTO response = null;
        if (user == null) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Bad credentials"))
                    .message("User not found").status(HttpStatus.NOT_FOUND).build();
        } else {
            String role = user.getType().toUpperCase();
            response = LoginDTO.builder().uuid(user.getId()).role(role).build();
            if (dto.getPassword().equals(user.getPassword())) {
                return response;
            }
        }
        return response;
    }
}
