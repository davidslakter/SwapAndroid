package com.swap.retrofitApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface APIService {

    @FormUrlEncoded
    @POST()
    Call<ResponseBody> followInstagramUser(@Url String url, @Field("action") String follow);

    @PUT()
    Call<ResponseBody> followSoundCloudUser(@Url String url);

    @POST()
    Call<ResponseBody> vimeoUserLogin(@Url String url);

    @GET()
    Call<ResponseBody> getYoutubeChannels(@Url String url);

    @GET()
    Call<ResponseBody> getRedditUsersDetails(@Url String url);

    @FormUrlEncoded
    @POST()
    Call<ResponseBody> getGitHubDeviceToken(@Url String url,
                                            @Field("client_id") String client_id,
                                            @Field("client_secret") String client_secret,
                                            @Field("code") String code,
                                            @Field("redirect_uri") String redirect_uri,
                                            @Field("state") String state);

    @GET()
    Call<ResponseBody> getGitHubUsersDetails(@Url String url);

    @PUT()
    Call<ResponseBody> followGitHub(@Url String url);

    @POST()
    @FormUrlEncoded
    Call<ResponseBody> getSpotifyTokens(@Url String requestUrl, @Field("grant_type") String grantType, @Field("code") String code, @Field("redirect_uri") String redirectUri, @Field("client_id") String clientId, @Field("client_secret") String clientSecret);

    @POST()
    @FormUrlEncoded
    Call<ResponseBody> renewSpotifyAccessToken(@Url String requestUrl, @Field("grant_type") String grantType, @Field("refresh_token") String refreshToken, @Field("client_id") String clientId, @Field("client_secret") String clientSecret);

    @PUT()
    Call<ResponseBody> followVimeoUser(@Url String requestUrl);

}
