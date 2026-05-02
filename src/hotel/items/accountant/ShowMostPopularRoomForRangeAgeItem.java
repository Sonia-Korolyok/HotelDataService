package hotel.items.accountant;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

import java.util.List;

public class ShowMostPopularRoomForRangeAgeItem extends HotelItem {
    public ShowMostPopularRoomForRangeAgeItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show most popular room for age range";
    }

    @Override
    public void perform() {
        Integer minAge = inOut.inputInteger("Enter minimum age");
        if (minAge == null)
            return;
        Integer maxAge = inOut.inputInteger("Enter maximum age");
        if (maxAge == null)
            return;
        List<String> rooms = analyticsService.getMostPopularRoomTypesForAgeRange(hotelService.getBookings(), minAge, maxAge);
        if (rooms == null || rooms.size() == 0) {
            inOut.outputlLine("No rooms found for selected age range");
            return;
        }
        inOut.outputlLine("Most popular room type for age range " + minAge + "-" + maxAge);
        for (String room : rooms)
            inOut.outputlLine(room);
    }
}
