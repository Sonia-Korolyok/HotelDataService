package hotel.items.manager;

import java.util.ArrayList;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class ShowRoomsItem extends HotelItem {
    public ShowRoomsItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show rooms";
    }

    @Override
    public void perform() {
        showRooms(new ArrayList<>(hotelService.getRooms().values()), "No rooms found");
    }
}
