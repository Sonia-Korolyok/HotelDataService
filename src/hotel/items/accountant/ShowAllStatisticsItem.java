package hotel.items.accountant;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

import java.time.LocalDate;

public class ShowAllStatisticsItem extends HotelItem {

    public ShowAllStatisticsItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show all statistics";
    }

    @Override
    public void perform() {
        LocalDate today = LocalDate.now();
        inOut.outputlLine("Room types count: " + hotelService.getRoomTypes().size());
        inOut.outputlLine("Rooms count: " + hotelService.getRooms().size());
        inOut.outputlLine("Guests count: " + hotelService.getGuests().size());
        inOut.outputlLine("Bookings count: " + hotelService.getBookings().size());
        inOut.outputlLine("Average booking price: " + analyticsService.getAverageBookingPrice(hotelService.getBookings()));
        inOut.outputlLine("Most Popular Room Types: " + analyticsService.getMostPopularRoomTypes(hotelService.getBookings()));
        inOut.outputlLine("Available rooms today: " + analyticsService.getAvailableRoomsCount(hotelService.getRooms(), hotelService.getBookings(), today));
        inOut.outputlLine("Occupied rooms today: " + analyticsService.getOccupiedRoomsCount(hotelService.getRooms(), hotelService.getBookings(), today));
    }
}
