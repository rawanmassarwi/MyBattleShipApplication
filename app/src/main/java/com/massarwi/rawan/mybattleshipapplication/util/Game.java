package com.massarwi.rawan.mybattleshipapplication.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.massarwi.rawan.mybattleshipapplication.activities.GameActivity;
import com.massarwi.rawan.mybattleshipapplication.R;
import com.massarwi.rawan.mybattleshipapplication.components.Cell;

/*Game Class*/
public class Game {
    //Setting Variables
    private int place = 0;
    private int currentSizes;
    private int boardSize;
    private String level;
    private boolean setMode;
    private boolean random;
    private Handler handler;
    private boolean computerBoard;
    private RelativeLayout setTable;
    private RelativeLayout userTable;
    private LinearLayout setRowsLayout;
    private LinearLayout setColsLayout;
    private int[] shipsSizes;
    private int[] startPosistion;
    private Cell[][] setBoard;
    private Cell[][] userBoard;
    private Ship[] userShips;
    private Ship[] computerShips;
    private static final int HARD_BOARD_SIZE = 10;
    private static final int MEDIUM_BOARD_SIZE = 9;
    private static final int EASY_BOARD_SIZE = 8;
    private static final int[] EASY_SHIPS_SIZES = {3, 4, 5};
    private static final int[] MEDIUM_SHIPS_SIZES = {2, 3, 4, 5};
    private static final int[] HARD_SHIPS_SIZES = {2, 3, 4, 5, 6};

    /*Game Constractor*/
    public Game(String thisLevel) {
        setThisLevel(thisLevel);
        this.setMode = true;
    }

    /*Create User Board Method*/
    public void createUserBoard(GameActivity game) {
        userBoard = new Cell[this.getBoardSize()][this.getBoardSize()];
        setRowsLayout = new LinearLayout(game);
        setRowsLayout.setBackgroundColor(Color.TRANSPARENT);
        setRowsLayout.setOrientation(LinearLayout.VERTICAL);
        for (int col = 0; col < this.getBoardSize(); col++) {
            setColsLayout = new LinearLayout(game);
            setColsLayout.setBackgroundColor(Color.TRANSPARENT);
            setColsLayout.setOrientation(LinearLayout.HORIZONTAL);
            setColsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            for (int row = 0; row < this.getBoardSize(); row++) {
                userBoard[col][row] = new Cell(game);
                userBoard[col][row].setPosition(col, row);
                userBoard[col][row].setListener(game);
                setColsLayout.addView(userBoard[col][row]);
                if (getSetBoard()[col][row].isShip()) {
                    userBoard[col][row].setShip(true);
                    userBoard[col][row].setShipId(getSetBoard()[col][row].getShipId());
                    userBoard[col][row].setBackgroundResource(R.drawable.blue_cell);
                } else {
                    userBoard[col][row].setBackgroundResource(R.drawable.cell_background);
                }

                userBoard[col][row].setClickable(false);
            }
            setRowsLayout.addView(setColsLayout);
        }
        userTable = (RelativeLayout) game.findViewById(R.id.userBoard);
        userTable.addView(setRowsLayout);
        userTable.setGravity(Gravity.CENTER);
    }

    /*Create Set Board Method*/
    public void createSetBoard(GameActivity setScreen) {
        setBoard = new Cell[this.getBoardSize()][this.getBoardSize()];
        setRowsLayout = new LinearLayout(setScreen);
        setRowsLayout.setBackgroundColor(Color.TRANSPARENT);
        setRowsLayout.setOrientation(LinearLayout.VERTICAL);
        setRowsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        for (int col = 0; col < this.getBoardSize(); col++) {
            setColsLayout = new LinearLayout(setScreen);
            setColsLayout.setBackgroundColor(Color.TRANSPARENT);
            setColsLayout.setOrientation(LinearLayout.HORIZONTAL);
            setColsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            for (int row = 0; row < this.getBoardSize(); row++) {
                setBoard[col][row] = new Cell(setScreen);
                setBoard[col][row].setPosition(col, row);
                setBoard[col][row].setListener(setScreen);
                setBoard[col][row].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                setColsLayout.addView(setBoard[col][row]);
                setBoard[col][row].setBackgroundResource(R.drawable.cell_background);
            }
            setRowsLayout.addView(setColsLayout);
        }
        setTable = (RelativeLayout) setScreen.findViewById(R.id.SetAndOpBoard);
        setTable.addView(setRowsLayout);
        setTable.setGravity(Gravity.CENTER);
    }

    /*Check Remaining Sizes Method*/
    private boolean checkRemainingSizes(int size, GameActivity game) {

        for (int i = 0; i < shipsSizes.length; i++) {
            if (size + 1 == shipsSizes[i]) {
                currentSizes = shipsSizes[i];
                if (!random) {
                    shipsSizes[i] = -1;
                    place += 1;
                }
                game.getStartButton().setClickable(true);
                game.getShipsSizesTxt()[i].setVisibility(View.GONE);
                return true;
            }

        }
        return false;
    }

    /*Check Method*/
    private boolean check(int startX, int endX, int startY, int endY) {
        if (this.getSetBoard()[startY][startX].isShip() || this.getSetBoard()[startY][startX].isBesideShip())
            return false;
        if (startY == endY && startX != endX) {
            for (int i = 0; i < (endX - startX); i++) {
                if (this.getSetBoard()[startY][startX + i + 1].isShip() || this.getSetBoard()[startY][startX + i + 1].isBesideShip())
                    return false;
            }
        } else {
            if (startY != endY && startX == endX) {

                for (int i = 0; i < (endY - startY); i++) {
                    if (this.getSetBoard()[startY + i + 1][startX].isShip() || this.getSetBoard()[startY + i + 1][startX].isBesideShip())
                        return false;
                }
            }
        }
        return true;
    }

    /*Color Cells Method*/
    private void colorCells(int startX, int endX, int startY, int endY) {
        if (startY == endY && startX != endX) {
            Ship currentShip = null;
            if (!isComputerBoard()) {
                currentShip = getShipIdBySize(currentSizes, this.getUserShips());
            } else {
                currentShip = getShipIdBySize(currentSizes, this.getComputerShips());
            }
            this.getSetBoard()[startY][startX].setShip(true);
            if (!isComputerBoard())
                this.getSetBoard()[startY][startX].setBackgroundResource(R.drawable.blue_cell);
            this.getSetBoard()[startY][startX].setShipId(currentShip.getId());


            for (int i = 0; i < (endX - startX); i++) {
                if (!isComputerBoard())
                    this.getSetBoard()[startY][startX + i + 1].setBackgroundResource(R.drawable.blue_cell);
                this.getSetBoard()[startY][startX + i + 1].setShipId(currentShip.getId());
                this.getSetBoard()[startY][startX + i + 1].setShip(true);
            }
            currentShip.setPlace(true);
        } else {
            if (startY != endY && startX == endX) {
                Ship currShip = null;
                if (!isComputerBoard()) {
                    currShip = getShipIdBySize(currentSizes, this.getUserShips());
                } else {
                    currShip = getShipIdBySize(currentSizes, this.getComputerShips());
                }
                this.getSetBoard()[startY][startX].setShip(true);
                if (!isComputerBoard())
                    this.getSetBoard()[startY][startX].setBackgroundResource(R.drawable.blue_cell);
                this.getSetBoard()[startY][startX].setShipId(currShip.getId());

                for (int i = 0; i < (endY - startY); i++) {
                    if (!isComputerBoard())
                        this.getSetBoard()[startY + i + 1][startX].setBackgroundResource(R.drawable.blue_cell);
                    this.getSetBoard()[startY + i + 1][startX].setShipId(currShip.getId());
                    this.getSetBoard()[startY + i + 1][startX].setShip(true);

                }
                currShip.setPlace(true);
            } else {
                if (!isComputerBoard())
                    this.getSetBoard()[startY][startX].setBackgroundResource(R.drawable.blue_cell);
            }
        }
    }

    /*Clear Board Method*/
    public void clearBoard(GameActivity setShipsScreen) {
        this.getSetTable().removeAllViews();
        this.createSetBoard(setShipsScreen);
        String level = setShipsScreen.getIntent().getStringExtra("Level");
        switch (level) {
            case "EASY":
                this.shipsSizes = Game.EASY_SHIPS_SIZES.clone();

                break;
            case "MEDIUM":
                this.shipsSizes = Game.MEDIUM_SHIPS_SIZES.clone();
                break;
            case "HARD":
                this.shipsSizes = Game.HARD_SHIPS_SIZES.clone();
                break;
        }
        place = 0;
        startPosistion[0] = -1;
        for (Ship ship : userShips
                ) {
            ship.clearShip();
        }
        for (TextView v : setShipsScreen.getShipsSizesTxt()
                ) {
            v.setVisibility(View.GONE);
        }
    }

    /*Init Method*/
    public void init() {
        startPosistion = new int[2];
        startPosistion[0] = -1;
        switch (this.getLevel()) {
            case "EASY":
                this.shipsSizes = Game.EASY_SHIPS_SIZES.clone();
                break;
            case "MEDIUM":
                this.shipsSizes = Game.MEDIUM_SHIPS_SIZES.clone();
                break;
            case "HARD":
                this.shipsSizes = Game.HARD_SHIPS_SIZES.clone();
                break;
        }
    }

    /*Handel Hit Method*/
    public boolean handelHit(int col, int row, GameActivity game) {
        if (!this.getSetBoard()[col][row].isHit()) {
            this.getSetBoard()[col][row].setHit(true);
            if (this.getSetBoard()[col][row].isShip()) {
                this.getSetBoard()[col][row].setBackgroundResource(R.drawable.boom);
                Ship hitedShip = game.cont.getShipById(game.cont.getSetBoard()[col][row].getShipId(), game.cont.getComputerShips());
                hitedShip.setNumOfHits();
                if (hitedShip.isSink()) {
                    game.minusRemainingNumOfcomputerShips();
                    game.getRemainingComputerShips().setText(Integer.toString(game.getNumOfComputerShipsRemaining()));
                    if (game.getNumOfComputerShipsRemaining() == 0) {
                        game.getRoles().victory(game);
                    }
                        final Dialog builder = new Dialog(game);
                        builder.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
                        builder.getWindow().setBackgroundDrawable(
                                new ColorDrawable(Color.TRANSPARENT));
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                            }
                        });
                        if(game.getNumOfComputerShipsRemaining() !=0){
                            ImageView imageView = new ImageView(game);
                            handler=new Handler();


                        imageView.setImageResource(R.drawable.sunk_a_ship);
                        handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            builder.dismiss();
                        }
                    }, 4000);


                        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        }
                        builder.setCancelable(true);
                        if(!game.isFinishing()) {
                            builder.show();
                        }
                }
            } else
                this.getSetBoard()[col][row].setBackgroundResource(R.drawable.incorrect);

            return true;
        }
        return false;
    }

    /*Set Mode Method*/
    public void setMode(boolean setMode) {
        this.setMode = setMode;
    }

    /*Set This Level Method*/
    private void setThisLevel(String level) {
        setLevel(level);
        switch (level) {
            case "EASY":
                setBoardSize(EASY_BOARD_SIZE);
                this.userShips = new Ship[EASY_SHIPS_SIZES.length];
                this.computerShips = new Ship[EASY_SHIPS_SIZES.length];
                for (int i = 0; i < EASY_SHIPS_SIZES.length; i++) {
                    this.userShips[i] = new Ship(EASY_SHIPS_SIZES[i]);
                    this.computerShips[i] = new Ship(EASY_SHIPS_SIZES[i]);
                }
                break;
            case "MEDIUM":
                setBoardSize(MEDIUM_BOARD_SIZE);
                this.userShips = new Ship[MEDIUM_SHIPS_SIZES.length];
                this.computerShips = new Ship[MEDIUM_SHIPS_SIZES.length];
                for (int i = 0; i < MEDIUM_SHIPS_SIZES.length; i++) {
                    this.userShips[i] = new Ship(MEDIUM_SHIPS_SIZES[i]);
                    this.computerShips[i] = new Ship(MEDIUM_SHIPS_SIZES[i]);
                }
                break;
            case "HARD":
                setBoardSize(HARD_BOARD_SIZE);
                this.userShips = new Ship[HARD_SHIPS_SIZES.length];
                this.computerShips = new Ship[HARD_SHIPS_SIZES.length];
                for (int i = 0; i < HARD_SHIPS_SIZES.length; i++) {
                    this.userShips[i] = new Ship(HARD_SHIPS_SIZES[i]);
                    this.computerShips[i] = new Ship(HARD_SHIPS_SIZES[i]);
                }
                break;

        }
    }

    /*Set Level Method*/
    private void setLevel(String level) {
        this.level = level;
    }

    /*Set Random*/
    public void setRandom(boolean random) {
        this.random = random;
    }

    /*Set Ships Random Method*/
    public void setShipsRandom(GameActivity setShipsScreen) {
        boolean OK;
        int count = 0;
        int randFirstCol;
        int randFirstRow;
        for (int i = 0; i < shipsSizes.length; i++) {
            do {
                randFirstCol = (int) (Math.random() * (this.getBoardSize() - 1));
                randFirstRow = (int) (Math.random() * (this.getBoardSize() - 1));
            } while (!setShips(randFirstCol, randFirstRow, setShipsScreen));
            int randSecondCol = 0;
            int randSecondRow = 0;
            do {

                int verOrHor = (int) (Math.random() * 2);
                int plusOrMinus = (int) (Math.random() * 2);
                switch (verOrHor) {
                    case 0:

                        randSecondRow = randFirstRow;
                        switch (plusOrMinus) {
                            case 0:
                                randSecondCol = randFirstCol - shipsSizes[i] + 1;

                                break;
                            case 1:
                                randSecondCol = randFirstCol + shipsSizes[i] - 1;
                                break;
                        }
                        break;
                    case 1:
                        randSecondCol = randFirstCol;
                        switch (plusOrMinus) {
                            case 0:
                                randSecondRow = randFirstRow - shipsSizes[i] + 1;
                                break;
                            case 1:
                                randSecondRow = randFirstRow + shipsSizes[i] - 1;
                                break;
                        }
                        break;
                }
                if ((randSecondCol < 0 || randSecondCol > this.getBoardSize() - 1) || (randSecondRow < 0 || randSecondRow > this.getBoardSize() - 1) || (count == 10)) {
                    --i;
                    count = 0;
                    OK = false;
                    this.getSetBoard()[startPosistion[0]][startPosistion[1]].setBackgroundResource(R.drawable.cell_background);
                    startPosistion[0] = -1;
                    break;
                } else {
                    count++;
                    OK = true;
                }
            } while (!setShips(randSecondCol, randSecondRow, setShipsScreen));
            if (OK) {
                shipsSizes[i] = -1;
                place += 1;
            }
        }
    }

    /*Set Ships Method*/
    public boolean setShips(int col, int row, GameActivity setShipsScreen) {
        if (startPosistion[0] > -1) {
            if (col != startPosistion[0] && row != startPosistion[1]) {
                  if (!isRandom()) {
                    AlertDialog.Builder notSameColRowAlert = new AlertDialog.Builder(setShipsScreen);
                    notSameColRowAlert.setMessage("The second click must be in the same row or collum like the first click");
                    notSameColRowAlert.setTitle("Error Message...");
                    notSameColRowAlert.setPositiveButton("OK", null);
                    notSameColRowAlert.setCancelable(true);
                    notSameColRowAlert.create().show();
                    notSameColRowAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                } else {
                    return false;
                }
            } else {
                if (col == startPosistion[0]) {
                    if (checkRemainingSizes(Math.abs(row - startPosistion[1]), setShipsScreen)) {
                        if (startPosistion[1] > row) {
                            if (check(row, startPosistion[1], col, startPosistion[0])) {
                                colorCells(row, startPosistion[1], col, startPosistion[0]);
                                startPosistion[0] = -1;
                            } else {
                                if (!isRandom()) {
                                    AlertDialog.Builder notValidAlert = new AlertDialog.Builder(setShipsScreen);
                                    notValidAlert.setMessage("You can't placed 2 ships on the same cell");
                                    notValidAlert.setTitle("Error Message...");
                                    notValidAlert.setPositiveButton("OK", null);
                                    notValidAlert.setCancelable(true);
                                    notValidAlert.create().show();
                                    notValidAlert.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                } else {
                                    return false;
                                }
                            }
                        } else {
                            if (check(startPosistion[1], row, col, startPosistion[0])) {
                                colorCells(startPosistion[1], row, col, startPosistion[0]);
                                startPosistion[0] = -1;
                            } else {
                                if (!isRandom()) {
                                    AlertDialog.Builder notValidAlert = new AlertDialog.Builder(setShipsScreen);
                                    notValidAlert.setMessage("You can't placed 2 ships on the same cell");
                                    notValidAlert.setTitle("Error Message...");
                                    notValidAlert.setPositiveButton("OK", null);
                                    notValidAlert.setCancelable(true);
                                    notValidAlert.create().show();
                                    notValidAlert.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                } else {
                                    return false;
                                }
                            }

                        }
                    } else {
                        if (place != shipsSizes.length) {
                            if (!isRandom()) {
                                AlertDialog.Builder notThisSizeAlert = new AlertDialog.Builder(setShipsScreen);
                                notThisSizeAlert.setMessage("you don't have this size");
                                notThisSizeAlert.setTitle("Error Message...");
                                notThisSizeAlert.setPositiveButton("OK", null);
                                notThisSizeAlert.setCancelable(true);
                                notThisSizeAlert.create().show();
                                notThisSizeAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    if (checkRemainingSizes(Math.abs(col - startPosistion[0]), setShipsScreen)) {
                        if (startPosistion[0] > col) {
                            if (check(row, startPosistion[1], col, startPosistion[0])) {
                                colorCells(row, startPosistion[1], col, startPosistion[0]);
                                startPosistion[0] = -1;
                            } else {
                                if (!isRandom()) {
                                    AlertDialog.Builder notValidAlert = new AlertDialog.Builder(setShipsScreen);
                                    notValidAlert.setMessage("You can't placed two ships on the same cell");
                                    notValidAlert.setTitle("Error Message...");
                                    notValidAlert.setPositiveButton("OK", null);
                                    notValidAlert.setCancelable(true);
                                    notValidAlert.create().show();
                                    notValidAlert.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                } else {
                                    return false;
                                }
                            }
                        } else {
                            if (check(startPosistion[1], row, startPosistion[0], col)) {
                                colorCells(startPosistion[1], row, startPosistion[0], col);
                                startPosistion[0] = -1;
                            } else {
                                if (!isRandom()) {
                                    AlertDialog.Builder notValidAlert = new AlertDialog.Builder(setShipsScreen);
                                    notValidAlert.setMessage("You can't placed two ships on the same cell");
                                    notValidAlert.setTitle("Error Message...");
                                    notValidAlert.setPositiveButton("OK", null);
                                    notValidAlert.setCancelable(true);
                                    notValidAlert.create().show();
                                    notValidAlert.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });

                                } else {
                                    return false;
                                }
                            }
                        }
                    } else {
                        if (place != shipsSizes.length) {
                            if (!isRandom()) {
                                AlertDialog.Builder notThisSizeAlert = new AlertDialog.Builder(setShipsScreen);
                                notThisSizeAlert.setMessage("you don't have this size");
                                notThisSizeAlert.setTitle("Error Message...");
                                notThisSizeAlert.setPositiveButton("OK", null);
                                notThisSizeAlert.setCancelable(true);
                                notThisSizeAlert.create().show();
                                notThisSizeAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                            } else {
                                return false;
                            }
                        }

                    }

                }
            }
        } else {
            if (place != shipsSizes.length) {
                if (check(row, row, col, col)) {
                    startPosistion[0] = col;
                    startPosistion[1] = row;
                    colorCells(startPosistion[1], startPosistion[1], startPosistion[0], startPosistion[0]);
                } else {
                    if (!isRandom()) {
                        AlertDialog.Builder notValidAlert = new AlertDialog.Builder(setShipsScreen);
                        notValidAlert.setMessage("You can't placed 2 ships on the same cell");
                        notValidAlert.setTitle("Error Message...");
                        notValidAlert.setPositiveButton("OK", null);
                        notValidAlert.setCancelable(true);
                        notValidAlert.create().show();
                        notValidAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                    } else {
                        return false;
                    }
                }
            } else {
                if (!isRandom()) {
                    AlertDialog.Builder noMoreShipsAlert = new AlertDialog.Builder(setShipsScreen);
                    noMoreShipsAlert.setMessage("you place all the ships please start the game");
                    noMoreShipsAlert.setTitle("Message");
                    noMoreShipsAlert.setPositiveButton("OK", null);
                    noMoreShipsAlert.setCancelable(true);
                    noMoreShipsAlert.create().show();
                    noMoreShipsAlert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    setShipsScreen.getStartButton().setClickable(true);
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    /*Set Is Computer Board Method*/
    public void setIsComputerBoard(boolean computerBoard) {
        this.computerBoard = computerBoard;
    }

    /*Set Board Size Method*/
    private void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    /*Set Text Views Method*/
    public void setTextViews(GameActivity game) {
        game.setRemainingShips((TextView) game.findViewById(R.id.remainingShips));
        game.setRemainingComputerShips((TextView) game.findViewById(R.id.ComputerShipsLeft));
        game.setTurn((TextView) game.findViewById(R.id.turn));
        game.getRemainingShips().setText(Integer.toString(this.place));
        game.getRemainingComputerShips().setText(Integer.toString(this.place));
        game.getTurn().setText(R.string.userTurn);
        game.getTurn().setTextColor(Color.GREEN);
    }

    /*Is Set Mode Method*/
    public boolean isSetMode() {
        return setMode;
    }

    /*Is Computer Board Method*/
    private boolean isComputerBoard() {
        return computerBoard;
    }

    /*Is Random */
    private boolean isRandom() {
        return random;
    }
    /*Get Place Method*/
    public int getPlace() {
        return place;
    }

    /*Get Board Size Method*/
    public int getBoardSize() {
        return boardSize;
    }

    /*Get Level Method*/
    private String getLevel() {
        return level;
    }

    /*Get Computer Ships Method*/
    public Ship[] getComputerShips() {
        return computerShips;
    }

    /*Get Set Board Method*/
    public Cell[][] getSetBoard() {
        return setBoard;
    }

    /*Get Set Table Method*/
    private RelativeLayout getSetTable() {
        return setTable;
    }

    /*Get Ship ID By Size Method*/
    private Ship getShipIdBySize(int size, Ship[] ships) {
        for (Ship ship : ships) {
            if (ship.getSize() == size && !ship.isPlace()) {
                return ship;
            }
        }
        return null;
    }

    /*Get User Ships Method*/
    public Ship[] getUserShips() {
        return userShips;
    }

    /*Get Ships Sizes Method*/
    public int[] getShipsSizes() {
        return this.shipsSizes;
    }

    /*Get Ship By ID Method*/
    public Ship getShipById(int id, Ship[] ships) {
        for (Ship ship : ships) {
            if (id == ship.getId()) {
                return ship;
            }
        }
        return null;
    }

    /*Get User Board Method*/
    public Cell[][] getUserBoard() {
        return userBoard;
    }
}
