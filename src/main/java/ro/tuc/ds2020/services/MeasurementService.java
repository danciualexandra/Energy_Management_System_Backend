package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.builders.MeasurementBuilder;
import ro.tuc.ds2020.dtos.builders.SensorBuilder;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.repositories.MeasurementRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeasurementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);
    @Autowired
    private SensorService sensorService;
    @Autowired
    private MeasurementRepository measurementRepository;


    public List<MeasurementDTO> getAll() {
        List<Measurement> measurementList = measurementRepository.findAll();
        return measurementList.stream().map(MeasurementBuilder::toDTO).collect(Collectors.toList());
    }

    public MeasurementDTO findMeasurementById(UUID uuid) {
        Optional<Measurement> measurementOptional = measurementRepository.findById(uuid);
        if (!measurementOptional.isPresent()) {
            LOGGER.error("Measurement with id {} was not found in db", uuid);
            throw new ResourceNotFoundException(Measurement.class.getSimpleName() + " with id: " + uuid);
        }
        return MeasurementBuilder.toDTO(measurementOptional.get());
    }

    @Transactional
    public UUID addMeasurement(MeasurementDTO measurementDTO) {
        Sensor sensor = SensorBuilder.toEntity(sensorService.findSensorById(measurementDTO.getSensorId()));
        Measurement measurement = MeasurementBuilder.toEntity(measurementDTO);
        measurement.setSensor(sensor);
        return measurementRepository.save(measurement).getId();
    }

    @Transactional
    public List<MeasurementDTO> filterByTimestamp(UUID uuid, Timestamp timestamp) {
        List<Measurement> measurementList = measurementRepository.getAllByUserId(uuid);
        if (timestamp != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(timestamp);
            cal.add(Calendar.DAY_OF_WEEK, 1);
            Timestamp plusOneDayTimestamp = new Timestamp(cal.getTime().getTime());
            return measurementList.stream()
                    .filter(measurement -> measurement.getTimestamp().after(timestamp) && measurement.getTimestamp().before(plusOneDayTimestamp))
                    .map(MeasurementBuilder::toDTO)
                    .collect(Collectors.toList());
        }
        return measurementList.stream()
                .map(MeasurementBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public List<MeasurementDTO> getAllByUserId(UUID uuid) {
        List<Measurement> measurementList = measurementRepository.getAllByUserId(uuid);
        return measurementList.stream()
                .map(MeasurementBuilder::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MeasurementDTO getLastMeasurementForSensor(UUID sensorId) {
        List<Measurement> measurementList = measurementRepository.getAllByTimestamp(sensorId);
        List<MeasurementDTO> measurementDTOList = measurementList.stream()
                .map(MeasurementBuilder::toDTO)
                .collect(Collectors.toList());
        if (!measurementDTOList.isEmpty()) {

            return measurementDTOList.get(0);
        }
        return null;
    }


}
