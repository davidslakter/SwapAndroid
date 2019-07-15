package com.swap.retrofitApi;

import android.content.Context;
import com.swap.utilities.Constants;
import com.swap.utilities.Preferences;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by deepali on 06-04-2017.
 */

public class ApiUtils {

    private ApiUtils() {
    }

    public static APIService getAPIService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);
        return apiService;
    }

    private static OkHttpClient getObject(final Context context) {

        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request original = chain.request();

                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", "Bearer " + Preferences.get(context, Preferences.REDDIT_ACCESS_TOKEN))
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();
        return okClient;
    }


    //------------------------------------------------------------------------
    //Create Object of Reterofit
    public static APIService get(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getObject(context))
                .build();
        APIService apiService = retrofit.create(APIService.class);
        return apiService;
    }

    //Create Object of Reterofit
    public static APIService get(Context context, String headerName, String headerValue) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getObject(context, headerName, headerValue))
                .build();
        APIService apiService = retrofit.create(APIService.class);
        return apiService;
    }

    private static OkHttpClient getObject(final Context context, final String headerName, final String headerValue) {

        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request original = chain.request();

                                // Request customization: add request headers
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header(headerName, headerValue)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();
        return okClient;
    }
}