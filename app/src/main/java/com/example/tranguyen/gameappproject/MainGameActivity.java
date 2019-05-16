package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.Serializable;

public class MainGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        //get Extras
        final Hunt hunt = (Hunt) getIntent().getSerializableExtra("hunt");
        final Hint[] hints = (Hint[]) getIntent().getSerializableExtra("hints");
        final int currentHint = getIntent().getExtras().getInt("currentHint");

        ImageView owlHomeBtn = findViewById(R.id.homeOwl);
        owlHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGameActivity.this, HomescreenActivity.class);
                startActivity(intent);
            }
        });

        ImageView helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGameActivity.this, HelpActivity.class);
                intent.putExtra("hunt",(Serializable) hunt);
                intent.putExtra("hints",(Serializable) hints);
                intent.putExtra("currentHint", currentHint);
                intent.putExtra("sourceClass", MainGameActivity.class);
                startActivity(intent);
            }
        });

        //Click on "Hint" Button
        Button mainGameHintBtn = findViewById(R.id.mainGameHintBtn);
        mainGameHintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGameActivity.this, HintActivity.class);
                intent.putExtra("hunt",(Serializable) hunt);
                intent.putExtra("hints",(Serializable) hints);
                intent.putExtra("currentHint", currentHint);
                startActivity(intent);
            }
        });

        //Click on "Enter Treasure Code" Button
        Button mainGameEnterCodeBtn = findViewById(R.id.mainGameEnterCodeBtn);
        mainGameEnterCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGameActivity.this, EnterCodeActivity.class);
                intent.putExtra("hunt",(Serializable) hunt);
                intent.putExtra("hints",(Serializable) hints);
                intent.putExtra("currentHint", currentHint);
                startActivity(intent);
            }
        });
    }
}
