package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        //Click on Tick Button
        ImageButton hintTickBtn = findViewById(R.id.hintTickButton);
        hintTickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HintActivity.this, MainGameActivity.class);
                startActivity(intent);
            }
        });
    }
}
