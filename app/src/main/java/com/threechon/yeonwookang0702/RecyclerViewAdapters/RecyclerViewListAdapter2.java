package com.threechon.yeonwookang0702.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.threechon.yeonwookang0702.Classes.ImageTask;
import com.threechon.yeonwookang0702.RecyclerViewItems.ActivityItem;
import com.threechon.yeonwookang0702.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sadruddin on 12/24/2017.
 */

public class RecyclerViewListAdapter2 extends RecyclerView.Adapter<RecyclerViewListAdapter2.ActivityViewHolder>{
    private List<ActivityItem> horizontalActivityList;
    Context context;

    public RecyclerViewListAdapter2(List<ActivityItem> horizontalActivityList, Context context){
        this.horizontalActivityList= horizontalActivityList;
        this.context = context;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View activityView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler_item2, parent, false);
        ActivityViewHolder tvh = new ActivityViewHolder(activityView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, final int position) {
        /*ImageTask imageTask = new ImageTask();
        Bitmap bitmap = null;
        try {
            bitmap = (Bitmap) imageTask.execute(horizontalActivityList.get(position).getImageUrl()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(bitmap);*/
        Glide.with(context).load(horizontalActivityList.get(position).getImageUrl()).into(holder.imageView);
        holder.txtview.setText(horizontalActivityList.get(position).getActivityName());
        holder.txtview2.setText("참가비용: " + horizontalActivityList.get(position).getActivityPrice() + "원");
        holder.txtview3.setText("참가인원: " + horizontalActivityList.get(position).getActivityMin() + "~" + horizontalActivityList.get(position).getActivityMax() + "명");
        holder.txtview4.setText(horizontalActivityList.get(position).getActivityTag());

        // 아이템이 클릭 되었을 때 액션 처리할 리스너
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productIntro = horizontalActivityList.get(position).getActivityIntro();
                Toast.makeText(context, productIntro, Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalActivityList.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        TextView txtview2;
        TextView txtview3;
        TextView txtview4;

        public ActivityViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.activityImage);
            txtview=view.findViewById(R.id.activityName);
            txtview2=view.findViewById(R.id.activityPeriod);
            txtview3=view.findViewById(R.id.activityPeople);
            txtview4=view.findViewById(R.id.activityAge);
        }
    }
}