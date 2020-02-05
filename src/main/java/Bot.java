import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        // инициализируем аппер
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            //регистрируем чат бота
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    //Что бот будет отвечать на наше сообщение
    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage(); //инициализируем обьект
        sendMessage.enableMarkdown(true);//разметка

        sendMessage.setChatId(message.getChatId().toString()); //в какой конкретно чат мы должны отправить ответ
        sendMessage.setReplyToMessageId(message.getMessageId());// на какое конкретное сообщение мы должны ответить
        sendMessage.setText(text); //установим этот текст
        //установка отправки самого сообщения
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //метод для приема сообщений обновление
    public void onUpdateReceived(Update update) {
        Model model = new Model();//создаем модель
        Message message = update.getMessage();  //помещаем наше сообщение
        //если сообщение не нулевое и содержит текст
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                case "/setting":
                    sendMsg(message, "Что будем настраивать?");
                    break;
                case "Как дела?":
                    sendMsg(message,"Отлично, а ты зануда");
                    break;
                default://помещаем сообщение клиента и модель
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Город не найден!");
                    }


            }
        }
    }

    //клавиатура под текстовой панелью(отправляем сообщение)
    public void setButtons(SendMessage sendMessage) {
        //создаем клавиатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);//раметка клавиатуры
        replyKeyboardMarkup.setSelective(true);//выводит клавиатуру клиенту
        replyKeyboardMarkup.setResizeKeyboard(true);//автоматически подгонять клавиатуру
        replyKeyboardMarkup.setOneTimeKeyboard(false);//скрывать или не скрывать клавиатруру
        // создаем кнопки
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();//первая кнопка клавиатруры
        keyboardFirstRow.add(new KeyboardButton("/help"));//первая кнопка
        keyboardFirstRow.add(new KeyboardButton("/setting"));//вторая кнопка
        keyboardRowList.add(keyboardFirstRow);// добавляем все строчки клавиатуры в список
        replyKeyboardMarkup.setKeyboard(keyboardRowList);// и устанавливаем этот список на клавиатуре
    }

    //вернуть имя бота, указанного при регистрации
    public String getBotUsername() {
        return "MyTestPolinaBot";
    }

    //вернуть token, указанного при регистрации
    public String getBotToken() {
        return "816433761:AAHS_SIxUOVvSOmY-ovMMjRo8cNunwRi8ww";
    }
}
