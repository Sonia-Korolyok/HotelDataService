package hotel.items.accountant;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

import java.time.LocalDate;

public class ShowOccupancyForDateItem extends HotelItem {
    public ShowOccupancyForDateItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show Occupancy For Date";
    }

    @Override
    public void perform() {
        LocalDate selected = inOut.inputDate("enter date in format", dateFormat);
        if (selected==null)
            return;
        inOut.outputlLine("Occupied rooms for: " + selected + "->" + analyticsService.getOccupiedRoomsCount(hotelService.getRooms(), hotelService.getBookings(), selected));
    }
}
