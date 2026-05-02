package hotel.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hotel.interfaces.IHotelAnalyticsService;
import hotel.models.Booking;
import hotel.models.Room;

public class HotelAnalyticsService implements IHotelAnalyticsService {
    @Override
    public int getTotalBookings(Map<Integer, Booking> bookings) {
        return bookings == null ? 0 : bookings.size();
    }

    @Override
    public double getAverageBookingPrice(Map<Integer, Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return 0.0;
        }
        return bookings.values().stream().mapToDouble(Booking::getTotalPrice).average().orElse(0.0);
    }

    @Override
    public List<String> getMostPopularRoomTypes(Map<Integer, Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return List.of();
        }

        Map<String, Long> counts = bookings.values().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getRoom().getType().getName(),
                        Collectors.counting()
                ));

        long max = counts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        return counts.entrySet().stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public long getAvailableRoomsCount(Map<Integer, Room> rooms, Map<Integer, Booking> bookings, LocalDate date) {
        if (rooms == null || rooms.isEmpty()) {
            return 0;
        }
        return rooms.values().stream().filter(room -> isAvailable(room, bookings, date)).count();
    }

    @Override
    public long getOccupiedRoomsCount(Map<Integer, Room> rooms, Map<Integer, Booking> bookings, LocalDate date) {
        if (rooms == null || rooms.isEmpty()) {
            return 0;
        }
        return rooms.values().stream().filter(room -> !isAvailable(room, bookings, date)).count();
    }

    @Override
    public List<String> getMostPopularRoomTypesForAgeRange(Map<Integer, Booking> bookings, int minAge, int maxAge) {
        validateAgeRange(minAge, maxAge);
        if (bookings == null || bookings.isEmpty()) {
            return List.of();
        }

        Map<String, Long> counts = bookings.values().stream()
                .filter(booking -> isGuestAgeInRangeAtCheckIn(booking, minAge, maxAge))
                .collect(Collectors.groupingBy(
                        booking -> booking.getRoom().getType().getName(),
                        Collectors.counting()
                ));

        long max = counts.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        return counts.entrySet().stream()
                .filter(entry -> entry.getValue() == max)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    private boolean isAvailable(Room room, Map<Integer, Booking> bookings, LocalDate date) {
        if (bookings == null || bookings.isEmpty()) {
            return true;
        }
        return bookings.values().stream()
                .noneMatch(booking -> booking.getRoom().equals(room) && booking.isActiveOn(date));
    }

    private boolean isGuestAgeInRangeAtCheckIn(Booking booking, int minAge, int maxAge) {
        Integer age = booking.getGuest().getAgeOn(booking.getCheckIn());
        return age != null && age >= minAge && age <= maxAge;
    }

    private void validateAgeRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0) {
            throw new IllegalArgumentException("Age range must not be negative");
        }
        if (minAge > maxAge) {
            throw new IllegalArgumentException("Min age must not be greater than max age");
        }
    }
    
   
}
