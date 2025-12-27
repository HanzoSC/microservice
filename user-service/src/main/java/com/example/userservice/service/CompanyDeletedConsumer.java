package com.example.userservice.service;

import com.example.userservice.dto.CompanyDeletedEvent;
import com.example.userservice.dto.CompanyHardDeleteEvent;
import com.example.userservice.entity.UserAccount;
import com.example.userservice.repository.UserAccountRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyDeletedConsumer {

    private final UserAccountRepository userRepository;
    private final KafkaTemplate<String, CompanyHardDeleteEvent> hardDeleteKafkaTemplate;
    private static final String COMPANY_HARD_DELETE_TOPIC = "company-hard-delete";

    public CompanyDeletedConsumer(UserAccountRepository userRepository,
                                  KafkaTemplate<String, CompanyHardDeleteEvent> hardDeleteKafkaTemplate) {
        this.userRepository = userRepository;
        this.hardDeleteKafkaTemplate = hardDeleteKafkaTemplate;
    }

    @KafkaListener(topics = "company-deleted", groupId = "user-service-group")
    @Transactional
    public void consumeCompanyDeleted(CompanyDeletedEvent event) {
        try {
            // Находим всех пользователей, работающих в данной компании
            List<UserAccount> users = userRepository.findByCompanyId(event.getCompanyId());
            
            // Сбрасываем company_id в null для всех найденных пользователей
            for (UserAccount user : users) {
                user.setCompanyId(null);
                userRepository.save(user);
            }
            
            // Отправляем сообщение обратно в company-service для физического удаления
            CompanyHardDeleteEvent hardDeleteEvent = new CompanyHardDeleteEvent(event.getCompanyId());
            hardDeleteKafkaTemplate.send(COMPANY_HARD_DELETE_TOPIC, hardDeleteEvent);
            
        } catch (Exception e) {
            // Игнорируем ошибки
        }
    }
}

