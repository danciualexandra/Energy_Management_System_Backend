package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.services.SensorService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/sensor")
public class SensorController {
    @Autowired
    private SensorService sensorService;
    @GetMapping("/all")
    public ResponseEntity<List<SensorDTO>> getAllSensors(){
        List<SensorDTO> sensors =sensorService.getAll();
        return new ResponseEntity<>(sensors, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('Administrator', 'Client')")
    @GetMapping("/all/{uuid}")
    public ResponseEntity<List<SensorDTO>> getAllByUserId(@PathVariable UUID uuid){
        List<SensorDTO> sensors =sensorService.getAllByUserId(uuid);
        return new ResponseEntity<>(sensors, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @PostMapping("/addSensor")
    public ResponseEntity<UUID> addSensor(@RequestBody SensorDTO sensorDTO) {
        UUID sensorId = sensorService.insertSensor(sensorDTO);
        return new ResponseEntity<>(sensorId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity deleteSensorById(@PathVariable UUID uuid) {
        sensorService.deleteSensor(uuid);
        return new ResponseEntity<>(uuid, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @PutMapping(value = "/update/{uuid}")
    public ResponseEntity<UUID> updateSensor(@PathVariable UUID uuid,  @RequestBody SensorDTO sensorDTO) {
        UUID sensorUUID= sensorService.updateSensor(uuid,sensorDTO );
        return new ResponseEntity<>(sensorUUID, HttpStatus.OK);
    }


}
