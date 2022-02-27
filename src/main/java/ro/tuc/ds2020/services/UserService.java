package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public List<UserDTO> findUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserBuilder::toDTO).collect(Collectors.toList());
    }

    public UUID insertUser(UserDTO userDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder().username(userDTO.getUsername())
                .password(encoder.encode(userDTO.getPassword()))
                .type(userDTO.getType())
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .birthDate(userDTO.getBirthDate())
                .address(userDTO.getAddress())
                .build();
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return userRepository.save(user).getId();
    }

    public UserDTO findUserById(UUID uuid) {
        Optional<User> UserOptional = userRepository.findById(uuid);
        if (!UserOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", uuid);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + uuid);
        }
        return UserBuilder.toDTO(UserOptional.get());

    }
    //
   @Transactional
    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    public UUID updateUser(UUID uuid, UserDTO userDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<User> optionalUser = userRepository.findById(uuid);
        if (!optionalUser.isPresent()) {
            LOGGER.error("User with id {} was not found in db", uuid);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + uuid);
        }
        User user = optionalUser.get();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBirthDate(userDTO.getBirthDate());
        user.setAddress(userDTO.getAddress());
        userRepository.save(user);
        LOGGER.debug("User with id {} was updated in db", user.getId());
        return user.getId();

    }


    public User findByUsernameAndPassword(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if(encoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = findByUsername(s);
        if(user != null) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(user.getType()));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + s);
        }
    }
}
