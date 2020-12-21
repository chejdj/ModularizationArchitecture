package com.spinytech.macore.net;

/**
 * @author zhuyangyang on 12/21/20
 **/

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class StringConverterFactory extends Converter.Factory {

    public static final StringConverterFactory INSTANCE = new StringConverterFactory();

    public static StringConverterFactory create() {
        return INSTANCE;
    }

    @Override
    public Converter<ResponseBody, String> responseBodyConverter(Type type,
            Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, String>() {
            @Override
            public String convert(ResponseBody value) throws IOException {
                return value.string();
            }
        };
    }
}