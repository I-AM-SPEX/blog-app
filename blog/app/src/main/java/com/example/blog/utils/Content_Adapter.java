package com.example.blog.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blog.R;
import com.example.blog.models.ContentModel;

import java.util.ArrayList;

public class Content_Adapter extends RecyclerView.Adapter<Content_Adapter.MyViewHolder> {
    // content adapter

    Context context;

    ArrayList<ContentModel>contentModels;

    public Content_Adapter(Context context, ArrayList<ContentModel> contentModels ) {
        this.context = context;
        this.contentModels = contentModels;

    }

    @NonNull
    @Override
    public Content_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_view, parent, false);
        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Content_Adapter.MyViewHolder holder, int position) {
        holder.authorName.setText("Hard coded for now");
        holder.time.setText(contentModels.get(position).getTime());
        holder.title.setText(contentModels.get(position).getTitle());
        holder.date.setText(contentModels.get(position).getDate());
        Glide.with(context).load(contentModels.get(position).getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return contentModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, authorName, date, time;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.titleView2);
            time = itemView.findViewById(R.id.textTime);
            date = itemView.findViewById(R.id.textDate2);
            authorName = itemView.findViewById(R.id.authorView);




        }
    }
}
