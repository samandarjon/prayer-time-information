package uz.pdp.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.pdp.database.DataBaseConnection;
import uz.pdp.model.Country;

import java.util.ArrayList;
import java.util.List;

public class SetKeyboard {
    public static InlineKeyboardMarkup setInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<Country> countries = DataBaseConnection.getCountries();
        int column = 0;
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (Country country : countries) {
            if (column == 2) {
                rowsInline.add(rowInline);
                column = 0;
                rowInline = new ArrayList<>();
            }
            column++;
            rowInline.add(new InlineKeyboardButton().setText(country.getName()).setCallbackData(country.getId()));
        }
        if (rowInline.size() < 2) {
            rowsInline.add(rowInline);
        }

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
