package com.example.tranguyen.gameappproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class Overlays {

    private final Activity activity;

    public Overlays(Activity activity){
        this.activity = activity;
    }


    public void prepareHintOverlay(Hint[] hints, final int screenHeight, int currentHintNo){
        final LinearLayout linearLayout  = (LinearLayout) this.activity.findViewById(R.id.hintOverlay);
        TextView hintTextView = (TextView) this.activity.findViewById(R.id.hintText);
        hintTextView.setText(hints[currentHintNo].getText());
        //hide Hint overlay

        linearLayout.setTranslationY(screenHeight);

        //Click on "Hint" Button
        Button mainGameHintBtn = this.activity.findViewById(R.id.mainGameHintBtn);
        mainGameHintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.animate().translationY(0);
            }
        });

        //Click on Tick Button
        ImageButton hintTickBtn = this.activity.findViewById(R.id.hintTickButton);
        hintTickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.animate().translationY(screenHeight);
            }
        });
    }

    public void prepareHelpOverlay(final int screenHeight){

        final LinearLayout linearLayout  = (LinearLayout) this.activity.findViewById(R.id.helpOverlay);
        linearLayout.setTranslationY((-1)*screenHeight);

        ImageView helpBtn = this.activity.findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.animate().translationY(0);
            }
        });

        //Click on Tick Button
        ImageButton helpTickBtn = this.activity.findViewById(R.id.helpTickButton);
        helpTickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.animate().translationY((-1)*screenHeight);
            }
        });

    }


    public void prepareEnterCodeOverlay(final int screenHeight, Hunt hunt, Context thisContext){
        final ConstraintLayout constraintLayout  = (ConstraintLayout) this.activity.findViewById(R.id.enterCodeOverlay);
        constraintLayout.setTranslationY(screenHeight);

        //Click on "Enter Treasure Code" Button
        final Button mainGameEnterCodeBtn = this.activity.findViewById(R.id.mainGameEnterCodeBtn);
        final Button mainGameHintBtn = this.activity.findViewById(R.id.mainGameHintBtn);
        mainGameEnterCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEnterCodeOverlay(constraintLayout, mainGameEnterCodeBtn, mainGameHintBtn);
//                constraintLayout.setTranslationY(0);
//                mainGameEnterCodeBtn.setVisibility(View.GONE);
//                mainGameHintBtn.setVisibility(View.GONE);
            }
        });

        //Click on "Back" Button
        Button enterCodeBackBtn = this.activity.findViewById(R.id.enterCodeBackBtn);
        enterCodeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                constraintLayout.setTranslationY(screenHeight);
                mainGameEnterCodeBtn.setVisibility(View.VISIBLE);
                mainGameHintBtn.setVisibility(View.VISIBLE);
            }
        });

        //Click on "Submit button
        createEnterCodeSubmitButtonListener(hunt, thisContext);

    }


    public void openEnterCodeOverlay(ConstraintLayout constraintLayout, Button mainGameEnterCodeBtn,Button mainGameHintBtn){

        constraintLayout.setTranslationY(0);
        mainGameEnterCodeBtn.setVisibility(View.GONE);
        mainGameHintBtn.setVisibility(View.GONE);

    }


    private void createEnterCodeSubmitButtonListener(final Hunt hunt, final Context context){
        final Activity activity = this.activity;
        Button submitCodeBtn = this.activity.findViewById(R.id.submitCodeBtn);
        submitCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) activity.findViewById(R.id.editTextEnterTreasureCode);
                String inputCode = editText.getText().toString();

                if(inputCode.equals(hunt.getWinningCode())){
                    Intent intent = new Intent(context, WinActivity.class);
                    context.startActivity(intent);
                }
                else {
                    hunt.enterCodeTries--;
                    if(hunt.enterCodeTries <= 0){
                        String toastText = "Wrong code. That was your last try. Game over!";
                        Toast toast = Toast.makeText(context,
                                toastText,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                        Intent intent = new Intent(context, LoseActivity.class);
                        context.startActivity(intent);
                    }
                    else{
                        String tryCase = "tries";
                        if(hunt.enterCodeTries == 1)
                            tryCase = "try";
                        String toastText = "Wrong code. Only " + hunt.enterCodeTries + " " + tryCase + " left!";
                        Toast toast = Toast.makeText(context,
                                toastText,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                    }

                }
            }
        });
    }

}
