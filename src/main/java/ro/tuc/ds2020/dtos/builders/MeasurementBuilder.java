package ro.tuc.ds2020.dtos.builders;

import lombok.NoArgsConstructor;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;

@NoArgsConstructor
public class MeasurementBuilder {
    public static MeasurementDTO toDTO(Measurement measurement){
        return new MeasurementDTO(measurement.getId(),
                measurement.getTimestamp(), measurement.getValue(),measurement.getSensor().getId(), measurement.getSensor().getSensorDescription());
    }
    public static Measurement toEntity(MeasurementDTO measurementDTO){
        return Measurement.builder()
                .id(measurementDTO.getId())
                .timestamp(measurementDTO.getTimestamp())
                .value(measurementDTO.getValue())
                .build();
    }
}
