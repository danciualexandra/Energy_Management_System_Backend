package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Measurement;

import java.util.List;
import java.util.UUID;
@Repository
public interface MeasurementRepository  extends JpaRepository<Measurement, UUID> {
    @Query("SELECT m from Measurement m where m.sensor.device.user.id = :uuid")
    List<Measurement> getAllByUserId(UUID uuid);

    @Query("SELECT m from Measurement m where m.sensor.id= :sensorId order by m.timestamp DESC")
    List<Measurement> getAllByTimestamp(UUID sensorId);
}
