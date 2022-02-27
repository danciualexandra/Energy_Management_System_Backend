package ro.tuc.ds2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeasurementDTO {
    private UUID id;
    private Timestamp timestamp;
    private double value;
    private UUID sensorId;
    private String sensorDescription;
}
