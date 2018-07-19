package com.threechon.yeonwookang0702.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.threechon.yeonwookang0702.RecyclerViewItems.ReviewItem;
import com.threechon.yeonwookang0702.R;
import java.util.List;

/**
 * Created by Sadruddin on 12/24/2017.
 */

public class RecyclerViewListAdapter3 extends RecyclerView.Adapter<RecyclerViewListAdapter3.ReviewViewHolder>{
    private List<ReviewItem> verticalReviewList;
    Context context;

    public RecyclerViewListAdapter3(List<ReviewItem> verticalReviewList, Context context){
        this.verticalReviewList= verticalReviewList;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View ReviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_recycler_item, parent, false);
        ReviewViewHolder rvh = new ReviewViewHolder(ReviewView);
        return rvh;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, final int position) {
        holder.txtview.setText(verticalReviewList.get(position).getReviewContent());
        holder.ratingBar.setRating((float)verticalReviewList.get(position).getReviewRating());
        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#F1C40F"), PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public int getItemCount() {
        return verticalReviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView txtview;

        public ReviewViewHolder(View view) {
            super(view);
            txtview=view.findViewById(R.id.reviewContent);
            ratingBar=view.findViewById(R.id.reviewRating);


        }
    }
}