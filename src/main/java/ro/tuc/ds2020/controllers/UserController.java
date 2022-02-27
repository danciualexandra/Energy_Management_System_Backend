package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @PostMapping("/addUser")
    public ResponseEntity<UUID> addUser(@RequestBody UserDTO userDTO) {
        UUID UserID = userService.insertUser(userDTO);
        return new ResponseEntity<>(UserID, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOS = userService.findUsers();
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity deleteUserById(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
        return new ResponseEntity<>(uuid, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @PutMapping(value = "/update/{uuid}")
    public ResponseEntity<UUID> updateUser(@PathVariable UUID uuid, @RequestBody UserDTO userDTO) {
        UUID UserUUID = userService.updateUser(uuid, userDTO);
        return new ResponseEntity<>(UserUUID, HttpStatus.OK);
    }
}
