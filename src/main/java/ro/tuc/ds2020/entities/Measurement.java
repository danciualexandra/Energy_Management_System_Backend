package ro.tuc.ds2020.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name="measurement")
@Getter
@Setter
@NoArgsConstructor
public class Measurement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    private UUID id;

    private Timestamp timestamp;
    private double value;


    @ManyToOne()
    @JoinColumn(name="sensor_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Sensor sensor;
    @Builder
    public Measurement(UUID id,Timestamp timestamp,double value,Sensor sensor){
        this.id=id;
        this.timestamp=timestamp;
        this.value=value;
        this.sensor=sensor;

    }



}
