package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        //Click on "Hint" Button
        Button mainGameHintBtn = findViewById(R.id.mainGameHintBtn);
        mainGameHintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGameActivity.this, HintActivity.class);
                startActivity(intent);
            }
        });

        //Click on "Enter Treasure Code" Button
        Button mainGameEnterCodeBtn = findViewById(R.id.mainGameEnterCodeBtn);
        mainGameEnterCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGameActivity.this, EnterCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}
