package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class RemoveRoomTypeItem extends HotelItem {
    public RemoveRoomTypeItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Remove room type";
    }

    @Override
    public void perform() {
        String name = inOut.inputString("Enter room type name to remove");
        if (name == null) {
            return;
        }
        boolean removed = hotelService.removeRoomType(name);
        inOut.outputlLine(removed ? "Room type removed" : "Room type not found");
    }
}
