package hotel.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hotel.interfaces.IGuestFileService;
import hotel.models.Guest;

public class GuestFileService implements IGuestFileService {
    @Override
    public void saveGuests(Map<Integer, Guest> guests, String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(guests);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Integer, Guest> loadGuests(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            Object data = in.readObject();
            if (data instanceof Map<?, ?> map) {
                return (Map<Integer, Guest>) map;
            }
            if (data instanceof List<?> list) {
                Map<Integer, Guest> result = new LinkedHashMap<>();
                for (Object item : list) {
                    Guest guest = (Guest) item;
                    result.put(guest.getId(), guest);
                }
                return result;
            }
            throw new IOException("Unsupported guests data format");
        }
    }
}
