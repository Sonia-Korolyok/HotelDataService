package hotel.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import hotel.interfaces.IHotelService;
import hotel.models.*;

public class HotelService implements IHotelService {
	private final Map<String, RoomType> roomTypes = new LinkedHashMap<>();// name(type) - key
	private final Map<Integer, Room> rooms = new LinkedHashMap<>();// id room - key
	private final Map<Integer, Guest> guests = new LinkedHashMap<>();// id guest - key
	private final Map<Integer, Booking> bookings = new LinkedHashMap<>();// id booking - key
	private final Map<LocalDate, List<Booking>> bookingsCheckInDate = new TreeMap<>();

	@Override
	public void addRoomType(RoomType roomType) {
		validateNotNull(roomType, "Room type must not be null");
		String key = normalizeRoomTypeName(roomType.getName());
		if (roomTypes.containsKey(key)) {
			throw new IllegalArgumentException("Room type already exists: " + roomType.getName());
		}
		roomTypes.put(key, roomType);
	}

	@Override
	public void addRoom(Room room) {
		validateNotNull(room, "Room must not be null");
		String roomTypeKey = normalizeRoomTypeName(room.getType().getName());
		if (!roomTypes.containsKey(roomTypeKey)) {
			throw new IllegalArgumentException(
					"Room type is not registered in hotel service: " + room.getType().getName());
		}
		if (rooms.containsKey(room.getRoomNumber())) {
			throw new IllegalArgumentException("Room already exists: " + room.getRoomNumber());
		}
		rooms.put(room.getRoomNumber(), room);
	}

	@Override
	public void addGuest(Guest guest) {
		validateNotNull(guest, "Guest must not be null");
		if (guests.containsKey(guest.getId())) {
			throw new IllegalArgumentException("Guest already exists: " + guest.getId());
		}
		guests.put(guest.getId(), guest);
	}

	@Override
	public void addBooking(Booking booking) {
		validateNotNull(booking, "Booking must not be null");
		if (!guests.containsKey(booking.getGuest().getId())) {
			throw new IllegalArgumentException(
					"Guest is not registered in hotel service: " + booking.getGuest().getId());
		}
		if (!rooms.containsKey(booking.getRoom().getRoomNumber())) {
			throw new IllegalArgumentException(
					"Room is not registered in hotel service: " + booking.getRoom().getRoomNumber());
		}
		if (bookings.containsKey(booking.getBookingId())) {
			throw new IllegalArgumentException("Booking already exists: " + booking.getBookingId());
		}
		if (!isRoomAvailableForBooking(booking.getRoom(), booking.getCheckIn(), booking.getCheckOut(),
				booking.getBookingId())) {
			throw new IllegalStateException(
					"Room " + booking.getRoom().getRoomNumber() + " is not available for the selected dates");
		}
		Booking.syncCounter(booking.getBookingId());
		bookings.put(booking.getBookingId(), booking);
		addBookingToDateIndex(booking);
	}

	@Override
	public Booking createBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
		validateNotNull(guest, "Guest must not be null");
		validateNotNull(room, "Room must not be null");
		validateNotNull(checkIn, "Check-in date must not be null");
		validateNotNull(checkOut, "Check-out date must not be null");

		if (!guests.containsKey(guest.getId())) {
			throw new IllegalArgumentException("Guest is not registered in hotel service: " + guest.getId());
		}
		if (!rooms.containsKey(room.getRoomNumber())) {
			throw new IllegalArgumentException("Room is not registered in hotel service: " + room.getRoomNumber());
		}
		if (!isRoomAvailable(room, checkIn, checkOut)) {
			throw new IllegalStateException(
					"Room " + room.getRoomNumber() + " is not available for the selected dates");
		}
		Booking booking = new Booking(guest, room, checkIn, checkOut);
		bookings.put(booking.getBookingId(), booking);
		addBookingToDateIndex(booking);
		return booking;
	}

	@Override
	public boolean cancelBooking(int bookingId) {
		Booking removedBooking = bookings.remove(bookingId);
		if (removedBooking == null) {
			return false;
		}
		removeBookingFromDateIndex(removedBooking);
		return true;
	}

	@Override
	public boolean removeRoom(int roomNumber) {
		Room room = rooms.get(roomNumber);
		if (room == null) {
			return false;
		}
		boolean hasRelatedBookings = bookings.values().stream().anyMatch(booking -> booking.getRoom().equals(room));
		if (hasRelatedBookings) {
			throw new IllegalStateException("Cannot remove room " + roomNumber + " because it has related bookings");
		}
		rooms.remove(roomNumber);
		return true;
	}

	@Override
	public boolean removeRoomType(String roomTypeName) {
		validateText(roomTypeName, "Room type name must not be blank");
		String key = normalizeRoomTypeName(roomTypeName);
		RoomType roomType = roomTypes.get(key);
		if (roomType == null) {
			return false;
		}
		boolean typeIsUsedByRooms = rooms.values().stream().map(Room::getType).anyMatch(type -> type.equals(roomType));
		if (typeIsUsedByRooms) {
			throw new IllegalStateException(
					"Cannot remove room type " + roomTypeName + " because it is assigned to rooms");
		}
		roomTypes.remove(key);
		return true;
	}

	@Override
	public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
		validateNotNull(room, "Room must not be null");
		validateNotNull(checkIn, "Check-in date must not be null");
		validateNotNull(checkOut, "Check-out date must not be null");
		if (!checkOut.isAfter(checkIn)) {
			throw new IllegalArgumentException("Check-out must be after check-in");
		}
		return isRoomAvailableForBooking(room, checkIn, checkOut, null);
	}

	@Override
	public Room findRoomByNumber(int roomNumber) {
		return rooms.get(roomNumber);
	}

	@Override
	public RoomType findRoomTypeByName(String roomTypeName) {
		if (roomTypeName == null) {
			return null;
		}
		return roomTypes.get(normalizeRoomTypeName(roomTypeName));
	}

	@Override
	public Guest findGuestById(int guestId) {
		return guests.get(guestId);
	}

	@Override
	public Booking findBookingById(int bookingId) {
		return bookings.get(bookingId);
	}

	@Override
	public void rebuildBookingsByCheckInDate() {
		bookingsCheckInDate.clear();
		bookings.values().forEach(this::addBookingToDateIndex);
	}

	@Override
	public Map<String, RoomType> getRoomTypes() {
		return Collections.unmodifiableMap(roomTypes);
	}

	@Override
	public Map<Integer, Room> getRooms() {
		return Collections.unmodifiableMap(rooms);
	}

	@Override
	public Map<Integer, Guest> getGuests() {
		return Collections.unmodifiableMap(guests);
	}

	@Override
	public Map<Integer, Booking> getBookings() {
		return Collections.unmodifiableMap(bookings);
	}

	@Override
	public Map<LocalDate, List<Booking>> getBookingsCheckInDate() {
		Map<LocalDate, List<Booking>> result = new TreeMap<>();
		bookingsCheckInDate.forEach(
				(date, bookingList) -> result.put(date, Collections.unmodifiableList(new ArrayList<>(bookingList))));
		return Collections.unmodifiableMap(result);
	}

	@Override
	public List<Booking> getBookingsStartOn(LocalDate checkInDate) {
		validateNotNull(checkInDate, "Check-in date must not be null");
//              return bookingsCheckInDate.getOrDefault(checkInDate, new ArrayList<Booking>());
		return new ArrayList<>(bookingsCheckInDate.getOrDefault(checkInDate, new ArrayList<>()));
	}

	@Override
	public List<Booking> getBookingsByGuestsId(int guestId) {
		return bookings.values().stream().filter(booking -> booking.getGuest().getId() == guestId).toList();
	}

	private boolean isRoomAvailableForBooking(Room room, LocalDate checkIn, LocalDate checkOut,
			Integer ignoredBookingId) {
		return bookings.values().stream().filter(booking -> booking.getRoom().equals(room))
				.filter(booking -> ignoredBookingId == null || booking.getBookingId() != ignoredBookingId)
				.noneMatch(booking -> booking.overlaps(checkIn, checkOut));
	}

	private void addBookingToDateIndex(Booking booking) {
		bookingsCheckInDate.computeIfAbsent(booking.getCheckIn(), key -> new ArrayList<>()).add(booking);
	}

	private void removeBookingFromDateIndex(Booking booking) {
		List<Booking> bookingList = bookingsCheckInDate.get(booking.getCheckIn());
		if (bookingList == null) {
			return;
		}
		bookingList.removeIf(existing -> existing.getBookingId() == booking.getBookingId());
		if (bookingList.isEmpty()) {
			bookingsCheckInDate.remove(booking.getCheckIn());
		}
	}

	private void validateNotNull(Object value, String message) {
		if (value == null) {
			throw new IllegalArgumentException(message);
		}
	}

	private void validateText(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
	}

	private String normalizeRoomTypeName(String roomTypeName) {
		return roomTypeName.trim().toLowerCase();
	}

	@Override
	public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
		validateNotNull(checkOut, "Check out date not be null");
		validateNotNull(checkIn, "Check out date not be null");
		if (!checkOut.isAfter(checkIn))
			throw new IllegalArgumentException("Check out must be after check in");
		return rooms.values().stream().filter(room -> isRoomAvailable(room, checkIn, checkOut)).toList();
	}
	@Override
    public boolean removeGuest(int guestId) {
        Guest guest = guests.get(guestId);
        if (guest == null) {
            return false;
        }
        boolean hasRelatedBookings = bookings.values().stream()
                .anyMatch(booking -> booking.getGuest().equals(guest));
        if (hasRelatedBookings) {
            throw new IllegalStateException("Cannot remove guest " + guestId + " because the guest has related bookings");
        }
        guests.remove(guestId);
        return true;
    }
}
