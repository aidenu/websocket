package com.websocket.chat.service;

import com.websocket.chat.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveData(String data) {

        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyyMMddHHmmss")
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .toFormatter();
        Chat chat = new Chat();
        chat.setTimestamp(localDate.format(formatter));
        chat.setData(data);

        mongoTemplate.insert(chat);

    }

    public List<Chat> getChatList() {
        return mongoTemplate.findAll(Chat.class);
    }

}
