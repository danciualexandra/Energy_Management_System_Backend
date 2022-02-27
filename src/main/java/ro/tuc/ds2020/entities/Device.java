package ro.tuc.ds2020.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "device")
@Getter
@Setter
public class Device implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    private String description;
    private String location;
    private double maxEnergyConsumption;
    private double avgEnergyConsumption;

    @ManyToOne()
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private User user;

    @Builder
    public Device(UUID id, String description, String location, double maxEnergyConsumption, double avgEnergyConsumption, User user) {
        this.id = id;
        this.description = description;
        this.location = location;
        this.maxEnergyConsumption = maxEnergyConsumption;
        this.avgEnergyConsumption = avgEnergyConsumption;
        this.user = user;
    }

    public Device(){}

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "device")
    @JsonBackReference
    private Sensor sensor;



}
