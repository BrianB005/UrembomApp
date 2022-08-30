package com.brianbett.urembom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brianbett.urembom.retrofit.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.MyViewHolder> {

    ArrayList<Review> reviewsList;
    Context context;

    public ReviewsRecyclerViewAdapter(ArrayList<Review> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(context).inflate(R.layout.single_review,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.reviewerName.setText(reviewsList.get(position).getReviewerName());
        holder.comment.setText(reviewsList.get(position).getTitle());
        holder.reviewRating.setRating(Float.parseFloat(String.valueOf(reviewsList.get(position).getRating())));
        holder.reviewRating.setNumStars(Integer.parseInt(String.valueOf(reviewsList.get(position).getRating())));
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.opacity));
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView reviewerName,comment;
        RatingBar reviewRating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerName=itemView.findViewById(R.id.reviewer_name);
            comment=itemView.findViewById(R.id.review_comment);
            reviewRating=itemView.findViewById(R.id.review_rating);
        }
    }
}
