package hotel;

import java.io.IOException;

import cli.*;
import hotel.interfaces.*;

public class HotelApplContext {
	private final Inputoutput inOut;
	private final IHotelService hotelService;
	private final IHotelAnalyticsService analyticsService;
	private final IBookingFileService bookingFileService;
	private final IRoomFileService roomFileService;
	private final IRoomTypeFileService roomTypeFileService;
	private final IGuestFileService guestFileService;

	private final String roomTypesFile;
	private final String roomsFile;
	private final String bookingsFile;
	private final String guestsFile;

	private final String dateFormat;

	public HotelApplContext(Inputoutput inOut, IHotelService hotelService, IHotelAnalyticsService analyticsService,
			IBookingFileService bookingFileService, IRoomFileService roomFileService,
			IRoomTypeFileService roomTypeFileService, IGuestFileService guestFileService, String roomTypesFile,
			String roomsFile, String bookingsFile, String guestsFile, String dateFormat) {

		this.inOut = inOut;
		this.hotelService = hotelService;
		this.analyticsService = analyticsService;
		this.bookingFileService = bookingFileService;
		this.roomFileService = roomFileService;
		this.roomTypeFileService = roomTypeFileService;
		this.guestFileService = guestFileService;
		this.roomTypesFile = roomTypesFile;
		this.roomsFile = roomsFile;
		this.bookingsFile = bookingsFile;
		this.guestsFile = guestsFile;
		this.dateFormat = dateFormat;
	}

	public Inputoutput getInOut() {
		return inOut;
	}

	public IHotelService getHotelService() {
		return hotelService;
	}

	public IHotelAnalyticsService getAnalyticsService() {
		return analyticsService;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void saveAll() throws IOException {
		roomTypeFileService.saveRoomTypes(hotelService.getRoomTypes(), roomTypesFile);
		roomFileService.saveRooms(hotelService.getRooms(), roomsFile);
		guestFileService.saveGuests(hotelService.getGuests(), guestsFile);
		bookingFileService.saveBookings(hotelService.getBookings(), bookingsFile);
	}

}
