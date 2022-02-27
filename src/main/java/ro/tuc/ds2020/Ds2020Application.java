package ro.tuc.ds2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.remoting.support.RemoteExporter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import ro.tuc.ds2020.rmi.ConsumptionService;
import ro.tuc.ds2020.rmi.ConsumptionServiceImpl;

import java.util.TimeZone;

@SpringBootApplication
@Validated
@EnableScheduling
public class Ds2020Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Ds2020Application.class);
    }

    public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Ds2020Application.class, args);
    }

//    @Bean
//    ConsumptionService messengerService() {
//        return new ConsumptionServiceImpl();
//    }
//
//    @Bean
//    RmiServiceExporter exporter(ConsumptionService implementation) {
//
//        // Expose a service via RMI. Remote obect URL is:
//        // rmi://<HOST>:<PORT>/<SERVICE_NAME>
//        // 1099 is the default port
//
//        Class<ConsumptionService> serviceInterface = ConsumptionService.class;
//        RmiServiceExporter exporter = new RmiServiceExporter();
//        exporter.setServiceInterface(serviceInterface);
//        exporter.setService(implementation);
//        exporter.setServiceName(serviceInterface.getSimpleName());
//        exporter.setRegistryPort(1099);
//        return exporter;
//    }
}
