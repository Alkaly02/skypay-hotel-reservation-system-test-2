package dev.lka.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomSimpleDateFormat {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static String format(Date date){
        return sdf.format(date);
    }

    public static Date parse(String dateStr) throws ParseException {
        return sdf.parse(dateStr);
    }
}
