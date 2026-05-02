package hotel.generator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hotel.interfaces.IRandomHotelDataGenerator;
import hotel.models.Booking;
import hotel.models.Guest;
import hotel.models.Room;
import hotel.models.RoomType;

public class RandomHotelDataGenerator implements IRandomHotelDataGenerator {
    private static final Random RND = new Random();
    private static final int NUMBER_OF_ROOMS = 20;
    private static final int NUMBER_OF_GUESTS = 10;
    private static final int NUMBER_OF_BOOKINGS = 10;

    @Override
    public Map<String, RoomType> getRoomTypes() {
        Map<String, RoomType> map = new LinkedHashMap<>();
        putRoomType(map, new RoomType("Standard", 120.0, 2));
        putRoomType(map, new RoomType("Deluxe", 180.0, 3));
        putRoomType(map, new RoomType("Suite", 300.0, 4));
        return map;
    }

    @Override
    public Map<Integer, Room> getRooms(Map<String, RoomType> types) {
        Map<Integer, Room> map = new LinkedHashMap<>();
        List<RoomType> typeList = new ArrayList<>(types.values());
        for (int i = 0; i < NUMBER_OF_ROOMS; i++) {
            int roomNumber = 101 + i;
            RoomType type = typeList.get(RND.nextInt(typeList.size()));
            map.put(roomNumber, new Room(roomNumber, type));
        }
        return map;
    }

    @Override
    public Map<Integer, Guest> getGuests() {
        Map<Integer, Guest> map = new LinkedHashMap<>();
        for (int i = 0; i < NUMBER_OF_GUESTS; i++) {
            map.put(i, new Guest(i, "Guest" + i, "guest_" + i + "@gmail.com", "pass_" + i, generateBirthDate()));
        }
        return map;
    }

    @Override
    public Map<Integer, Booking> getBookings(Map<Integer, Guest> guests, Map<Integer, Room> rooms) {
        Map<Integer, Booking> map = new LinkedHashMap<>();
        List<Guest> guestList = new ArrayList<>(guests.values());
        List<Room> roomList = new ArrayList<>(rooms.values());
        int attempts = 0;
        int maxAttempts = 1000;
        while (map.size() < NUMBER_OF_BOOKINGS && attempts < maxAttempts) {
            attempts++;
            Guest guest = guestList.get(RND.nextInt(guestList.size()));
            Room room = roomList.get(RND.nextInt(roomList.size()));
            LocalDate checkIn = LocalDate.now().plusDays(RND.nextInt(100));
            LocalDate checkOut = checkIn.plusDays(1 + RND.nextInt(7));
            if (isRoomAvailable(map, room, checkIn, checkOut)) {
                Booking booking = new Booking(guest, room, checkIn, checkOut);
                map.put(booking.getBookingId(), booking);
            }
        }
        return map;
    }

    private LocalDate generateBirthDate() {
        int age = 18 + RND.nextInt(70);
        int randomDays = RND.nextInt(365);
        return LocalDate.now().minusYears(age).minusDays(randomDays);
    }

    private boolean isRoomAvailable(Map<Integer, Booking> bookings, Room room, LocalDate checkIn, LocalDate checkOut) {
        return bookings.values().stream()
                .filter(booking -> booking.getRoom().equals(room))
                .noneMatch(booking -> booking.overlaps(checkIn, checkOut));
    }

    private void putRoomType(Map<String, RoomType> map, RoomType roomType) {
        map.put(roomType.getName().trim().toLowerCase(), roomType);
    }
}
