package hotel.items.manager;


import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class RemoveGuestItem extends HotelItem {
    public RemoveGuestItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Remove guest";
    }

    @Override
    public void perform() {
        Integer guestId = inOut.inputInteger("Enter guest id to remove");
        if (guestId == null) {
            return;
        }
        boolean removed = hotelService.removeGuest(guestId);
        inOut.outputlLine(removed ? "Guest removed" : "Guest not found");
    }
}
