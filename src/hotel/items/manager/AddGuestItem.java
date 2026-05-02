package hotel.items.manager;

import hotel.HotelApplContext;

import hotel.items.HotelItem;
import hotel.models.Guest;

public class AddGuestItem extends HotelItem {
    public AddGuestItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Add guest";
    }

    @Override
    public void perform() {
        Guest guest = inputNewGuest();
        if (guest == null) {
            return;
        }
        hotelService.addGuest(guest);
        inOut.outputlLine("Guest added: " + guest);
    }
}
