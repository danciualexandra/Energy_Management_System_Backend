package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.dtos.RegisterDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.UUID;

@Service
public class RegisterService {
    @Autowired
    private  UserRepository userRepository;
    public UUID register(RegisterDTO dto) {
        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .type(dto.getType())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress()).build();
        return userRepository.save(user).getId();

    }
}
