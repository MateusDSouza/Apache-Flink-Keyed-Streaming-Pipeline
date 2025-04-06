package org.streaming.job;

/**
 * Represents an aggregated flash sale event for a specific client. This class holds the
 * summary of a client's total purchases, including the total amount spent and the total
 * number of units bought during a flash sale event.
 * The idea behind the aggregation of this streaming is to know how much a given user pays in
 * our online platform and how many products they are buying.
 * To do this, we expect the result as a stream with different schema.
 * Following the same thought of process, the getters and setters were implemented.
 */
public class AggregatedFlashSaleEvent {
    private String clientId;
    private double total;
    private int totalUnitsBought;

    // Constructor, getters, and setters
    public AggregatedFlashSaleEvent(String clientId, double total, int totalUnitsBought) {
        this.clientId = clientId;
        this.total = total;
        this.totalUnitsBought = totalUnitsBought;
    }

    public String getClientId() {
        return clientId;
    }

    public double getTotal() {
        return total;
    }

    public int getTotalUnitsBought() {
        return totalUnitsBought;
    }

    @Override
    public String toString() {
        return "AggregatedFlashSaleEvent{" +
                "clientId='" + clientId + '\'' +
                ", total=" + total +
                ", totalUnitsBought=" + totalUnitsBought +
                '}';
    }
}
