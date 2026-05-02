package hotel.models;

import java.io.Serializable;
import java.util.Objects;

public class RoomType implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final double pricePerNight;
    private final int capacity;

    public RoomType(String name, double pricePerNight, int capacity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Room type name must not be blank");
        }
        if (pricePerNight <= 0) {
            throw new IllegalArgumentException("Price per night must be positive");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "RoomType [name=" + name + ", pricePerNight=" + pricePerNight + ", capacity=" + capacity + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(capacity, name, pricePerNight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RoomType other)) {
            return false;
        }
        return capacity == other.capacity
                && Double.doubleToLongBits(pricePerNight) == Double.doubleToLongBits(other.pricePerNight)
                && Objects.equals(name, other.name);
    }
}
