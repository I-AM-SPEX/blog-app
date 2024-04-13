package com.example.blog.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blog.R;
import com.example.blog.models.ContentModel;
import com.example.blog.pages.DetailPage;

import java.util.ArrayList;

public class Content_Adapter extends RecyclerView.Adapter<Content_Adapter.MyViewHolder> {
    // content adapter

    static Context context;

    ArrayList<ContentModel>contentModels;

    //private AdapterView.OnItemClickListener mListener;

    public Content_Adapter(Context context, ArrayList<ContentModel> contentModels ) {
        this.context = context;
        this.contentModels = contentModels;

    }

//    public void setOnItemClickListener(OnItemClickListener listener) {
//        mListener = listener;
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(ContentModel contentModel);
//    }



    @NonNull
    @Override
    public Content_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_view, parent, false);
        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Content_Adapter.MyViewHolder holder, int position) {
        holder.authorName.setText(contentModels.get(position).getAuthorName());
        holder.time.setText(contentModels.get(position).getTime());
        holder.title.setText(contentModels.get(position).getTitle());
        holder.date.setText(contentModels.get(position).getDate());
       Glide.with(context).load(contentModels.get(position).getImage()).into(holder.imageView);
      ContentModel contentModel = contentModels.get(position);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                Intent i = new Intent(context, DetailPage.class);
                i.putExtra("data", contentModel);
                i.putExtra("all", contentModels);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contentModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, authorName, date, time;
        ImageView imageView;

        CardView cardview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.titleView2);
            time = itemView.findViewById(R.id.textTime);
            date = itemView.findViewById(R.id.textDate2);
            authorName = itemView.findViewById(R.id.authorView);
            cardview = itemView.findViewById(R.id.cardview);




        }

    }

}
