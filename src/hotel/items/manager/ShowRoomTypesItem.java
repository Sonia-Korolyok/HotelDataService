package hotel.items.manager;

import java.util.Comparator;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class ShowRoomTypesItem extends HotelItem {
    public ShowRoomTypesItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show room types";
    }

    @Override
    public void perform() {
        if (hotelService.getRoomTypes().isEmpty()) {
            inOut.outputlLine("No room types found");
            return;
        }
        hotelService.getRoomTypes().values().stream()
                .sorted(Comparator.comparing(type -> type.getName().toLowerCase()))
                .forEach(inOut::outputlLine);
    }
}
