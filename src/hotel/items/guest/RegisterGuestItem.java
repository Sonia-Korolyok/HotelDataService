package hotel.items.guest;

import hotel.HotelApplContext;
import hotel.items.HotelItem;
import hotel.models.Guest;

public class RegisterGuestItem extends HotelItem {

	public RegisterGuestItem(HotelApplContext context) {
		super(context);

	}

	@Override
	public String displayName() {

		return "Register guest";
	}

	@Override
	public void perform() {
		Guest guest = inputNewGuest();
		if (guest == null)
			return;
		hotelService.addGuest(guest);
		inOut.outputlLine("Guest added " + guest);

	}

}
