import cli.*;
import hotel.HotelApplContext;
import hotel.generator.InitialData;
import hotel.generator.RandomHotelDataGenerator;
import hotel.interfaces.*;
import hotel.items.accountant.ShowAllStatisticsItem;
import hotel.items.accountant.ShowBookingsByCheckInItem;
import hotel.items.accountant.ShowMostPopularRoomForRangeAgeItem;
import hotel.items.accountant.ShowOccupancyForDateItem;
import hotel.items.guest.*;
import hotel.items.manager.*;
import hotel.menu.BackItem;
import hotel.menu.SaveAndExitItem;
import hotel.models.Booking;
import hotel.models.Guest;
import hotel.models.Room;
import hotel.models.RoomType;
import hotel.services.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HotelApplCLI {
    private static final String ROOM_TYPES_FILE = "roomtypes.data";
    private static final String ROOMS_FILE = "rooms.data";
    private static final String BOOKINGS_FILE = "bookings.data";
    private static final String GUESTS_FILE = "guests.data";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static void main(String[] args) {
        try {
            IRandomHotelDataGenerator generator = new RandomHotelDataGenerator();
            IRoomTypeFileService roomTypeFileService = new DataStreamService();
            IRoomFileService roomFileService = new RoomFileService();
            IBookingFileService bookingFileService = new BookingFileService();
            IHotelAnalyticsService analyticsService = new HotelAnalyticsService();
            IGuestFileService guestFileService = new GuestFileService();
            IHotelService hotelService = new HotelService();

            InitialData data = loadOrGenerate(
                    generator,
                    roomTypeFileService,
                    roomFileService,
                    bookingFileService,
                    guestFileService
            );

            data.roomTypes().values().forEach(hotelService::addRoomType);
            data.rooms().values().forEach(hotelService::addRoom);
            data.guests().values().forEach(hotelService::addGuest);
            data.bookings().values().forEach(hotelService::addBooking);
            hotelService.rebuildBookingsByCheckInDate();


            Inputoutput inOut = new ConsoleInputOutput();

            HotelApplContext context = new HotelApplContext(inOut, hotelService, analyticsService, bookingFileService,
                    roomFileService, roomTypeFileService, guestFileService,
                    ROOM_TYPES_FILE, ROOMS_FILE, BOOKINGS_FILE, GUESTS_FILE, DATE_FORMAT);

            Menu menu = new Menu(getMainMenuItems(context), inOut);
            menu.runMenu();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static InitialData loadOrGenerate(
            IRandomHotelDataGenerator generator,
            IRoomTypeFileService roomTypeFileService,
            IRoomFileService roomFileService,
            IBookingFileService bookingFileService,
            IGuestFileService guestFileService
    ) throws IOException, ClassNotFoundException {

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

        return new InitialData(roomTypes, rooms, guests, bookings);
    }


    private static Item[] getMainMenuItems(HotelApplContext context) {
        return new Item[]{
                new SubmenuItem("Guest", context.getInOut(), getGuestItems(context)),
                new SubmenuItem("Manager", context.getInOut(), getManagerItems(context)),
                new SubmenuItem("Accountant", context.getInOut(), getAccountantItems(context)),
                new SaveAndExitItem(context)
        };
    }

    private static Item[] getAccountantItems(HotelApplContext context) {
        return new Item[]{
                new ShowAllStatisticsItem(context),
                new ShowBookingsByCheckInItem(context),
                new ShowMostPopularRoomForRangeAgeItem(context),
                new ShowOccupancyForDateItem(context),
                new BackItem()
        };
    }

    private static Item[] getManagerItems(HotelApplContext context) {
        return new Item[]{
                new AddGuestItem(context),
                new AddRoomItem(context),
                new AddRoomTypeItem(context),
                new RemoveBookingItem(context),
                new RemoveGuestItem(context),
                new RemoveRoomTypeItem(context),
                new RemoveRoomItem(context),
                new ShowBookingsItem(context),
                new ShowGuestsItem(context),
                new ShowRoomsItem(context),
                new ShowRoomTypesItem(context),
                new BackItem()
        };
    }

    private static Item[] getGuestItems(HotelApplContext context) {
        return new Item[]{
                new RegisterGuestItem(context),
                new ShowAvailableRoomsItem(context),
                new CreateBookingItem(context),
                new CancelMyBookingItem(context),
                new ShowMyBookingsItem(context),
                new BackItem()
        };
    }

    private static boolean fileExistsAndNotEmpty(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.length() > 0;
    }
}
