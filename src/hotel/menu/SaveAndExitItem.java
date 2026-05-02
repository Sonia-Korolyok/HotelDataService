package hotel.menu;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

import java.io.IOException;

public class SaveAndExitItem extends HotelItem {
    public SaveAndExitItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Save and exit";
    }

    @Override
    public void perform() {
        try {
            context.saveAll();
            inOut.outputlLine("data saved successfully");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save data: " + e.getMessage());
        }
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
