package hotel.items;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import cli.Inputoutput;
import cli.Item;
import hotel.HotelApplContext;
import hotel.interfaces.*;
import hotel.models.*;

public abstract class HotelItem implements Item {
	protected final HotelApplContext context;
	protected final Inputoutput inOut;
	protected final IHotelService hotelService;
	protected final IHotelAnalyticsService analyticsService;
	protected final String dateFormat;

	protected HotelItem(HotelApplContext context) {
		super();
		this.context = context;
		this.inOut = context.getInOut();
		this.hotelService = context.getHotelService();
		this.analyticsService = context.getAnalyticsService();
		this.dateFormat = context.getDateFormat();
	}

	protected Guest getExistingGuest() {
		Integer guestId = inOut.inputInteger("Enter guest id");
		if (guestId == null)
			return null;
		Guest guest = hotelService.findGuestById(guestId);
		if (guest == null)
			inOut.outputlLine("Guest not found: " + guestId);
		return guest;
	}

	protected Room getExistingRoom() {
		Integer roomNumber = inOut.inputInteger("Enter room number");
		if (roomNumber == null)
			return null;
		Room room = hotelService.findRoomByNumber(roomNumber);
		if (room == null)
			inOut.outputlLine("Room not found: " + roomNumber);
		return room;
	}

	protected RoomType getExistingRoomType() {
		String roomTypeName = inOut.inputString("Enter room type name");
		if (roomTypeName == null)
			return null;
		RoomType roomType = hotelService.findRoomTypeByName(roomTypeName);
		if (roomType == null)
			inOut.outputlLine("Room type not found " + roomTypeName);
		return roomType;
	}

	protected LocalDate[] inputCheckInCheckOut() {
		LocalDate checkIn = inOut.inputDate("Enter check in date in format", dateFormat);
		if (checkIn == null)
			return null;
		LocalDate checkOut = inOut.inputDate("Enter check out date in format", dateFormat);
		if (checkOut == null)
			return null;
		if (!checkOut.isAfter(checkIn)) {
			inOut.outputlLine("Check out must be after check in");
			return null;
		}
		return new LocalDate[] { checkIn, checkOut };
	}

	protected Guest inputNewGuest() {
		Integer guestId = inOut.inputInteger("Enter guest id");
		if (guestId == null)
			return null;

		String name = inOut.inputString("Enter guest name");
		if (name == null)
			return null;
		String email = inOut.inputString("Enter guest email");
		if (email == null)
			return null;

		String password = inOut.inputString("Enter guest password");
		if (password == null)
			return null;
		LocalDate birthDate = inOut.inputDate("Enter birth date in format", dateFormat);
		if (birthDate == null)
			return null;

		return new Guest(guestId, name, email, password, birthDate);
	}

	protected void showBookings(List<Booking> bookings, String newMessage) {
		if (bookings == null || bookings.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		bookings.stream().sorted(Comparator.comparing(Booking::getBookingId))
		.forEach(inOut::outputlLine);
	}

	protected void showRooms(List<Room> rooms, String newMessage) {
		if (rooms == null || rooms.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		rooms.stream().sorted(Comparator.comparing(Room::getRoomNumber))
		.forEach(inOut::outputlLine);
	}
}
