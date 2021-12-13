package com.galaxybruce.component.net.factory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bruce.zhang
 * @date 2020/11/4 10:21
 * @description (亲 ， 我是做什么的)
 * <p>
 * modification history:
 */
public class DateToLongDeserializer implements JsonDeserializer<Long> {

    private String[] datePatternArray = {"yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};

    @Override
    public Long deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {

        if (json == null) {
            return new Long(0);
        }

        String jsonAsString = json.getAsString();
        try {
            Long aLong1 = Long.valueOf(jsonAsString);
            return aLong1;
        }catch (NumberFormatException e){}

        for (int i = 0; i < datePatternArray.length; i++) {
            try {
                Long parse = formatDate(jsonAsString, datePatternArray[i]);
                if (parse != null) return parse;
            } catch (Exception e) {}
        }

        return Long.valueOf(0);
    }

    @Nullable
    private Long formatDate(String jsonAsString, String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(s);
        Date parse = format.parse(jsonAsString);
        if (parse != null) {
            return parse.getTime();
        }
        return null;
    }


}