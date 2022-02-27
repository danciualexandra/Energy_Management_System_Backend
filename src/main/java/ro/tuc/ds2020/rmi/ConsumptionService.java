package ro.tuc.ds2020.rmi;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

public interface ConsumptionService {
    String sendMessage(String clientMessage);

    double getEnergyConsumption(String day,int hour,UUID uuid);

    Map<Integer, Map<Integer, Double>> getEnergyConsumptionDDays(Calendar calendar, int days, UUID uuid);

    Map<Integer, Double> getBaseline(Calendar calendar, int days, UUID uuid);




    UUID login(String username, String password) ;

}
