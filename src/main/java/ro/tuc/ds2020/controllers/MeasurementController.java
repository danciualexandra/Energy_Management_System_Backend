package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.UserBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.services.MeasurementService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/measurement")
public class MeasurementController {
    @Autowired
    private MeasurementService measurementService;

    @PreAuthorize("hasAnyAuthority('Administrator', 'Client')")
    @GetMapping("/all/{uuid}")
    public ResponseEntity<List<MeasurementDTO>> getAllMeasurements(@PathVariable UUID uuid) {
        List<MeasurementDTO> measurementDTOList = measurementService.getAllByUserId(uuid);
        return new ResponseEntity<>(measurementDTOList, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('Administrator', 'Client')")
    @PutMapping("/all/filter/{uuid}")
    public ResponseEntity<List<MeasurementDTO>> getAllMeasurementsById(@PathVariable UUID uuid, @RequestBody LocalDateTime localDateTime) {
        LocalDateTime dateTimeWithoutTimeZone = localDateTime.plusHours(3);
        List<MeasurementDTO> measurementDTOList = measurementService.filterByTimestamp(uuid, Timestamp.valueOf(dateTimeWithoutTimeZone));
        return new ResponseEntity<>(measurementDTOList, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @PostMapping("/addMeasurement")
    public ResponseEntity<UUID> addMeasurement(@RequestBody MeasurementDTO measurementDTO) {
        UUID uuid = measurementService.addMeasurement(measurementDTO);
        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }
}
