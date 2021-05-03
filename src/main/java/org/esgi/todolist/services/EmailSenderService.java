package org.esgi.todolist.services;

import org.springframework.stereotype.Service;

@Service
public class EmailSenderService implements MessageSenderService {

    public void sendWarningMessage(String emailUser) {
        System.out.println("Email sending to " + emailUser);
    }
}
