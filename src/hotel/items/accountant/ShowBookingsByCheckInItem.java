package hotel.items.accountant;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.models.Booking;

import java.time.LocalDate;
import java.util.List;

public class ShowBookingsByCheckInItem extends HotelItem {
    public ShowBookingsByCheckInItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show bookings for selected day";
    }

    @Override
    public void perform() {
        LocalDate selected = inOut.inputDate("enter date in format", dateFormat);
        if (selected==null)
            return;
        List<Booking> bookings = hotelService.getBookingsStartOn(selected);
        showBookings(bookings, "No bookings found for " + selected);
    }
}
