package com.brianbett.urembom.retrofit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//https://urembom-backend.herokuapp.com/api/v1/products/search?search_query=${searchTerm}
//https://urembom-backend.herokuapp.com/api/v1/products/category?category=${category}
public class RetrofitHandler {
    private static final Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("https://urembom-backend.herokuapp.com/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    private static final MyApi myApi=retrofit.create(MyApi.class);

//    fetching all products
    public static void getAllProducts(Context context, ProductsInterface productsInterface){

        Call<List<SingleProduct>> productsCall=myApi.getAllProducts();
//        loader.showLoader();
        productsCall.enqueue(new Callback<List<SingleProduct>>() {
            @Override
            public void onResponse(@NonNull Call<List<SingleProduct>> call, @NonNull Response<List<SingleProduct>> response) {
//                loader.dismissLoader();
                if (!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();

                }else{
                    assert response.body()!=null;
                    productsInterface.success(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SingleProduct>> call,@NonNull Throwable t) {
                productsInterface.failure(t);
//                loader.dismissLoader();
            }
        });

    }
    public static void searchProducts(Context context,String searchQuery,ProductsInterface productsInterface){
        Call<List<SingleProduct>> searchProducts= myApi.searchProducts(searchQuery);

        searchProducts.enqueue(new Callback<List<SingleProduct>>() {
            @Override
            public void onResponse(@NonNull Call<List<SingleProduct>> call,@NonNull Response<List<SingleProduct>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }else {
                    productsInterface.success(response.body());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<SingleProduct>> call,@NonNull Throwable t) {
                productsInterface.failure(t);
            }
        });
    }
    public static void getSingleProduct(Context context,String productId,ProductInterface productInterface){
        Call<ProductDetails> productDetailsCall=myApi.getSingleProduct(productId);
        productDetailsCall.enqueue(new Callback<ProductDetails>() {
            @Override
            public void onResponse(@NonNull Call<ProductDetails> call,@NonNull Response<ProductDetails> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                }else{
                    productInterface.success(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductDetails> call,@NonNull Throwable t) {
                productInterface.failure(t);
            }
        });
    }

    public static void registerUser(Context context,HashMap<String,String> userDetails,UserInterface userInterface){
        Call<User> userCall= myApi.registerUser(userDetails);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,@NonNull Response<User> response) {
                if(!response.isSuccessful()){
                    try {
                        assert response.errorBody() != null;
                        JSONObject errorObject=new JSONObject(response.errorBody().string());
                        String errorMessage=errorObject.getString("msg");
                        Toast.makeText(context,errorMessage,Toast.LENGTH_LONG).show();

                        userInterface.errorExists(response.message());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    userInterface.success(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call,@NonNull Throwable t) {
                userInterface.failure(t);
            }
        });

    }

    public static void loginUser(Context context,HashMap<String,String> loginDetails,UserInterface userInterface){
        Call<User> userCall= myApi.loginUser(loginDetails);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call,@NonNull Response<User> response) {
                if(!response.isSuccessful()){
//
                    try {
                        assert response.errorBody() != null;
                        JSONObject errorObject=new JSONObject(response.errorBody().string());
                        String errorMessage=errorObject.getString("msg");
                        if(errorMessage.equals("User doesn't exist")){
                            Toast.makeText(context,"This email hasn't been registered!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context,errorMessage,Toast.LENGTH_LONG).show();
                        }

                        userInterface.errorExists(errorMessage);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    userInterface.success(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call,@NonNull Throwable t) {
                userInterface.failure(t);
            }
        });

    }

    public static void createReview(Context context, HashMap<String,String> reviewDetails, SuccessInterface reviewInterface){
        String token=Preferences.getItemFromSP(context,"token");
        Call<ResponseBody> reviewCall= myApi.createReview("Bearer "+token,reviewDetails);

        reviewCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,@NonNull Response<ResponseBody> response) {
                if(!response.isSuccessful()){

                    try {
                        JSONObject errorObject=new JSONObject(response.errorBody().string());
                        String errorMessage=errorObject.getString("msg");
                        reviewInterface.errorExists(errorMessage);

                    } catch (JSONException |IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    reviewInterface.success("Review submitted");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call,@NonNull Throwable t) {
                reviewInterface.failure(t);
            }
        });
    }

}
