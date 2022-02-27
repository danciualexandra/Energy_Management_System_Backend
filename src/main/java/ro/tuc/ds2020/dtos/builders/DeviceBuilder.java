package ro.tuc.ds2020.dtos.builders;

import lombok.NoArgsConstructor;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;

@NoArgsConstructor
public class DeviceBuilder {
    public static DeviceDTO toDTO(Device device){
        return new DeviceDTO(device.getId(), device.getDescription(), device.getLocation(), device.getMaxEnergyConsumption(), device.getAvgEnergyConsumption(), null);
    }
    public static Device toEntity(DeviceDTO deviceDTO){
        return Device.builder()
                .id(deviceDTO.getId())
                .description(deviceDTO.getDescription())
                .location(deviceDTO.getLocation())
                .maxEnergyConsumption(deviceDTO.getMaxEnergyConsumption())
                .avgEnergyConsumption(deviceDTO.getAvgEnergyConsumption())
                .build();
    }
}
