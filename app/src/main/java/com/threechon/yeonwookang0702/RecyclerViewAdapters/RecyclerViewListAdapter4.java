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
import com.threechon.yeonwookang0702.R;
import com.threechon.yeonwookang0702.RecyclerViewItems.SearchItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Sadruddin on 12/24/2017.
 */

public class RecyclerViewListAdapter4 extends RecyclerView.Adapter<RecyclerViewListAdapter4.SearchViewHolder>{
    private List<SearchItem> verticalSearchList;
    Context context;

    public RecyclerViewListAdapter4(List<SearchItem> verticalSearchList, Context context){
        this.verticalSearchList= verticalSearchList;
        this.context = context;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View SearchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_recycler_item2, parent, false);
        SearchViewHolder svh = new SearchViewHolder(SearchView);
        return svh;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {
        //holder.imageview.setImageResource(verticalSearchList.get(position).getTownImage());
        /*ImageTask imageTask = new ImageTask();
        Bitmap bitmap = null;
        try {
            bitmap = (Bitmap) imageTask.execute(verticalSearchList.get(position).getTownImage2()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        holder.imageview.setImageBitmap(bitmap);*/
        Glide.with(context).load(verticalSearchList.get(position).getTownImage2()).into(holder.imageview);
        holder.txtview.setText(verticalSearchList.get(position).getTownName());
        holder.txtview2.setText(verticalSearchList.get(position).getTownAddress());

        String string = verticalSearchList.get(position).getTownFunctions();
        holder.txtview3.setText(verticalSearchList.get(position).getTownFunctions());

        if(verticalSearchList.get(position).getTownTag().equals("웰빙여행")) {
            try {
                holder.txtview4.setBackgroundResource(R.drawable.rounded_button_sky);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(verticalSearchList.get(position).getTownTag().equals("자연여행")) {
            try {
                holder.txtview4.setBackgroundResource(R.drawable.rounded_button_mint);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(verticalSearchList.get(position).getTownTag().equals("전통문화")) {
            try {
                holder.txtview4.setBackgroundResource(R.drawable.rounded_button_yellow);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                holder.txtview4.setBackgroundResource(R.drawable.rounded_button_pink);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.txtview4.setText(verticalSearchList.get(position).getTownTag());

        // 아이템이 클릭 되었을 때 액션 처리할 리스너
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 현재는 토스트 박스로 확인 하게 해둠 => 추후 디테일 액티비티로 이동하도록 수정해야함
                //String productName = verticalSearchList.get(position).getTownName().toString();
                //Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();

                // 디테일 액티비티로 이동시 키값을 넘겨주도록 처리
                // 예) callDetailView(getTownKey().toString())의 형태
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name", verticalSearchList.get(position).getTownName());
                intent.putExtra("intro", verticalSearchList.get(position).getTownIntro());
                intent.putExtra("address", verticalSearchList.get(position).getTownAddress());
                intent.putExtra("manager", verticalSearchList.get(position).getTownManager());
                intent.putExtra("email", verticalSearchList.get(position).getTownEmail());
                intent.putExtra("homepage", verticalSearchList.get(position).getTownHomepage());
                intent.putExtra("id", verticalSearchList.get(position).getTownId());
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

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView txtview;
        TextView txtview2;
        TextView txtview3;
        TextView txtview4;

        public SearchViewHolder(View view) {
            super(view);
            imageview=view.findViewById(R.id.search_townImage);
            txtview=view.findViewById(R.id.search_townName);
            txtview2=view.findViewById(R.id.search_townAddress);
            txtview3=view.findViewById(R.id.search_function);
            txtview4=view.findViewById(R.id.search_townTag);
        }
    }

}