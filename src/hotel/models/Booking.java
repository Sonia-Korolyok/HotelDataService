package hotel.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;
	private static int counter = 1000;

	private final int bookingId;
	private final Guest guest;
	private final Room room;
	private final LocalDate checkIn;
	private final LocalDate checkOut;

	public Booking(int bookingId, Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
		super();
		if (guest == null) {
			throw new IllegalArgumentException("Guest must not be null");
		}
		if (room == null) {
			throw new IllegalArgumentException("Room must not be null");
		}
		if (checkIn == null || checkOut == null) {
			throw new IllegalArgumentException("Check-in and check-out dates must not be null");
		}
		if (!checkOut.isAfter(checkIn)) {
			throw new IllegalArgumentException("Check-out must be after check-in");
		}
		if (bookingId <= counter)
			throw new IllegalArgumentException("Booking id not valid");
		this.bookingId = bookingId;
		syncCounter(bookingId);
		this.guest = guest;
		this.room = room;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
	}

	public static void syncCounter(int bookingId) {
		counter = Math.max(counter, bookingId);

	}

	public Booking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
		if (guest == null) {
			throw new IllegalArgumentException("Guest must not be null");
		}
		if (room == null) {
			throw new IllegalArgumentException("Room must not be null");
		}
		if (checkIn == null || checkOut == null) {
			throw new IllegalArgumentException("Check-in and check-out dates must not be null");
		}
		if (!checkOut.isAfter(checkIn)) {
			throw new IllegalArgumentException("Check-out must be after check-in");
		}
		this.bookingId = ++counter;
		this.guest = guest;
		this.room = room;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
	}

	public int getBookingId() {
		return bookingId;
	}

	public Guest getGuest() {
		return guest;
	}

	public Room getRoom() {
		return room;
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}

	public LocalDate getCheckOut() {
		return checkOut;
	}

	public long getNights() {
		return ChronoUnit.DAYS.between(checkIn, checkOut);
	}

	public double getTotalPrice() {
		return getNights() * room.getType().getPricePerNight();
	}

	public boolean overlaps(LocalDate requestedCheckIn, LocalDate requestedCheckOut) {
		return requestedCheckIn.isBefore(checkOut) && requestedCheckOut.isAfter(checkIn);
	}

	public boolean isActiveOn(LocalDate date) {
		return (date.isEqual(checkIn) || date.isAfter(checkIn)) && date.isBefore(checkOut);
	}

	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", guest=" + guest + ", room=" + room + ", checkIn=" + checkIn
				+ ", checkOut=" + checkOut + ", nights=" + getNights() + ", totalPrice=" + getTotalPrice() + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookingId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Booking other)) {
			return false;
		}
		return bookingId == other.bookingId;
	}
}
