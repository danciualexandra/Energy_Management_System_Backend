package ro.tuc.ds2020.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.tuc.ds2020.dtos.MeasurementRabbitMQ;
import ro.tuc.ds2020.dtos.MeasurementDTO;
import ro.tuc.ds2020.dtos.SensorDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.MeasurementService;
import ro.tuc.ds2020.services.SensorService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
public class Consumer {
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private DeviceService deviceService;

    public void consumeMessageFromQueue(byte[] message) {
        System.out.println("Message received from queue : " + message.toString());
        ObjectMapper mapper = new ObjectMapper();
        try {
            MeasurementRabbitMQ measurementRabbitMQ = mapper.readValue(message, MeasurementRabbitMQ.class);
            MeasurementDTO dto = new MeasurementDTO(null,
                    Timestamp.valueOf(measurementRabbitMQ.getTimestamp()),
                    measurementRabbitMQ.getValue(),
                    UUID.fromString(measurementRabbitMQ.getSensorId()),
                    null);
            checkPowerPeakValue(dto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkPowerPeakValue(MeasurementDTO dto) {
        measurementService.addMeasurement(dto);
        MeasurementDTO measurementDTO = measurementService.getLastMeasurementForSensor(dto.getSensorId());
        if (measurementDTO != null) {
            long differenceTime = dto.getTimestamp().getTime() - measurementDTO.getTimestamp().getTime();
            double peak = (dto.getValue() - measurementDTO.getValue()) / differenceTime;
            SensorDTO sensor = sensorService.findSensorById(dto.getSensorId());
            Device device = deviceService.findDeviceById(sensorService.findSensorById(dto.getSensorId()).getDeviceId());
            User user = device.getUser();
            if (user.getType().equals("Client")) {
                if (peak > sensor.getMaxValue()) {
                    this.template.convertAndSend("/topic/socket/client/" + user.getId(),
                            "Sensor exceeds it's maximum value: " + sensor.getMaxValue() + " ,actual value is:" + peak);
                } else {
                    this.template.convertAndSend("/topic/socket/client/" + user.getId(),
                            "Sensor does not exceeds it's maximum value: ");
                    System.out.println("nu dau");
                }
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    public void getMessage() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        String uri = "amqps://shehcgna:uJJ1TxFYmMe3W9mIWOiO4m6P-P3fkWem@rattlesnake.rmq.cloudamqp.com/shehcgna";
        ConnectionFactory factory = new ConnectionFactory();
        //Recommended settings
        factory.setUri(uri);
        factory.setConnectionTimeout(30000);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
        ) {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                consumeMessageFromQueue(delivery.getBody());
            };
            channel.basicConsume("myQueue", true, deliverCallback, consumerTag -> {
            });

        } catch (TimeoutException | IOException timeoutException) {
            timeoutException.printStackTrace();
        }
    }
}
