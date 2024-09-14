package me.stilblue.killStamina.data;

import java.util.HashMap;
import java.util.Map;

public class StaminaCostManager {
    private final Map<String, StaminaCost> staminaMap = new HashMap<>();

    public StaminaCost getStaminaCost(String mobID) {
        return staminaMap.get(mobID);
    }

    public void addStaminaCost(StaminaCost cost) {
        staminaMap.put(cost.getInternalID(), cost);
    }

    public void clearAll() {
        staminaMap.clear();
    }
}
