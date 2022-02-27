package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeviceDTO {
    private UUID id;
    private String description;
    private String location;
    private double maxEnergyConsumption;
    private double avgEnergyConsumption;
    private UUID userId;
}
