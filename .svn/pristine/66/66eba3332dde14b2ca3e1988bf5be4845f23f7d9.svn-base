package com.swap.Twitter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Admin on 19-09-2017.
 */

public interface CreateFriendship {
    @POST()
    Call<ResponseBody> followTwitterUser(@Url String url, @Query("follow") boolean follow, @Query("screen_name") String screenName, @Query("user_id") String userId);
}
