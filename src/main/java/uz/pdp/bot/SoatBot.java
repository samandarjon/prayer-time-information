package uz.pdp.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.config.Keys;
import uz.pdp.database.DataBaseConnection;
import uz.pdp.model.Profile;
import uz.pdp.util.CommandInfo;
import uz.pdp.util.SetKeyboard;

public class SoatBot extends TelegramLongPollingBot {
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            Profile profile = DataBaseConnection.getProfile(message.getFrom().getId());
            if (message.getText().equals("/start") || message.getText().equals("/update")) {
                DataBaseConnection.add(message);
                sendMessage.setText("Assalomu alaykum. O`z jaylashuvingizni tanlang.");
                sendMessage.setReplyMarkup(SetKeyboard.setInlineKeyboard());
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.hasText()) {
                sendMessage.setParseMode(ParseMode.HTML);
                switch (message.getText()) {
                    case "/help": {
                        sendMessage.setText(CommandInfo.help());
                        execute(sendMessage);
                        return;
                    }
                    case "/tong":
                    case "/quyosh":
                    case "/peshin":
                    case "/asr":
                    case "/shom":
                    case "/xufton": {
                        assert profile != null;
                        sendMessage.setText(CommandInfo.event(message.getText(), profile));
                        execute(sendMessage);
                        return;
                    }
                    default:
                        sendMessage.setText("Bunday buyruq mavjud emas iltimos /help dan buyruqlarni tekshib ko`ring.");
                        execute(sendMessage);

                }
            }
        } else if (update.hasCallbackQuery()) {
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            DataBaseConnection.updateCountryOfProfile(update.getCallbackQuery());
            String answer = "Tanlovni o`zgartish uchun /update buyrug`ini berishingiz mumkin.   ";
            EditMessageText new_message = new EditMessageText()
                    .setChatId(chat_id)
                    .setMessageId(Math.toIntExact((message_id)))
                    .setText(answer);
            try {
                execute(new_message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return Keys.USERNAME;
    }

    @Override
    public String getBotToken() {
        return Keys.TOKEN;
    }
}
