package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class RemoveRoomItem extends HotelItem {
    public RemoveRoomItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Remove room";
    }

    @Override
    public void perform() {
        Integer roomNumber = inOut.inputInteger("Enter room number to remove");
        if (roomNumber == null) {
            return;
        }
        boolean removed = hotelService.removeRoom(roomNumber);
        inOut.outputlLine(removed ? "Room removed" : "Room not found");
    }
}
