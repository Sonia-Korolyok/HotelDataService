package hotel.items.guest;

import java.util.List;

import hotel.HotelApplContext;

import hotel.items.HotelItem;
import hotel.models.Booking;
import hotel.models.Guest;

public class CancelMyBookingItem extends HotelItem {
    public CancelMyBookingItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Cancel my booking";
    }

    @Override
    public void perform() {
        Guest guest = getExistingGuest();
        if (guest == null) {
            return;
        }

        List<Booking> bookings = hotelService.getBookingsByGuestsId(guest.getId());
        if (bookings.isEmpty()) {
            inOut.outputlLine("No bookings found for guest id " + guest.getId());
            return;
        }

        inOut.outputlLine("Guest bookings:");
        showBookings(bookings, "No bookings found for guest id " + guest.getId());

        Integer bookingId = inOut.inputInteger("Enter booking id to cancel");
        if (bookingId == null) {
            return;
        }

        Booking booking = hotelService.findBookingById(bookingId);
        if (booking == null || booking.getGuest().getId() != guest.getId()) {
            inOut.outputlLine("Booking does not belong to the selected guest");
            return;
        }

        boolean removed = hotelService.cancelBooking(bookingId);
        inOut.outputlLine(removed ? "Booking cancelled" : "Booking not found");
    }
}
