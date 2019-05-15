package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class HintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        ImageView owlHomeBtn = findViewById(R.id.homeOwl);
        owlHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HintActivity.this, HomescreenActivity.class);
                startActivity(intent);
            }
        });

        ImageView helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HintActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

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
