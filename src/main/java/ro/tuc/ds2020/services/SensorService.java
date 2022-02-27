package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.SensorBuilder;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.SensorRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SensorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private DeviceService deviceService;

    @Transactional
    public List<SensorDTO> getAll(){
        List<Sensor> sensors = sensorRepository.findAll();
        return sensors.stream().map(SensorBuilder::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<SensorDTO> getAllByUserId(UUID uuid){
        List<Sensor> sensors = sensorRepository.getAllByUserId(uuid);
        return sensors.stream().map(SensorBuilder::toDTO).collect(Collectors.toList());
    }

    public UUID insertSensor(SensorDTO sensorDTO) {
        Device device = deviceService.findDeviceById(sensorDTO.getDeviceId());
        Sensor sensor = Sensor.builder()
                .id(sensorDTO.getId())
                .sensorDescription(sensorDTO.getSensorDescription())
                .maxValue(sensorDTO.getMaxValue())
                .device(device)
                .build();
        LOGGER.debug("Sensor with id {} was inserted in db", sensor.getId());
        return sensorRepository.save(sensor).getId();
    }

    @Transactional
    public void deleteSensor(UUID uuid) {
        sensorRepository.deleteById(uuid);
    }

    public UUID updateSensor(UUID uuid, SensorDTO sensorDTO) {
        Optional<Sensor> optionalSensor= sensorRepository.findById(uuid);
        if (!optionalSensor.isPresent()) {
            LOGGER.error("Sensor with id {} was not found in db", uuid);
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + uuid);
        }
        Sensor sensor = optionalSensor.get();
        sensor.setSensorDescription(sensorDTO.getSensorDescription());
        sensor.setMaxValue(sensorDTO.getMaxValue());
        sensorRepository.save(sensor);
        LOGGER.debug("Sensor with id {} was updated in db", sensor.getId());
        return sensor.getId();

    }

    @Transactional
    public SensorDTO findSensorById(UUID uuid) {
        Optional<Sensor> sensorOptional = sensorRepository.findById(uuid);
        if (!sensorOptional.isPresent()) {
            LOGGER.error("Sensor with id {} was not found in db", uuid);
            throw new ResourceNotFoundException(Sensor.class.getSimpleName() + " with id: " + uuid);
        }
        return SensorBuilder.toDTO(sensorOptional.get());
    }
}
