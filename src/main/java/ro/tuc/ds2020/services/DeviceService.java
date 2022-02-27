package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.DeviceRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private UserService userService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private DeviceRepository deviceRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    @Transactional
    public UUID addDevice(DeviceDTO deviceDTO) {
        User user = UserBuilder.toEntity(userService.findUserById(deviceDTO.getUserId()));
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device.setUser(user);
        return deviceRepository.save(device).getId();
    }

    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    public List<Device> getAllByUserId(UUID userId) {
        return deviceRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteDevice(UUID uuid) {
        Device device = findDeviceById(uuid);
        if(device.getSensor()!=null) {
            sensorService.deleteSensor(device.getSensor().getId());
        }
        deviceRepository.delete(uuid);
    }

    public UUID updateDevice(UUID uuid, DeviceDTO deviceDTO) {
        Optional<Device> optionalDevice = deviceRepository.findById(uuid);
        if (!optionalDevice.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", uuid);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + uuid);
        }
        Device device = optionalDevice.get();
        device.setDescription(deviceDTO.getDescription());
        device.setLocation(deviceDTO.getLocation());
        device.setMaxEnergyConsumption(deviceDTO.getMaxEnergyConsumption());
        device.setAvgEnergyConsumption(deviceDTO.getAvgEnergyConsumption());

        deviceRepository.save(device);
        LOGGER.debug("Device with id {} was updated in db", device.getId());
        return device.getId();

    }

    public Device findDeviceById(UUID uuid) {
        Optional<Device> deviceOptional = deviceRepository.findById(uuid);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", uuid);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + uuid);
        }
        return deviceOptional.get();
    }


}
