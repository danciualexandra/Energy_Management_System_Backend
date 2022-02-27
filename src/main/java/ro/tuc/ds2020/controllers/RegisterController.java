package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.RegisterDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.services.RegisterService;
import ro.tuc.ds2020.services.UserService;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @PostMapping("/success")
    public ResponseEntity<UUID> registerUser(@RequestBody RegisterDTO dto) {
        UUID uuid = registerService.register(dto);
        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }
}
