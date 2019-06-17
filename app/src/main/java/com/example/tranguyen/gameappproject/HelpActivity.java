package com.example.tranguyen.gameappproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final Class nextActivityClass;
        final Hunt hunt;
        final Hint[] hints;
       // final int currentHint;

        if(getIntent().hasExtra("sourceClass")){
            Bundle extras = getIntent().getExtras();
            nextActivityClass = (Class<Activity>)extras.getSerializable("sourceClass");
        }
        else{
            nextActivityClass = HomescreenActivity.class;
        }

        if(getIntent().hasExtra("hunt")){
            //get Extras
            hunt = (Hunt) getIntent().getSerializableExtra("hunt");
            hints = (Hint[]) getIntent().getSerializableExtra("hints");
            //currentHint = getIntent().getExtras().getInt("currentHint");
        }
        else{
             hunt = null;
             hints = null;
             //currentHint = 0;
        }


        //Make link in textview clickable
        TextView helpTextView = (TextView) findViewById(R.id.helpText);
        helpTextView.setMovementMethod(LinkMovementMethod.getInstance());

        Button backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, nextActivityClass);
                if(hunt != null) {
                    intent.putExtra("hunt", (Serializable) hunt);
                    intent.putExtra("hints", (Serializable) hints);
                    //intent.putExtra("currentHint", currentHint);
                }
                startActivity(intent);
            }
        });
    }
}
