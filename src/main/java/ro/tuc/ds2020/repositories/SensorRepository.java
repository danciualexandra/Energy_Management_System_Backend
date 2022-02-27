package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tuc.ds2020.entities.Sensor;

import java.util.List;
import java.util.UUID;
@Repository
public interface SensorRepository  extends JpaRepository<Sensor, UUID> {
    @Query("SELECT s from Sensor s where s.device.user.id = :userId")
    List<Sensor> getAllByUserId(@Param("userId") UUID userId);
    @Modifying
    @Query("delete from Sensor s where s.id =:id")
    void delete(@Param("id") UUID entityId);
}
