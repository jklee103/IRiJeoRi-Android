package com.threechon.yeonwookang0702.RecyclerViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.threechon.yeonwookang0702.Classes.ImageTask;
import com.threechon.yeonwookang0702.DetailActivity;
import com.threechon.yeonwookang0702.Parser.NetworkTask;
import com.threechon.yeonwookang0702.R;
import com.threechon.yeonwookang0702.RecyclerViewItems.MyReviewItem;
import com.threechon.yeonwookang0702.ReviewDialog;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Sadruddin on 12/24/2017.
 */

// 리뷰 목록
public class RecyclerViewListAdapter5 extends RecyclerView.Adapter<RecyclerViewListAdapter5.MyReviewViewHolder>{
    private List<MyReviewItem> verticalSearchList;
    Context context;

    String content = null;
    Float rating = null;

    public RecyclerViewListAdapter5(List<MyReviewItem> verticalSearchList, Context context){
        this.verticalSearchList= verticalSearchList;
        this.context = context;
    }

    @Override
    public MyReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View myReviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_recycler_item3, parent, false);
        MyReviewViewHolder mrvh = new MyReviewViewHolder(myReviewView);
        return mrvh;
    }

    @Override
    public void onBindViewHolder(final MyReviewViewHolder holder, final int position) {
        //holder.imageview.setImageResource(verticalSearchList.get(position).getTownImage());
        /*ImageTask imageTask = new ImageTask();
        Bitmap bitmap = null;
        try {
            Log.d("마이리뷰이미지", verticalSearchList.get(position).getImageUrl());
            bitmap = (Bitmap) imageTask.execute(verticalSearchList.get(position).getImageUrl()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        holder.imageview.setImageBitmap(bitmap);*/

        Glide.with(context).load(verticalSearchList.get(position).getImageUrl()).into(holder.imageview);
        holder.txtview.setText(verticalSearchList.get(position).getVillageName());
        holder.ratingBar.setRating((float) verticalSearchList.get(position).getReviewRating());

        Drawable drawable = holder.ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#F1C40F"), PorterDuff.Mode.SRC_ATOP);

        holder.txtview2.setText(verticalSearchList.get(position).getReviewContent());
        holder.txtView3.setText(verticalSearchList.get(position).getDate());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 수정 하기

                final ReviewDialog inputDialog = new ReviewDialog((Activity) v.getContext());
                Bundle bundle = new Bundle();

                bundle.putString("content", verticalSearchList.get(position).getReviewContent());
                bundle.putFloat("rating", (float) verticalSearchList.get(position).getReviewRating());

                inputDialog.show();
                inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(inputDialog.getContent() != null) {
                            String content = inputDialog.getContent();
                            Log.d("콘텐츠", content);
                            Float rating = inputDialog.getRating();

                            // DB에 수정된 리뷰 넣기
                            //NetworkTask networkTask = new NetworkTask("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/reviews_edit.php?index="+verticalSearchList.get(position).getReview_index()+"&content="+content+"&rating="+rating);
                            Log.d("수정url", "http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/reviews_edit.php?index="+verticalSearchList.get(position).getReview_index()+"&content="+content+"&rating="+rating);

                            String url = "http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/reviews_edit.php?index="+verticalSearchList.get(position).getReview_index()+"&content="+content+"&rating="+rating;
                            NetworkTask networkTask = new NetworkTask(url);
                            networkTask.execute();

                            // 화면 갱신
                            MyReviewItem item = verticalSearchList.get(position);
                            item.setReviewContent(content);
                            item.setReviewRating(rating);
                            verticalSearchList.set(position, item);
                            notifyDataSetChanged();

                            Toast.makeText(context, "리뷰가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 삭제 하기
                int selected_item = verticalSearchList.get(position).getReview_index();
                NetworkTask networkTask = new NetworkTask("http://ec2-13-209-174-208.ap-northeast-2.compute.amazonaws.com/reviews_delete.php?index="+selected_item);
                networkTask.execute();

                verticalSearchList.remove(verticalSearchList.get(position));
                notifyDataSetChanged();

                Toast.makeText(context, "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 아이템이 클릭 되었을 때 액션 처리할 리스너
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 현재는 토스트 박스로 확인 하게 해둠 => 추후 디테일 액티비티로 이동하도록 수정해야함
                //Toast.makeText(context, "마이 리뷰 클릭 테스트", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name", verticalSearchList.get(position).getVillageName());
                intent.putExtra("intro", verticalSearchList.get(position).getVillageIntro());
                intent.putExtra("address", verticalSearchList.get(position).getVillageAddress());
                Log.d("마이리뷰주소(어댑터)", verticalSearchList.get(position).getVillageAddress());

                intent.putExtra("manager", verticalSearchList.get(position).getVillageManager());
                intent.putExtra("email", verticalSearchList.get(position).getVillageEmail());
                intent.putExtra("homepage", verticalSearchList.get(position).getVillageHomepage());
                intent.putExtra("id", verticalSearchList.get(position).getVillageId());
                intent.putExtra("image", verticalSearchList.get(position).getImageUrl());

                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return verticalSearchList.size();
    }

    public class MyReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView txtview;
        RatingBar ratingBar;
        TextView txtview2;
        Button button;
        Button button2;
        TextView txtView3;

        public MyReviewViewHolder(View view) {
            super(view);
            imageview=view.findViewById(R.id.review_item_image);
            txtview=view.findViewById(R.id.review_item_townName);
            txtview2=view.findViewById(R.id.review_text);
            ratingBar=view.findViewById(R.id.review_item_ratingbar);
            button=view.findViewById(R.id.review_item_edit_btn);
            button2=view.findViewById(R.id.review_item_delete_btn);
            txtView3=view.findViewById(R.id.review_date);
        }
    }
}