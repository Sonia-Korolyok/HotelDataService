package hotel.items.manager;

import java.util.Comparator;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class ShowGuestsItem extends HotelItem {
    public ShowGuestsItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show guests";
    }

    @Override
    public void perform() {
        if (hotelService.getGuests().isEmpty()) {
            inOut.outputlLine("No guests found");
            return;
        }
        hotelService.getGuests().values().stream()
                .sorted(Comparator.comparingInt(guest -> guest.getId()))
                .forEach(inOut::outputlLine);
    }
}
