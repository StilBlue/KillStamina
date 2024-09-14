package me.stilblue.killStamina.data;

public class StaminaCost {
    private final String internalID;
    private final int cost;

    public StaminaCost(String internalID, int cost) {
        this.internalID = internalID;
        this.cost = cost;
    }

    public String getInternalID() {
        return internalID;
    }

    public int getCost() {
        return cost;
    }
}
