package hotel.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import hotel.interfaces.IRoomTypeFileService;
import hotel.models.RoomType;

public class DataStreamService implements IRoomTypeFileService {
    @Override
    public void saveRoomTypes(Map<String, RoomType> roomTypes, String fileName) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
            out.writeInt(roomTypes.size());
            for (RoomType type : roomTypes.values()) {
                out.writeUTF(type.getName());
                out.writeDouble(type.getPricePerNight());
                out.writeInt(type.getCapacity());
            }
        }
    }

    @Override
    public Map<String, RoomType> loadRoomTypes(String fileName) throws IOException {
        Map<String, RoomType> result = new LinkedHashMap<>();
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                String name = in.readUTF();
                double price = in.readDouble();
                int capacity = in.readInt();
                RoomType roomType = new RoomType(name, price, capacity);
                result.put(name.trim().toLowerCase(), roomType);
            }
        }
        return result;
    }
}
