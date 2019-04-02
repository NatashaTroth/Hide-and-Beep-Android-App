package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LiveRankingActivity extends AppCompatActivity {

    //TODO: NEED TO ADD "(when last hint was found)" TO TIME REMAINING TITLE VIA JAVA (text size has to be smaller) (https://stackoverflow.com/questions/16335178/different-font-size-of-strings-in-the-same-textview)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ranking);

        ImageView owlHomeBtn = findViewById(R.id.homeOwl);
        owlHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveRankingActivity.this, HomescreenActivity.class);
                startActivity(intent);
            }
        });

        ImageView liveRankingBtn = findViewById(R.id.liveRankingBtn);
        liveRankingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveRankingActivity.this, LiveRankingActivity.class);
                startActivity(intent);
            }
        });

        ImageView logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveRankingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveRankingActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        //Click on "Back to game" Button
        Button liveRankingBackBtn = findViewById(R.id.liveRankingBackBtn);
        liveRankingBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveRankingActivity.this, MainGameActivity.class);
                startActivity(intent);
            }
        });
    }
}
