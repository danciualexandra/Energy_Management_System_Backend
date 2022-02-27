package ro.tuc.ds2020.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "sensor")
@Getter
@Setter
@NoArgsConstructor
public class Sensor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    private String sensorDescription;
    private double maxValue;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Builder
    public Sensor(UUID id, String sensorDescription, double maxValue, Device device) {
        this.id = id;
        this.sensorDescription = sensorDescription;
        this.maxValue = maxValue;
        this.device = device;
    }

    @Builder
    public Sensor(UUID id, String sensorDescription, double maxValue) {
        this.id = id;
        this.sensorDescription = sensorDescription;
        this.maxValue = maxValue;
    }
    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Measurement> measurementList=new ArrayList<>();

    public Sensor(UUID id,String sensorDescription,double maxValue,List<Measurement> measurementList){
          this.id=id;
          this.sensorDescription=sensorDescription;
          this.maxValue=maxValue;
          this.measurementList=measurementList;
    }
}
