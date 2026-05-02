package hotel.interfaces;

import java.io.IOException;
import java.util.Map;

import hotel.models.RoomType;

public interface IRoomTypeFileService {
    void saveRoomTypes(Map<String, RoomType> roomTypes, String fileName) throws IOException;
    Map<String, RoomType> loadRoomTypes(String fileName) throws IOException;
}
