package com.websocket.chat.service;

import com.websocket.chat.model.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveData(String data) {
        log.info("================= saveData() ===============");
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyyMMddHHmmss")
                .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                .toFormatter();
        Chat chat = new Chat();
        chat.setTimestamp(currentTime.format(formatter));
        chat.setData(data);

        mongoTemplate.insert(chat);

    }

    public List<Chat> getChatList() {
        log.info("================= getChatList() ===============");
        return mongoTemplate.findAll(Chat.class);
    }

    public List<Chat> getTodayChatList() {
        log.info("================= getTodayChatList() ===============");
        LocalDateTime currentday = LocalDateTime.now();
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyyMMdd")
                .toFormatter();

        Query query = new Query();
        query.addCriteria(Criteria.where("timestamp").regex("^"+currentday.format(formatter)));
        query.with(Sort.by(Sort.Direction.ASC, "timestamp"));

        return mongoTemplate.find(query, Chat.class);
    }

    public void deleteYesterDayData() {
        log.info("================= deleteYesterDayData() ===============");
        LocalDateTime currentday = LocalDateTime.now();
        LocalDateTime yesterday = currentday.minusDays(1);
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyyMMdd")
                .toFormatter();
        
        Query query = new Query();
        query.addCriteria(Criteria.where("timestamp").regex("^"+yesterday.format(formatter)));
        mongoTemplate.findAndRemove(query, Chat.class);
    }

}
