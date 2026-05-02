package hotel.items.manager;


import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.models.RoomType;

public class AddRoomTypeItem extends HotelItem {
    public AddRoomTypeItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Add room type";
    }

    @Override
    public void perform() {
        String name = inOut.inputString("Enter room type name");
        if (name == null) {
            return;
        }
        Double pricePerNight = inOut.inputDouble("Enter price per night");
        if (pricePerNight == null) {
            return;
        }
        Integer capacity = inOut.inputInteger("Enter room capacity");
        if (capacity == null) {
            return;
        }

        RoomType roomType = new RoomType(name, pricePerNight, capacity);
        hotelService.addRoomType(roomType);
        inOut.outputlLine("Room type added: " + roomType);
    }
}
