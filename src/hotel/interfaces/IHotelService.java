package hotel.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import hotel.models.*;



public interface IHotelService {
    void addRoomType(RoomType roomType);
    void addRoom(Room room);
    void addGuest(Guest guest);
    void addBooking(Booking booking);

    Booking createBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut);

    boolean cancelBooking(int bookingId);
    boolean removeRoom(int roomNumber);
    boolean removeRoomType(String roomTypeName);
    boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut);

    Room findRoomByNumber(int roomNumber);
    RoomType findRoomTypeByName(String roomTypeName);
    Guest findGuestById(int guestId);
    Booking findBookingById(int bookingId);

    void rebuildBookingsByCheckInDate();

    Map<String, RoomType> getRoomTypes();
    Map<Integer, Room> getRooms();
    Map<Integer, Guest> getGuests();
    Map<Integer, Booking> getBookings();
    Map<LocalDate, List<Booking>> getBookingsCheckInDate();

    List<Booking> getBookingsStartOn(LocalDate checkInDate);
    List<Booking> getBookingsByGuestsId(int guestId);
    
    //================================
    List<Room> getAvailableRooms(LocalDate checkIn,LocalDate checkOut);
    boolean removeGuest(int guestId);
}
