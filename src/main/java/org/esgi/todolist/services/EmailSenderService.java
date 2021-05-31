package org.esgi.todolist.services;

import org.esgi.todolist.commons.exceptions.EmailException;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    public void sendWarningMessage(String emailUser) {
        throw new EmailException("Not implemented yet.\nEmail sending to " + emailUser);
    }
}
