package ro.tuc.ds2020.dtos.builders;

import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;

@NoArgsConstructor
public class UserBuilder {
    public static UserDTO toDTO(User user){
        List<DeviceDTO> deviceDTOList = user.getDeviceList().stream()
                .map(DeviceBuilder::toDTO)
                .collect(Collectors.toList());
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getType(), user.getFirstname(), user.getLastname(), user.getBirthDate(), user.getAddress(), deviceDTOList);
    }
    public static User toEntity(UserDTO userDTO){
        List<Device> deviceList= userDTO.getDeviceList().stream()
                .map(DeviceBuilder::toEntity)
                .collect(Collectors.toList());
        return new User(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getType(), userDTO.getFirstname(), userDTO.getLastname(), userDTO.getBirthDate(), userDTO.getAddress(), deviceList);
    }
}
