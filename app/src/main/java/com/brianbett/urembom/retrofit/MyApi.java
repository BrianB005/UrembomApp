package com.brianbett.urembom.retrofit;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApi {
    @GET("products")
    Call<List<SingleProduct>> getAllProducts();

    @GET("products/category")
    Call<List<SingleProduct>> getCategoryProducts(@Query("category")String category);
    @GET("products/search")
    Call<List<SingleProduct>> searchProducts(@Query("search_query")String searchQuery);

    @GET("products/find/{productId}")
    Call<ProductDetails> getSingleProduct(@Path("productId")String productId);

    @POST("auth/register")
    Call<User> registerUser(@Body HashMap<String,String> registerDetails);

    @POST("auth/login")
    Call<User> loginUser(@Body HashMap<String,String> loginDetails);
    @POST("auth/logout")
    Call<ResponseBody> logoutUser();

    @POST("reviews")
    Call<ResponseBody> createReview(@Header("Authorization")String token,@Body HashMap<String,String> reviewDetails);


}
