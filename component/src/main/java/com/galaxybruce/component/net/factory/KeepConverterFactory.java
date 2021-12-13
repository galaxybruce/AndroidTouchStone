package com.galaxybruce.component.net.factory;


import com.galaxybruce.component.net.AppKeepRespModel;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author bruce.zhang
 * @date 2020/11/4 10:15
 * @description (亲 ， 我是做什么的)
 * <p>
 * modification history:
 */
public class KeepConverterFactory extends Converter.Factory {
    public static KeepConverterFactory create() {
        return new KeepConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof Class<?>
                && type == AppKeepRespModel.class) {
            return new KeepResponseBodyConverter();
        } else {
            return null;
        }
    }

    private static class KeepResponseBodyConverter implements Converter<ResponseBody, AppKeepRespModel> {

        @Override
        public AppKeepRespModel convert(@Nullable ResponseBody value) throws IOException {
            AppKeepRespModel respModel = new AppKeepRespModel();
            respModel.setBody(value);
            return respModel;
        }
    }
}
