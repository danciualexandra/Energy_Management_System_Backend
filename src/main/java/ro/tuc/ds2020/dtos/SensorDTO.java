package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SensorDTO {
    private UUID id;
    private String sensorDescription;
    private double maxValue;
    private UUID deviceId;
    private List<MeasurementDTO> measurementDTOList;
}
