package hotel.interfaces;

import java.util.Map;

import hotel.models.Booking;
import hotel.models.Guest;
import hotel.models.Room;
import hotel.models.RoomType;

public interface IRandomHotelDataGenerator {
    Map<String, RoomType> getRoomTypes();
    Map<Integer, Room> getRooms(Map<String, RoomType> types);
    Map<Integer, Guest> getGuests();
    Map<Integer, Booking> getBookings(Map<Integer, Guest> guests, Map<Integer, Room> rooms);
}
