package com.massarwi.rawan.mybattleshipapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.support.v7.app.AppCompatActivity;
import com.massarwi.rawan.mybattleshipapplication.R;
import com.massarwi.rawan.mybattleshipapplication.util.Game;
import com.massarwi.rawan.mybattleshipapplication.util.Turn;
import com.massarwi.rawan.mybattleshipapplication.util.Roles;
import com.massarwi.rawan.mybattleshipapplication.components.Cell;
import com.massarwi.rawan.mybattleshipapplication.components.CellListener;

/* Game Activity Class*/
public class GameActivity extends AppCompatActivity implements CellListener ,View.OnClickListener{
    //Setting Variables
    public Game cont;
    private Button startButton;
    private Button randomButton;
    private Button clearButton;
    private Handler computerTurnHandler;
    private TextView remainingShips;
    private TextView remainingComputerShips;
    private TextView headerShipsRemaining;
    private TextView turn;
    private int numOfShipsLeft;
    private Turn computerTurn;
    String level;
    private Roles roles;
    private TextView[] shipsSizesTxt;
    private int numOfComputerShipsRemaining;
    private TextView headerComputerShipsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.level = this.getIntent().getStringExtra("Level");
        cont = new Game(level);
        cont.createSetBoard(this);
        cont.init();
        cont.setRandom(false);
        setButtons();
    }

    /*Set Buttons Method*/
    private void setButtons() {
        startButton = (Button) findViewById(R.id.Start);
        startButton.setOnClickListener(this);
        startButton.setClickable(false);

        randomButton = (Button) findViewById(R.id.Random);
        randomButton.setOnClickListener(this);

        clearButton = (Button) findViewById(R.id.Clear);
        clearButton.setOnClickListener(this);

        remainingShips = findViewById(R.id.remainingShips);
        remainingShips.setVisibility(View.GONE);

        remainingComputerShips = findViewById(R.id.ComputerShipsLeft);
        remainingComputerShips.setVisibility(View.GONE);

        headerShipsRemaining = findViewById(R.id.shipRemainHeader);
        headerShipsRemaining.setVisibility(View.GONE);
        headerComputerShipsRemaining = findViewById(R.id.ComputerRemainingShipHeader);
        headerComputerShipsRemaining.setText("Ships Sizes");

        turn = findViewById(R.id.turn);
        turn.setVisibility(View.GONE);

        shipsSizesTxt = new TextView[cont.getShipsSizes().length];
        int[] shipSizes = cont.getShipsSizes().clone();
        LinearLayout ColsLayout = new LinearLayout(this);
        ColsLayout.setBackgroundColor(Color.TRANSPARENT);
        ColsLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < shipSizes.length; i++) {
            shipsSizesTxt[i] = new TextView(this);
            if(i == shipSizes.length-1)
                shipsSizesTxt[i].setText(Integer.toString(shipSizes[i]));
            else
                shipsSizesTxt[i].setText(Integer.toString(shipSizes[i]) + ",");
            shipsSizesTxt[i].setTextSize(30);
            ColsLayout.addView(shipsSizesTxt[i]);
        }
        ColsLayout.setGravity(Gravity.CENTER);
        TableLayout viewsTable = (TableLayout) this.findViewById(R.id.topScoresTable);
        viewsTable.setGravity(Gravity.CENTER);
        viewsTable.addView(ColsLayout);


    }

    public void buttonClicked(Cell cell) {
        if (cont.isSetMode()) {
            int col = cell.getCol();
            int row = cell.getRow();
            cont.setShips(col, row, this);
        } else {
            int col = cell.getCol();
            int row = cell.getRow();
            if (cont.handelHit(col, row, this)) {
                computerTurn();
            }

        }
    }

    /*Computer Turn Method*/
    private void computerTurn() {
        turn.setText(R.string.computerTurn);
        turn.setTextColor(Color.RED);
        setAllButtons(false);
        computerTurnHandler.postDelayed(computerTurn, (long) (Math.random() * 4000 + 1500));
    }

    /*Set All Buttons Method*/
    public void setAllButtons(boolean b) {
        for (Cell cellArray[] : cont.getSetBoard()) {
            for (Cell cell : cellArray) {
                cell.setClickable(b);
            }

        }
    }

    /*On Click Method*/
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Clear:
                cont.clearBoard(this);
                startButton.setClickable(false);
                setButtons();
                break;
            case R.id.Random:
                cont.clearBoard(this);
                cont.setRandom(true);
                cont.setShipsRandom(this);
                cont.setRandom(false);
                startButton.setClickable(true);
                break;
            case R.id.Start:
                setButtonsForStart();
                cont.setMode(false);
                cont.createUserBoard(this);
                cont.clearBoard(this);
                cont.setRandom(true);
                cont.setIsComputerBoard(true);
                cont.setShipsRandom(this);
                cont.setRandom(false);
                cont.setIsComputerBoard(false);
                cont.setTextViews(this);
                computerTurnHandler = new Handler();
                computerTurn = new Turn(computerTurnHandler, this);
                roles = new Roles(cont.getPlace(), cont.getPlace());
                this.setNumOfComputerShipsRemaining(cont.getPlace());
                this.setNumOfShipsLeft(cont.getPlace());
                break;

        }
    }

    /*Set Start Buttons Method*/
    private void setButtonsForStart() {
        startButton.setVisibility(View.GONE);
        randomButton.setVisibility(View.GONE);
        clearButton.setVisibility(View.GONE);

        remainingShips.setVisibility(View.VISIBLE);
        remainingComputerShips.setVisibility(View.VISIBLE);
        headerShipsRemaining.setVisibility(View.VISIBLE);
        turn.setVisibility(View.VISIBLE);
        headerComputerShipsRemaining.setText(R.string.computer_remain_ships);
    }

    /*Get Remaining Ships Method */
    public TextView getRemainingShips() {
        return remainingShips;
    }

    /*Get Remaining Computer Ships Method*/
    public TextView getRemainingComputerShips() {
        return remainingComputerShips;
    }

    /*Get Turn Method*/
    public TextView getTurn() {
        return turn;
    }

    /*Set Remaining Ships Method*/
    public void setRemainingShips(TextView remainingShips) {
        this.remainingShips = remainingShips;
    }

    /*Set Remaining Computer Ships Method*/
    public void setRemainingComputerShips(TextView remainingComputerShips) {
        this.remainingComputerShips = remainingComputerShips;
    }

    /*Set Turn Method*/
    public void setTurn(TextView turn) {
        this.turn = turn;
    }

    /*Get Role Method*/
    public Roles getRoles() {
        return roles;
    }

    /*Get Number Of Remaining Ships Method*/
    public int getNumOfRemainingShips() {
        return numOfShipsLeft;
    }

    /*Set Number Of Remaining Ships Method*/
    public void setNumOfShipsLeft(int numOfShipsLeft) {
        this.numOfShipsLeft = numOfShipsLeft;
    }

    /*Number of Ships Remaining - 1*/
    public void NumOfShipsRemainingMinusOne() {
        this.numOfShipsLeft--;
    }

    /*Get Number Of Remaining Computer Ships Method*/
    public int getNumOfComputerShipsRemaining() {
        return numOfComputerShipsRemaining;
    }

    /*Set Number Of Remaining Computer Ships Method*/
    public void setNumOfComputerShipsRemaining(int numOfComputerShipsRemaining) {
        this.numOfComputerShipsRemaining = numOfComputerShipsRemaining;
    }

    /*Number of Remaining Computer Ships - 1*/
    public void minusRemainingNumOfcomputerShips() {
        this.numOfComputerShipsRemaining--;
    }

    /*Get Level Method*/
    public String getLevel() {
        return level;
    }

    /*Get Ships To Plain Method*/
    public TextView[] getShipsSizesTxt() {
        return shipsSizesTxt;
    }

    /*Get Start Button Method*/
    public Button getStartButton() {
        return startButton;
    }
}
