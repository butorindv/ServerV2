package ru.butorin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class Parameters {
    @Value("${port}")
    private final int PORT_PARAM;
    private static final SimpleDateFormat FORMAT_FOR_DATE_NOW = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public Parameters() {
        this.PORT_PARAM = getPortParam();
    }

    public int getPortParam() {
        return PORT_PARAM;
    }

    public static SimpleDateFormat getFormatForDateNow() {
        return FORMAT_FOR_DATE_NOW;
    }
}
