package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class EnterCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        //get Extras
        final Hunt hunt = (Hunt) getIntent().getSerializableExtra("hunt");
        final Hint[] hints = (Hint[]) getIntent().getSerializableExtra("hints");
        final int currentHint = getIntent().getExtras().getInt("currentHint");

        ImageView owlHomeBtn = findViewById(R.id.homeOwl);
        owlHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterCodeActivity.this, HomescreenActivity.class);
                startActivity(intent);
            }
        });

        ImageView helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterCodeActivity.this, HelpActivity.class);
                intent.putExtra("hunt",(Serializable) hunt);
                intent.putExtra("hints",(Serializable) hints);
                intent.putExtra("currentHint", currentHint);
                intent.putExtra("sourceClass", EnterCodeActivity.class);
                startActivity(intent);
            }
        });

        //Click on "Submit Code" Button
        //TODO: CHANGE DESTINATION DEPENDING ON WHETHER WIN OR LOSE
        Button submitCodeBtn = findViewById(R.id.submitCodeBtn);
        submitCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.editTextEnterTreasureCode);
                String inputCode = editText.getText().toString();
//                TextView tv = (TextView) findViewById(R.id.textView2);
//                tv.setText(inputCode + "  " + hunt.getWinningCode());
//                tv.append(Boolean.toString(inputCode.equals(hunt.getWinningCode())));

                if(inputCode.equals(hunt.getWinningCode())){
                    Intent intent = new Intent(EnterCodeActivity.this, WinActivity.class);
                    startActivity(intent);
                }
                //TODO: ADD COUNTER
                else {
                    Intent intent = new Intent(EnterCodeActivity.this, LoseActivity.class);
                    startActivity(intent);
                }
            }
        });

        //Click on "Back" Button
        Button enterCodeBackBtn = findViewById(R.id.enterCodeBackBtn);
        enterCodeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterCodeActivity.this, MainGameActivity.class);
                intent.putExtra("hunt",(Serializable) hunt);
                intent.putExtra("hints",(Serializable) hints);
                intent.putExtra("currentHint", currentHint);
                startActivity(intent);
            }
        });

    }
}
