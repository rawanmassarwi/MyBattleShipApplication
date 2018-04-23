package com.massarwi.rawan.mybattleshipapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.support.v7.app.AppCompatActivity;
import com.massarwi.rawan.mybattleshipapplication.R;

/*Score Activity Class*/
public class ScoreActivity extends AppCompatActivity implements View.OnClickListener{

    //Setting Variables
    private Button retry;
    private Button mainMenu;
    private String level;
    private String whoWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        whoWin = this.getIntent().getStringExtra("isWin");
        this.level = this.getIntent().getStringExtra("Level");
        RelativeLayout relativeLayoutScore = (RelativeLayout) this.findViewById(R.id.relative_score);
        retry = this.findViewById(R.id.Retry);
        mainMenu = this.findViewById(R.id.MainMenu);
        switch (whoWin) {
            case "WIN":
                relativeLayoutScore.setBackgroundResource(R.drawable.win_screen);
                break;
            case "LOSE":
                relativeLayoutScore.setBackgroundResource(R.drawable.game_over);
                break;

        }
        setButtons();
    }

    /*Set Buttons Method*/
    private void setButtons() {
        retry = (Button) findViewById(R.id.Retry);
        retry.setOnClickListener(this);
        mainMenu = (Button) findViewById(R.id.MainMenu);
        mainMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Retry:
                Intent game = new Intent(this, GameActivity.class);
                game.putExtra("Level", this.getLevel());
                this.startActivity(game);
                this.finish();
                break;

            case R.id.MainMenu:
                this.finish();
                break;
        }
    }

    /*Get Level Method*/
    public String getLevel() {
        return level;
    }
}
