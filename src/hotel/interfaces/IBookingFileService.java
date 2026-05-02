package hotel.interfaces;

import java.io.IOException;
import java.util.Map;

import hotel.models.Booking;

public interface IBookingFileService {
    void saveBookings(Map<Integer, Booking> bookings, String fileName) throws IOException;
    Map<Integer, Booking> loadBookings(String fileName) throws IOException, ClassNotFoundException;
}
