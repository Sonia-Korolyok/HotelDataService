package hotel.items.manager;

import hotel.HotelApplContext;

import hotel.items.HotelItem;
import hotel.models.Room;
import hotel.models.RoomType;

public class AddRoomItem extends HotelItem {
    public AddRoomItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Add room";
    }

    @Override
    public void perform() {
        Integer roomNumber = inOut.inputInteger("Enter room number");
        if (roomNumber == null) {
            return;
        }
        RoomType roomType = getExistingRoomType();
        if (roomType == null) {
            return;
        }
        Room room = new Room(roomNumber, roomType);
        hotelService.addRoom(room);
        inOut.outputlLine("Room added: " + room);
    }
}
