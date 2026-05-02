package hotel.items.guest;

import java.time.LocalDate;
import java.util.List;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.models.*;

public class CreateBookingItem extends HotelItem{

	public CreateBookingItem(HotelApplContext context) {
		super(context);
		
	}

	@Override
	public String displayName() {
		
		return "Create booking";
	}

	@Override
	public void perform() {
		Guest guest = getExistingGuest();
		if(guest==null)
			return;
		LocalDate[]dates = inputCheckInCheckOut();
		if(dates==null)
			return;
		List<Room> availableRooms = hotelService.getAvailableRooms(dates[0],dates[1]);
		if(availableRooms.isEmpty()) {
			inOut.outputlLine("No available rooms for selected dates");
			return;
		}
		inOut.outputlLine("Available rooms: ");
		showRooms(availableRooms, "No available rooms for selected dates");
		Room room = getExistingRoom();
		if(room ==null)
			return;
		boolean selRoomAv = availableRooms.stream().anyMatch(eR->eR.getRoomNumber()==room.getRoomNumber()); 
		if(!selRoomAv) {
			inOut.outputlLine("Selected room not available for selected dates");
			return;
		}
			
		Booking booking = hotelService.createBooking(guest, room, dates[0],dates[1]);
		inOut.outputlLine("Booking created "+ booking);
	}

}
