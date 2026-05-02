package hotel.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hotel.interfaces.IBookingFileService;
import hotel.models.Booking;

public class BookingFileService implements IBookingFileService {
    @Override
    public void saveBookings(Map<Integer, Booking> bookings, String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(bookings);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Integer, Booking> loadBookings(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            Object data = in.readObject();
            if (data instanceof Map<?, ?> map) {
                return (Map<Integer, Booking>) map;
            }
            if (data instanceof List<?> list) {
                Map<Integer, Booking> result = new LinkedHashMap<>();
                for (Object item : list) {
                    Booking booking = (Booking) item;
                    result.put(booking.getBookingId(), booking);
                }
                return result;
            }
            throw new IOException("Unsupported bookings data format");
        }
    }
}
