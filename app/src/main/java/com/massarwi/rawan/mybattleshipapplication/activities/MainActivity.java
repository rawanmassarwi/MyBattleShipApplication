package com.massarwi.rawan.mybattleshipapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.massarwi.rawan.mybattleshipapplication.R;

/*Main Activity Class*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*On Radio Button Clicked Method*/
    public void onRadioButtonClicked(View view){
        Intent Game = new Intent(this, GameActivity.class);
        switch (view.getId()) {
            case R.id.radio_Easy:
                Game.putExtra("Level", "EASY");
                break;
            case R.id.radio_Medium:
                Game.putExtra("Level", "MEDIUM");
                break;
            case R.id.radio_Hard:
                Game.putExtra("Level", "HARD");
                break;
        }
        startActivity(Game);
    }
}
