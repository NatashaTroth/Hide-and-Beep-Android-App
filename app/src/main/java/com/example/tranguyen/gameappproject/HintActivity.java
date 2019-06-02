package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import java.io.Serializable;

public class HintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);


        //get Extras
        final Hunt hunt = (Hunt) getIntent().getSerializableExtra("hunt");
        final Hint[] hints = (Hint[]) getIntent().getSerializableExtra("hints");
        final int currentHint = getIntent().getExtras().getInt("currentHint");

        Log.d("CURRENT HINT ", String.valueOf(currentHint));

        TextView hintTextView = (TextView) findViewById(R.id.hintText);
        hintTextView.setText(hints[currentHint].getText());

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
                intent.putExtra("hunt",(Serializable) hunt);
                intent.putExtra("hints",(Serializable) hints);
                //intent.putExtra("currentHint", currentHint);
                intent.putExtra("sourceClass", HintActivity.class);
                startActivity(intent);
            }
        });

        //Click on Tick Button
        ImageButton hintTickBtn = findViewById(R.id.hintTickButton);
        hintTickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HintActivity.this, MainGameActivity.class);
                intent.putExtra("hunt",(Serializable) hunt);
                intent.putExtra("hints",(Serializable) hints);
                //intent.putExtra("currentHint", currentHint);
                startActivity(intent);
            }
        });
    }
}
