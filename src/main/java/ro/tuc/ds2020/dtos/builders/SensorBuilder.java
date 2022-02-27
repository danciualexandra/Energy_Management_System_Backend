package ro.tuc.ds2020.dtos.builders;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.SensorRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
public class SensorBuilder {


    public static SensorDTO toDTO(Sensor sensor){
        List<MeasurementDTO> measurementDTOList = sensor.getMeasurementList().stream()
                .map(MeasurementBuilder::toDTO)
                .collect(Collectors.toList());

        return new SensorDTO(sensor.getId(),sensor.getSensorDescription(),sensor.getMaxValue(),sensor.getDevice().getId(),measurementDTOList);
    }
    public static Sensor toEntity(SensorDTO sensorDTO){
        List<Measurement> measurements = sensorDTO.getMeasurementDTOList().stream()
                .map(MeasurementBuilder::toEntity)
                .collect(Collectors.toList());
        return new Sensor(sensorDTO.getId(),sensorDTO.getSensorDescription(),sensorDTO.getMaxValue(),measurements);
   }

}
