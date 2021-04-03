package com.example.blogs_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class startmenu extends AppCompatActivity {
Animation animation;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        getSupportActionBar().hide();
        imageView  = findViewById(R.id.viwer);
        animation = AnimationUtils.loadAnimation(startmenu.this,R.anim.al);
        imageView.setAnimation(animation);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                    startActivity(new Intent(startmenu.this, LoginForm.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}