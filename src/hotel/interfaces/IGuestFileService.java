package hotel.interfaces;

import java.io.IOException;
import java.util.Map;

import hotel.models.Guest;

public interface IGuestFileService {
    void saveGuests(Map<Integer, Guest> guests, String fileName) throws IOException;
    Map<Integer, Guest> loadGuests(String fileName) throws IOException, ClassNotFoundException;
}
