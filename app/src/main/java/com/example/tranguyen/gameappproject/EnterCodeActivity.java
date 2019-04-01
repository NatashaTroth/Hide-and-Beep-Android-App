package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        //Click on "Submit Code" Button
        //TODO: CHANGE DESTINATION DEPENDING ON WHETHER WIN OR LOSE
        Button submitCodeBtn = findViewById(R.id.submitCodeBtn);
        submitCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterCodeActivity.this, LoseActivity.class);
                startActivity(intent);
            }
        });

        //Click on "Back" Button
        Button enterCodeBackBtn = findViewById(R.id.enterCodeBackBtn);
        enterCodeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterCodeActivity.this, MainGameActivity.class);
                startActivity(intent);
            }
        });

    }
}
