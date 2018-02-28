package br.com.everton.bateconta.br.com.everton.bateconta.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by r on 18/01/2018.
 */

public class Util {

    public Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                //.addInterceptor(loggingInterceptor)
                //.addNetworkInterceptor(networkInterceptor)
                .build();

        return new Retrofit.Builder()
                //.baseUrl("http://10.0.2.2:8080") //localhost
                //TODO ALTERAR PARA O LINK DO HIROKU
                .baseUrl("https://batecontacloud.herokuapp.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
