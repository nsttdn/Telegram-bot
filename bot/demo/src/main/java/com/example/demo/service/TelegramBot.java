package com.example.demo.service;

import com.example.demo.config.BotConfig;
import com.example.demo.models.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }


    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    public String getAdmin(){
        return config.getAdmin();
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/homework":
                    answerHomework(chatId);
                    readExcel();
                    break;
                case "/help":
                    answerHelp(chatId);
                    break;
                case "/test":
                    if (String.valueOf(update.getMessage().getChatId()).equals(config.getAdmin())){
                        sendMessage(chatId, "OK");
                    }
                    break;
                case "/deadline":
                    readExcel();
                    break;
            }
        }
    }
    private void adminAction(Long chatId) {
        try {
            DatabaseService db = new DatabaseService();
            db.addTask("Second task", "25.03.2023");
            String text = "you admin";
            sendMessage(chatId, text);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi," + name + ". Your homework and reminders will be here";
        log.info("Replied to user" + name);

        sendMessage(chatId, answer);
    }

    private void answerHomework(long chatId) {
        String answerHomework = "Your homework:\n";
        sendMessage(chatId, answerHomework);
    }

    private void answerHelp(long chatId) {
        String answerHelp = "This bot is designed to give you quick access to your homework\n" +
                "click /homework and you get your homework\n" +
                "click /deadline and you find out the deadline for this homework\n" +
                "click /reminder and you can set yourself homework reminders";
        sendMessage(chatId, answerHelp);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    public String readExcel() {
        String message = "";
        try {
            FileInputStream file = new FileInputStream(new File("\"D:\\telegramBot\\bot\\demo\\0FA35F10.xlsx\""));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    message += cell.toString() + "\t";
                }
                message += "\n";
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
