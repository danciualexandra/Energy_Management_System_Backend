package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.services.DeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PreAuthorize("hasAuthority('Administrator')")
    @PostMapping("/add")
    public ResponseEntity<UUID> addDevice(@RequestBody DeviceDTO deviceDTO){
        UUID deviceId = deviceService.addDevice(deviceDTO);
        return new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('Administrator', 'Client')")
    @GetMapping("/all")
    public ResponseEntity<List<Device>> getAllDevices(){
        List<Device> devices = deviceService.getAll();
        return new ResponseEntity<>(devices,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('Administrator', 'Client')")
    @GetMapping("/all/{uuid}")
    public ResponseEntity<List<Device>> getAllDevicesByUserId(@PathVariable UUID uuid){
        List<Device> devices = deviceService.getAllByUserId(uuid);
        return new ResponseEntity<>(devices,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('Administrator', 'Client')")
    @GetMapping("/get/{uuid}")
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable UUID uuid){
        Device device = deviceService.findDeviceById(uuid);
        return new ResponseEntity<>(DeviceBuilder.toDTO(device),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Administrator')")
    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity deleteDeviceById(@PathVariable UUID uuid){
      deviceService.deleteDevice(uuid);
        return new ResponseEntity<>(uuid, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('Administrator')")
    @PutMapping(value = "/update/{uuid}")
    public ResponseEntity<UUID> updateDevice(@PathVariable UUID uuid,  @RequestBody DeviceDTO deviceDTO) {
        UUID deviceUUID= deviceService.updateDevice(uuid, deviceDTO);
        return new ResponseEntity<>(deviceUUID, HttpStatus.OK);
    }
}
