package org.esgi.todolist.services;

import org.assertj.core.api.Assertions;
import org.esgi.todolist.commons.exceptions.EmailException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EmailSenderServiceTest {

    private final EmailSenderService emailSenderService;

    @Autowired
    EmailSenderServiceTest(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Test
    void sendWarningMessage() {
        Assertions.assertThatThrownBy(() -> emailSenderService.sendWarningMessage("user@mail.example"))
                .isInstanceOf(EmailException.class)
                .hasMessage("Not implemented yet.\nEmail sending to user@mail.example");
    }
}