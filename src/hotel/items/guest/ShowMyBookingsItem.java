package hotel.items.guest;

import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.models.Booking;
import hotel.models.Guest;

public class ShowMyBookingsItem extends HotelItem {
    public ShowMyBookingsItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show my bookings";
    }

    @Override
    public void perform() {
        Guest guest = getExistingGuest();
        if (guest == null) {
            return;
        }
        List<Booking> bookings = hotelService.getBookingsByGuestsId(guest.getId());
        showBookings(bookings, "No bookings found for guest id " + guest.getId());
    }
}
