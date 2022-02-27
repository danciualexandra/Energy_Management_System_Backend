package ro.tuc.ds2020.rmi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.support.RemoteExporter;
import ro.tuc.ds2020.services.MeasurementService;
import ro.tuc.ds2020.services.SensorService;
import ro.tuc.ds2020.services.UserService;

@Configuration
public class HessianConfig {
    private MeasurementService measurementService;
    private SensorService sensorService;
    private UserService userService;

    public HessianConfig(MeasurementService measurementService, SensorService sensorService, UserService userService) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.userService = userService;
    }

    @Bean(name = "/consumption")
    RemoteExporter consumptionService() {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(new ConsumptionServiceImpl(measurementService, sensorService, userService));
        exporter.setServiceInterface( ConsumptionService.class );
        return exporter;
    }
}
