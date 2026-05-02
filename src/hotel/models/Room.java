package hotel.models;

import java.io.Serializable;
import java.util.Objects;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int roomNumber;
    private final RoomType type;

    public Room(int roomNumber, RoomType type) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be positive");
        }
        if (type == null) {
            throw new IllegalArgumentException("Room type must not be null");
        }
        this.roomNumber = roomNumber;
        this.type = type;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Room [roomNumber=" + roomNumber + ", type=" + type + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Room other)) {
            return false;
        }
        return roomNumber == other.roomNumber;
    }
}
