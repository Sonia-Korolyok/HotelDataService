package hotel;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

import hotel.generator.RandomHotelDataGenerator;
import hotel.interfaces.IBookingFileService;
import hotel.interfaces.IGuestFileService;
import hotel.interfaces.IHotelAnalyticsService;
import hotel.interfaces.IHotelService;
import hotel.interfaces.IRandomHotelDataGenerator;
import hotel.interfaces.IRoomFileService;
import hotel.interfaces.IRoomTypeFileService;
import hotel.models.Booking;
import hotel.models.Guest;
import hotel.models.Room;
import hotel.models.RoomType;
import hotel.services.BookingFileService;
import hotel.services.DataStreamService;
import hotel.services.GuestFileService;
import hotel.services.HotelAnalyticsService;
import hotel.services.HotelService;
import hotel.services.RoomFileService;

public class HotelAppl {
    private static final String ROOM_TYPES_FILE = "roomtypes.data";
    private static final String ROOMS_FILE = "rooms.data";
    private static final String BOOKINGS_FILE = "bookings.data";
    private static final String GUESTS_FILE = "guests.data";

    public static void main(String[] args) {
        try {
            IRandomHotelDataGenerator generator = new RandomHotelDataGenerator();
            IRoomTypeFileService roomTypeFileService = new DataStreamService();
            IRoomFileService roomFileService = new RoomFileService();
            IBookingFileService bookingFileService = new BookingFileService();
            IHotelAnalyticsService analyticsService = new HotelAnalyticsService();
            IGuestFileService guestFileService = new GuestFileService();
            IHotelService hotelService = new HotelService();

            Map<String, RoomType> roomTypes;
            Map<Integer, Room> rooms;
            Map<Integer, Guest> guests;
            Map<Integer, Booking> bookings;

            if (fileExistsAndNotEmpty(ROOM_TYPES_FILE)) {
                roomTypes = roomTypeFileService.loadRoomTypes(ROOM_TYPES_FILE);
            } else {
                roomTypes = generator.getRoomTypes();
                roomTypeFileService.saveRoomTypes(roomTypes, ROOM_TYPES_FILE);
            }

            if (fileExistsAndNotEmpty(ROOMS_FILE)) {
                rooms = roomFileService.loadRooms(ROOMS_FILE);
            } else {
                rooms = generator.getRooms(roomTypes);
                roomFileService.saveRooms(rooms, ROOMS_FILE);
            }

            if (fileExistsAndNotEmpty(GUESTS_FILE)) {
                guests = guestFileService.loadGuests(GUESTS_FILE);
            } else {
                guests = generator.getGuests();
                guestFileService.saveGuests(guests, GUESTS_FILE);
            }

            if (fileExistsAndNotEmpty(BOOKINGS_FILE)) {
                bookings = bookingFileService.loadBookings(BOOKINGS_FILE);
            } else {
                bookings = generator.getBookings(guests, rooms);
                bookingFileService.saveBookings(bookings, BOOKINGS_FILE);
            }

            roomTypes.values().forEach(hotelService::addRoomType);
            rooms.values().forEach(hotelService::addRoom);
            guests.values().forEach(hotelService::addGuest);
            bookings.values().forEach(hotelService::addBooking);
            hotelService.rebuildBookingsByCheckInDate();

            Map<String, RoomType> restoredRoomTypes = roomTypeFileService.loadRoomTypes(ROOM_TYPES_FILE);
            Map<Integer, Room> restoredRooms = roomFileService.loadRooms(ROOMS_FILE);
            Map<Integer, Guest> restoredGuests = guestFileService.loadGuests(GUESTS_FILE);
            Map<Integer, Booking> restoredBookings = bookingFileService.loadBookings(BOOKINGS_FILE);

            System.out.println("Room types restored");
            restoredRoomTypes.values().forEach(System.out::println);

            System.out.println("=".repeat(30));
            System.out.println("Rooms restored");
            restoredRooms.values().forEach(System.out::println);

            System.out.println("=".repeat(30));
            System.out.println("Guests restored");
            restoredGuests.values().forEach(System.out::println);

            System.out.println("=".repeat(30));
            System.out.println("Bookings in service");
            hotelService.getBookings().values().forEach(System.out::println);

            System.out.println("=".repeat(30));
            System.out.println("Bookings restored");
            restoredBookings.values().forEach(System.out::println);

            System.out.println("=".repeat(30));
            System.out.println("Average booking price: " + analyticsService.getAverageBookingPrice(restoredBookings));
            System.out.println("Most popular room types: " + analyticsService.getMostPopularRoomTypes(restoredBookings));
            System.out.println("Available rooms today: "
                    + analyticsService.getAvailableRoomsCount(restoredRooms, restoredBookings, LocalDate.now()));
            System.out.println("Occupied rooms today: "
                    + analyticsService.getOccupiedRoomsCount(restoredRooms, restoredBookings, LocalDate.now()));
            System.out.println("Occupied rooms on April 21: "
                    + analyticsService.getOccupiedRoomsCount(
                            restoredRooms,
                            restoredBookings,
                            LocalDate.of(2026, 4, 21)
                    ));
            System.out.println("Most popular room types for guests 18-35: "
                    + analyticsService.getMostPopularRoomTypesForAgeRange(restoredBookings, 18, 35));

            Booking firstBooking = hotelService.getBookings().values().stream().findFirst().orElse(null);
            if (firstBooking != null) {
                System.out.println("=".repeat(30));
                System.out.println("Cancel first booking: " + hotelService.cancelBooking(firstBooking.getBookingId()));
                System.out.println("Bookings count after cancellation: " + hotelService.getBookings().size());
            }

            Room removableRoom = hotelService.findRoomByNumber(999);
            if (removableRoom == null) {
                RoomType standard = hotelService.findRoomTypeByName("Standard");
                if (standard != null) {
                    Room extraRoom = new Room(999, standard);
                    hotelService.addRoom(extraRoom);
                    System.out.println("Added extra room 999 for removal demo");
                    System.out.println("Remove room 999: " + hotelService.removeRoom(999));
                }
            }

            RoomType removableType = new RoomType("Economy", 90.0, 1);
            hotelService.addRoomType(removableType);
            System.out.println("Remove room type Economy: " + hotelService.removeRoomType("Economy"));

            roomTypeFileService.saveRoomTypes(hotelService.getRoomTypes(), ROOM_TYPES_FILE);
            roomFileService.saveRooms(hotelService.getRooms(), ROOMS_FILE);
            guestFileService.saveGuests(hotelService.getGuests(), GUESTS_FILE);
            bookingFileService.saveBookings(hotelService.getBookings(), BOOKINGS_FILE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean fileExistsAndNotEmpty(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.length() > 0;
    }
}
