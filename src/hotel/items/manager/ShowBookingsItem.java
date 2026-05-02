package hotel.items.manager;

import java.util.ArrayList;

import hotel.HotelApplContext;

import hotel.items.HotelItem;

public class ShowBookingsItem extends HotelItem {
    public ShowBookingsItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show bookings";
    }

    @Override
    public void perform() {
        showBookings(new ArrayList<>(hotelService.getBookings().values()), "No bookings found");
    }
}
