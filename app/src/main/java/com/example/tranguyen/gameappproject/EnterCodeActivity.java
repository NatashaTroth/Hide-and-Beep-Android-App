package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;

public class EnterCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        //get Extras
        final Hunt hunt = (Hunt) getIntent().getSerializableExtra("hunt");
        final Hint[] hints = (Hint[]) getIntent().getSerializableExtra("hints");

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
                //intent.putExtra("currentHint", currentHint);
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

                if(inputCode.equals(hunt.getWinningCode())){
                    Intent intent = new Intent(EnterCodeActivity.this, WinActivity.class);
                    startActivity(intent);
                }
                else {
                    hunt.enterCodeTries--;
                    if(hunt.enterCodeTries <= 0){
                        String toastText = "Wrong code. That was your last try. Game over!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                toastText,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                        Intent intent = new Intent(EnterCodeActivity.this, LoseActivity.class);
                        startActivity(intent);
                    }
                    else{
                        String tryCase = "tries";
                        if(hunt.enterCodeTries == 1)
                            tryCase = "try";
                        String toastText = "Wrong code. Only " + hunt.enterCodeTries + " " + tryCase + " left!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                toastText,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                    }

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
                //intent.putExtra("currentHint", currentHint);
                startActivity(intent);
            }
        });

    }
}
