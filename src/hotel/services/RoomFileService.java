package hotel.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hotel.interfaces.IRoomFileService;
import hotel.models.Room;

public class RoomFileService implements IRoomFileService {
    @Override
    public void saveRooms(Map<Integer, Room> rooms, String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(rooms);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Integer, Room> loadRooms(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            Object data = in.readObject();
            if (data instanceof Map<?, ?> map) {
                return (Map<Integer, Room>) map;
            }
            if (data instanceof List<?> list) {
                Map<Integer, Room> result = new LinkedHashMap<>();
                for (Object item : list) {
                    Room room = (Room) item;
                    result.put(room.getRoomNumber(), room);
                }
                return result;
            }
            throw new IOException("Unsupported rooms data format");
        }
    }
}
