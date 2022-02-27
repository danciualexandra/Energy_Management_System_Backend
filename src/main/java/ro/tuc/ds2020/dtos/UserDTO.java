package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.Device;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO extends RepresentationModel<UserDTO> {
   private UUID id;
   private String username;
   private String password;
   private String type;
   private String firstname;
   private String lastname;
   private LocalDate birthDate;
   private String address;
   private List<DeviceDTO> deviceList;
}
