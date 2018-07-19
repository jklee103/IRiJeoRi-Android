package com.threechon.yeonwookang0702.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.threechon.yeonwookang0702.Classes.ImageTask;
import com.threechon.yeonwookang0702.DetailActivity;

import com.threechon.yeonwookang0702.RecyclerViewItems.SearchItem;
import com.threechon.yeonwookang0702.R;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Sadruddin on 12/24/2017.
 */

public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.TownViewHolder>{
    private List<SearchItem> horizontalTownList;
    Context context;

    public RecyclerViewListAdapter(List<SearchItem> horizontalTownList, Context context){
        this.horizontalTownList= horizontalTownList;
        this.context = context;
    }

    @Override
    public TownViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View townView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler_item, parent, false);
        TownViewHolder tvh = new TownViewHolder(townView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TownViewHolder holder, final int position) {
        /*ImageTask imageTask = new ImageTask();
        Bitmap bitmap = null;
        try {
            bitmap = (Bitmap) imageTask.execute(horizontalTownList.get(position).getTownImage2()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //holder.imageView.setImageBitmap(bitmap);*/
        Glide.with(context).load(horizontalTownList.get(position).getTownImage2()).into(holder.imageView);
        holder.txtview.setText(horizontalTownList.get(position).getTownName());
        holder.txtview2.setText(horizontalTownList.get(position).getTownAddress());

        // 아이템이 클릭 되었을 때 액션 처리할 리스너
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 현재는 토스트 박스로 확인 하게 해둠 => 추후 디테일 액티비티로 이동하도록 수정해야함
                //String productName = horizontalTownList.get(position).getTownName().toString();
                //Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();

                // 디테일 액티비티로 이동시 키값을 넘겨주도록 처리
                // 예) callDetailView(getTownKey().toString())의 형태
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name", horizontalTownList.get(position).getTownName());
                intent.putExtra("intro", horizontalTownList.get(position).getTownIntro());
                intent.putExtra("address", horizontalTownList.get(position).getTownAddress());
                intent.putExtra("manager", horizontalTownList.get(position).getTownManager());
                intent.putExtra("email", horizontalTownList.get(position).getTownEmail());
                intent.putExtra("homepage", horizontalTownList.get(position).getTownHomepage());
                intent.putExtra("id", horizontalTownList.get(position).getTownId());
                intent.putExtra("image", horizontalTownList.get(position).getImageUrl());

                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalTownList.size();
    }

    public class TownViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        TextView txtview2;

        public TownViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.townImage);
            txtview=view.findViewById(R.id.townName);
            txtview2=view.findViewById(R.id.townAddress);
        }
    }
}