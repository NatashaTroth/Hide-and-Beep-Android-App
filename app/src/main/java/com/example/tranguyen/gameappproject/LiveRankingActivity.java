package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LiveRankingActivity extends AppCompatActivity {

    //TODO: NEED TO ADD "(when last hint was found)" TO TIME REMAINING TITLE VIA JAVA (text size has to be smaller) (https://stackoverflow.com/questions/16335178/different-font-size-of-strings-in-the-same-textview)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_ranking);

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
