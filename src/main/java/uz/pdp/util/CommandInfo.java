package uz.pdp.util;

import uz.pdp.database.DataBaseConnection;
import uz.pdp.model.Profile;

import java.sql.Time;

public class CommandInfo {
    public static String help() {
        return "\uD83D\uDD38\uD83D\uDD37\uD83D\uDD38 <u>Botning imkoniyatlari.</u> \uD83D\uDD38\uD83D\uDD37\uD83D\uDD38\n\n" +
                "\uD83D\uDD38 <b>/update</b> - <i>joylashunni qaytadan tanlash buyrug`i</i>\n" +
                "\uD83D\uDD39 <b>/tong</b> - <i> tong vaqtini bilish buyrug`i </i>\n" +
                "\uD83D\uDD38<b>/quyosh</b> - <i> quyosh chiqish vaqtini bilish</i>\n" +
                "\uD83D\uDD39 <b>/peshin</b> - <i> peshin vaqtini bilish buyrug`i</i>\n" +
                "\uD83D\uDD38 <b>/asr</b> - <i>asr vaqtini bilish buyrug`i</i>\n" +
                "\uD83D\uDD39 <b>/shom</b> - <i> shom vaqtini bilish buyrug`i</i>\n" +
                "\uD83D\uDD38 <b>/xufton</b> - <i> xufton vaqtini bilish buyrug`i</i>\n";
    }

    public static String event(String event, Profile profile) {
        Time time = DataBaseConnection.getTimeTong(event.replace("/", ""));
        assert time != null;
        time = new Time(time.getTime() + 1000 * 60 * profile.getCountry().getDifference());
        return "⚪️ Joylashuvingiz bo`yicha " + event.replace("/", "") + " vaqti: \n" +
                "⏰  <b>" + time + "</b>";
    }
}
