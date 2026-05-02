package hotel.interfaces;

import java.io.IOException;
import java.util.Map;

import hotel.models.Room;

public interface IRoomFileService {
    void saveRooms(Map<Integer, Room> rooms, String fileName) throws IOException;
    Map<Integer, Room> loadRooms(String fileName) throws IOException, ClassNotFoundException;
}
