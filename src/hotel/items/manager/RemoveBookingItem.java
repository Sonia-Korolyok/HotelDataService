package hotel.items.manager;

import hotel.HotelApplContext;

import hotel.items.HotelItem;

public class RemoveBookingItem extends HotelItem {
    public RemoveBookingItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Remove booking";
    }

    @Override
    public void perform() {
        Integer bookingId = inOut.inputInteger("Enter booking id to remove");
        if (bookingId == null) {
            return;
        }
        boolean removed = hotelService.cancelBooking(bookingId);
        inOut.outputlLine(removed ? "Booking removed" : "Booking not found");
    }
}
