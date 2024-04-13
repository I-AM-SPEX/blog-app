package com.example.blog.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.blog.R;
import com.example.blog.models.ContentModel;

import java.util.ArrayList;

public class DetailPage extends AppCompatActivity {

    Intent i;

    ContentModel content;

    ArrayList<ContentModel> contentAll = new ArrayList<>();

    TextView dTitle;
    TextView dAuthor;
    TextView dDate;
    TextView dTime;
    TextView dData;

    ImageView img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        i = getIntent();
        content = (ContentModel) i.getSerializableExtra("data");
        contentAll = (ArrayList<ContentModel>) i.getSerializableExtra("all");

        dTitle = (TextView) findViewById(R.id.titleDetail);
        dAuthor = (TextView) findViewById(R.id.detailAuthor);
        dData = (TextView) findViewById(R.id.postText);
        dTime = (TextView)  findViewById(R.id.detailTime);
        dDate = (TextView)  findViewById(R.id.detailDate);
        img = (ImageView) findViewById(R.id.img);
        dTitle.setText(content.getTitle());
        dData.setText(content.getContent());
        dAuthor.setText(content.getAuthorName());
        dTime.setText(content.getTime());
        dDate.setText(content.getDate());
        Glide.with(this).load(content.getImage()).into(img);


    }
}