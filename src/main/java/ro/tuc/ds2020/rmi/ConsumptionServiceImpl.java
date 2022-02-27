package ro.tuc.ds2020.rmi;

import org.springframework.beans.factory.annotation.Autowired;
import ro.tuc.ds2020.dtos.ConsumptionDTO;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.builders.SensorBuilder;
import ro.tuc.ds2020.entities.Measurement;
import ro.tuc.ds2020.entities.Sensor;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.services.MeasurementService;
import ro.tuc.ds2020.services.SensorService;
import ro.tuc.ds2020.services.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class ConsumptionServiceImpl implements ConsumptionService {
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private UserService userService;

    public ConsumptionServiceImpl(MeasurementService measurementService, SensorService sensorService, UserService userService) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.userService = userService;
    }

    @Override
    public String sendMessage(String clientMessage) {
        return clientMessage;
    }


    @Override
    public UUID login(String username, String password) {
        User user = userService.findByUsernameAndPassword(username, password);
        if (user == null) {
            return null;
        } else {
            return user.getId();
        }
    }


    public double getEnergyConsumption(String day, int hour, UUID uuid) {
        //UUID uuid = UUID.fromString("2ee4ec81-fd1f-4379-b775-dd8a59cc52ad");
        List<MeasurementDTO> measurementList = measurementService.getAllByUserId(uuid);
        //return measurementList.stream()
        // .map(this::mapMeasurementToConsumption)
        //.filter(x -> x.getCalendar().get(Calendar.DAY_OF_MONTH) == day && x.getCalendar().get(Calendar.HOUR_OF_DAY) == hour)
        // .map(ConsumptionDTO::getValue)
        //.reduce(0.0, Double::sum);
        return 0;
    }

    @Override
    public Map<Integer, Map<Integer, Double>> getEnergyConsumptionDDays(Calendar calendar, int days, UUID uuid) {
        Map<Integer, Map<Integer, Double>> map = new HashMap<>();
        for(int i = 1 ; i <= days; i++){
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            map.put(i, getEnergyConsumptionForADay(calendar, uuid));
        }
        return map;
    }

    public Map<Integer, Double> getEnergyConsumptionForADay(Calendar calendar, UUID uuid) {
        Map<Integer, Double> map = new HashMap<>();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        List<Sensor> sensors = sensorService.getAllByUserId(uuid).stream().map(SensorBuilder::toEntity).collect(Collectors.toList());
        for (int i = 0; i < 24; i++) {
            double sum = getSumForAllDevicesFromStartingHour(sensors, year, month, day, i);
            map.put(i, sum);
        }
        return map;
    }

    @Override
    public Map<Integer, Double> getBaseline(Calendar calendar, int days, UUID uuid) {
        Map<Integer, Double> resultMap = new HashMap<>();
        Map<Integer, Map<Integer, Double>> map = getEnergyConsumptionDDays(calendar, days, uuid);
        for(int i = 0; i < 24; i++){
            double sum = 0.0;
            for (Map.Entry<Integer, Map<Integer, Double>> entry : map.entrySet()) {
                for (Map.Entry<Integer, Double> valueMap : entry.getValue().entrySet()) {
                    sum += valueMap.getValue();
                }
            }
            resultMap.put(i, sum / days);
        }
        return resultMap;
    }

    private double getSumForAllDevicesFromStartingHour(List<Sensor> sensors, int year, int month, int day, int hour) {
        double sum = 0.0;
        for (Sensor sensor : sensors) {
            sum += getAverageForADeviceFromStartingHour(sensor, year, month, day, hour);
        }
        return sum;
    }

    private double getAverageForADeviceFromStartingHour(Sensor sensor, int year, int month, int day, int hour) {
        double sum = sensor.getMeasurementList().stream()
                .map(this::mapMeasurementToConsumption)
                .filter(x -> x.getCalendar().get(Calendar.YEAR) == year &&
                        x.getCalendar().get(Calendar.MONTH) >= month - 1 &&
                        x.getCalendar().get(Calendar.DAY_OF_MONTH) >= day &&
                        x.getCalendar().get(Calendar.HOUR_OF_DAY) >= hour &&
                        x.getCalendar().get(Calendar.HOUR_OF_DAY) < hour + 1)
                .map(ConsumptionDTO::getValue)
                .reduce(0.0, Double::sum);
        return sum / 6;
    }

    private ConsumptionDTO mapMeasurementToConsumption(Measurement measurement) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(measurement.getTimestamp().getTime());
        ConsumptionDTO consumptionDTO = new ConsumptionDTO(cal, measurement.getValue());
        return consumptionDTO;
    }
}
