package com.brianbett.urembom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brianbett.urembom.retrofit.Preferences;
import com.brianbett.urembom.retrofit.ProductDetails;
import com.brianbett.urembom.retrofit.ProductInterface;
import com.brianbett.urembom.retrofit.RetrofitHandler;
import com.brianbett.urembom.retrofit.Review;
import com.brianbett.urembom.retrofit.SuccessInterface;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView companyName,productName,productDescription,initialPrice,productPrice;
    ImageView productImage;
    MaterialButton goToCart,addToCart;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent=getIntent();
        String productId=intent.getStringExtra("productId");

        ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#F037A5"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        RatingBar ratingBar=findViewById(R.id.rating_bar);
        RatingBar productRating=findViewById(R.id.product_rating);
        EditText commentEditText=findViewById(R.id.comment);
        TextView ratingComment=findViewById(R.id.rating_comment);
        MaterialButton submitReview=findViewById(R.id.submit_review);
        goToCart=findViewById(R.id.go_to_cart);
        addToCart=findViewById(R.id.add_to_cart);

        goToCart.setOnClickListener(view->{
            startActivity(new Intent(ProductDetailsActivity.this,CartActivity.class));
            finish();
        });



        Realm.init(getApplicationContext());
        RealmConfiguration configuration= new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        realm=Realm.getDefaultInstance();



        RecyclerView reviewsRecyclerView=findViewById(R.id.reviews_recycler_view);
        TextView numberOfReviews=findViewById(R.id.number_of_reviews);
        ArrayList<Review> reviewsList=new ArrayList<>();

        ReviewsRecyclerViewAdapter recyclerViewAdapter=new ReviewsRecyclerViewAdapter(reviewsList,getApplicationContext());
        reviewsRecyclerView.setAdapter(recyclerViewAdapter);

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        companyName=findViewById(R.id.company_name);
        productName=findViewById(R.id.product_name);
        productDescription=findViewById(R.id.description);
        initialPrice=findViewById(R.id.product_initial_price);
        productPrice=findViewById(R.id.product_price);
        productImage=findViewById(R.id.image);
        View loader=findViewById(R.id.loader);
        View loaderText=findViewById(R.id.loading_text);

        View productContainer=findViewById(R.id.product_container);
        loader.setVisibility(View.VISIBLE);
        productContainer.setVisibility(View.GONE);
        loaderText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.loader_text_animation));
        RetrofitHandler.getSingleProduct(getApplicationContext(), productId, new ProductInterface() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void success(ProductDetails productDetails) {
                loader.setVisibility(View.GONE);
                productContainer.setVisibility(View.VISIBLE);
                productContainer.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.opacity));

                companyName.setText(productDetails.getProduct().getCompany());
                productName.setText(productDetails.getProduct().getName());
                productDescription.setText(productDetails.getProduct().getDescription());
                initialPrice.setText(String.valueOf(productDetails.getProduct().getInitialPrice()));
                productPrice.setText(String.valueOf(productDetails.getProduct().getPrice()));
                if(Integer.parseInt(String.valueOf(productDetails.getProduct().getNumOfReviews()))==0){
                    reviewsRecyclerView.setVisibility(View.GONE);
                }else{
                    reviewsRecyclerView.setVisibility(View.VISIBLE);
                }
                numberOfReviews.setText("Number of reviews ( "+productDetails.getProduct().getNumOfReviews()+" )");
                if(Integer.parseInt(String.valueOf(productDetails.getProduct().getAverageRating()))==0){
                    productRating.setVisibility(View.GONE);
                }else{
                    productRating.setRating(Float.parseFloat(String.valueOf(productDetails.getProduct().getAverageRating())));
                    productRating.setNumStars(Integer.parseInt(String.valueOf(productDetails.getProduct().getAverageRating())));
                }
                reviewsList.addAll(productDetails.getProduct().getReviews());
                recyclerViewAdapter.notifyDataSetChanged();


                getSupportActionBar().setTitle(productDetails.getProduct().getName());
                Glide.with(getApplicationContext())
                        .load(Uri.parse(productDetails.getProduct().getImage()))
                        .into(productImage);
                addToCart.setOnClickListener(view2->{
                    RealmCartItem realmCartItem=new RealmCartItem(productDetails.getProduct().get_id(),productDetails.getProduct().getName(),Integer.parseInt(String.valueOf(productDetails.getProduct().getPrice())),productDetails.getProduct().getImage());
                    realm.executeTransaction(realm1->{
                        realm1.insertOrUpdate(realmCartItem);
                    });
                    Toast.makeText(getApplicationContext(),productDetails.getProduct().getName()+" added to cart",Toast.LENGTH_SHORT).show();
                });

            }

            @Override
            public void failure(Throwable throwable) {
                loader.setVisibility(View.GONE);
                productContainer.setVisibility(View.VISIBLE);
            }
        });



        int unfilledStarsColor=getResources().getColor(R.color.pink);

        LayerDrawable ratingStars=(LayerDrawable) ratingBar.getProgressDrawable();

        ratingStars.getDrawable(0).setTint(unfilledStarsColor);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingComment.setVisibility(View.VISIBLE);

                switch ((int) ratingBar.getRating()){
                    case 1:
                        ratingComment.setText("Too bad");
                        break;
                    case 2:
                        ratingComment.setText("Not good");
                        break;
                    case 3:
                        ratingComment.setText("Needs improvement");
                        break;
                    case 4:
                        ratingComment.setText("Great product!");
                        break;
                    case 5:
                        ratingComment.setText("Awesome product!I love it");
                        break;

                }
            }
        });

        submitReview.setOnClickListener((view -> {
            String comment=commentEditText.getText().toString();
            int rating =(int) ratingBar.getRating();
            HashMap<String,String> reviewDetails=new HashMap<>();

            String token= Preferences.getItemFromSP(getApplicationContext(),"token");

            if(token.isEmpty()){
                Toast.makeText(getApplicationContext(),"You must be logged in so as to review products!",Toast.LENGTH_LONG).show();
                return;
            }
            reviewDetails.put("product",productId);
            reviewDetails.put("rating",String.valueOf(rating));
            reviewDetails.put("title",comment);
            if(comment.equals("")){
                commentEditText.setError("You must provide a comment!");
            }else {

                submitReview.setText("Submitting...");
                submitReview.setEnabled(false);
                RetrofitHandler.createReview(getApplicationContext(), reviewDetails, new SuccessInterface() {
                    @Override
                    public void success(String message) {
                        ratingBar.setRating(0);
                        commentEditText.setText("");
                        submitReview.setText("Submit");
                        submitReview.setEnabled(true);
                    }
                    @Override
                    public void errorExists(String errorMessage) {
                        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
                        submitReview.setText("Submit");
                        submitReview.setEnabled(true);

                    }

                    @Override
                    public void failure(Throwable t) {
                        submitReview.setText("Submit");
                        submitReview.setEnabled(true);

                    }
                });

            }
            }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}