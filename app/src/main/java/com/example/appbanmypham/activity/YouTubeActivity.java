package com.example.appbanmypham.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appbanmypham.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YouTubeActivity extends AppCompatActivity {
    YouTubePlayerView playerView;/// Đối tượng để hiển thị video YouTube
    String idVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
        idVideo = getIntent().getStringExtra("linkvideo");
        initView();
    }

    private void initView() {
        playerView = findViewById(R.id.youtuber);
        playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(idVideo,0);// Tải và phát video khi YouTubePlayer đã sẵn sàng
            }
        });
    }
}