package com.massarwi.rawan.mybattleshipapplication.util;

import android.content.Intent;

import com.massarwi.rawan.mybattleshipapplication.activities.GameActivity;
import com.massarwi.rawan.mybattleshipapplication.activities.ScoreActivity;

/*Roles Class*/
public class Roles {
    private int remainingShips;
    private int computerRemainingShips;

    /*Constractor*/
    public Roles(int leftShips, int comLeftShips) {
        this.remainingShips = leftShips;
        this.computerRemainingShips = comLeftShips;
    }

    /*Victory Method*/
    public void victory(GameActivity game) {

        Intent Score = new Intent(game, ScoreActivity.class);
        Score.putExtra("isWin", "WIN");
        Score.putExtra("Level", game.getLevel());
        game.startActivity(Score);
        game.finish();
    }
    /*Lose Method*/
    public void lose(GameActivity game) {
        Intent Score = new Intent(game, ScoreActivity.class);
        Score.putExtra("isWin", "LOSE");
        Score.putExtra("Level", game.getLevel());
        game.startActivity(Score);
        game.finish();
    }

    /*Get Remaining Ships Method*/
    public int getRemainingShips() {
        return remainingShips;
    }

    /*Set Remaining Ships Method*/
    public void setRemainingShips(int remainingShips) {
        this.remainingShips = remainingShips;
    }

    /*Get Computer Remaining Ships Method*/
    public int getComputerRemainingShips() {
        return computerRemainingShips;
    }

    /*Set Computer Remaining Ships Method*/
    public void setComputerRemainingShips(int computerRemainingShips) {
        this.computerRemainingShips = computerRemainingShips;
    }

}
