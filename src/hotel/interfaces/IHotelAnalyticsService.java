package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import hotel.models.Booking;
import hotel.models.Room;

public interface IHotelAnalyticsService {
    int getTotalBookings(Map<Integer, Booking> bookings);
    double getAverageBookingPrice(Map<Integer, Booking> bookings);
    List<String> getMostPopularRoomTypes(Map<Integer, Booking> bookings);
    long getAvailableRoomsCount(Map<Integer, Room> rooms, Map<Integer, Booking> bookings, LocalDate date);
    long getOccupiedRoomsCount(Map<Integer, Room> rooms, Map<Integer, Booking> bookings, LocalDate date);
    List<String> getMostPopularRoomTypesForAgeRange(Map<Integer, Booking> bookings, int minAge, int maxAge);
}
