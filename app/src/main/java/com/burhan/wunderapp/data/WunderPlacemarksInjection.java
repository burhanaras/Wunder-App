package com.burhan.wunderapp.data;


import com.burhan.wunderapp.data.remote.WunderPlacemarksRestService;
import com.burhan.wunderapp.data.repository.WunderRepository;
import com.burhan.wunderapp.data.repository.WunderRepositoryImpl;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WunderPlacemarksInjection {

    private static final String BASE_URL = "https://s3-us-west-2.amazonaws.com";
    private static OkHttpClient okHttpClient;
    private static WunderPlacemarksRestService wunderRestService;
    private static Retrofit retrofitInstance;


    public static WunderRepository provideUserRepo() {
        return new WunderRepositoryImpl(provideWunderPlacemarksRestService());
    }

    static WunderPlacemarksRestService provideWunderPlacemarksRestService() {
        if (wunderRestService == null) {
            wunderRestService = getRetrofitInstance().create(WunderPlacemarksRestService.class);
        }
        return wunderRestService;
    }

    static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        }

        return okHttpClient;
    }

    static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            Retrofit.Builder retrofit = new Retrofit.Builder().client(WunderPlacemarksInjection.getOkHttpClient()).baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
            retrofitInstance = retrofit.build();

        }
        return retrofitInstance;
    }
}
