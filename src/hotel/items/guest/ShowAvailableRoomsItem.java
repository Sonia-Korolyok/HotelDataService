package hotel.items.guest;

import java.time.LocalDate;
import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.models.Room;

public class ShowAvailableRoomsItem extends HotelItem {
    public ShowAvailableRoomsItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show available rooms for dates";
    }

    @Override
    public void perform() {
        LocalDate[] dates = inputCheckInCheckOut();
        if (dates == null) {
            return;
        }
        List<Room> availableRooms = hotelService.getAvailableRooms(dates[0], dates[1]);
        showRooms(availableRooms, "No available rooms for the selected dates");
    }
}
