package org.esgi.todolist.commons.date_serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        return LocalDateTime.parse(jsonParser.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
