package com.massarwi.rawan.mybattleshipapplication.util;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.graphics.drawable.ColorDrawable;
import com.massarwi.rawan.mybattleshipapplication.R;
import com.massarwi.rawan.mybattleshipapplication.activities.GameActivity;

/*Turn Class*/
public class Turn extends Thread implements Runnable {

    //Setting Variables
    Handler handler;
    GameActivity game;

    /*Constractor*/
    public Turn(Handler handler, GameActivity game) {
        this.handler = handler;
        this.game = game;
    }
    /*Run Method*/
    public void run() {
        int row = (int) (Math.random() * game.cont.getBoardSize());
        int col = (int) (Math.random() * game.cont.getBoardSize());
        while (game.cont.getUserBoard()[col][row].isHit()){
             row = (int) (Math.random() * game.cont.getBoardSize());
             col = (int) (Math.random() * game.cont.getBoardSize());
        }
            game.cont.getUserBoard()[col][row].setHit(true);
            if (game.cont.getUserBoard()[col][row].isShip()) {
                game.cont.getUserBoard()[col][row].setBackgroundResource(R.drawable.boom);
                Ship hitShip = game.cont.getShipById(game.cont.getUserBoard()[col][row].getShipId(), game.cont.getUserShips());
                hitShip.setNumOfHits();
                if (hitShip.isSink()) {
                    game.NumOfShipsRemainingMinusOne();
                    game.getRemainingShips().setText(Integer.toString(game.getNumOfRemainingShips()));
                    if (game.getNumOfRemainingShips() == 0) {
                        game.getRoles().lose(game);
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
                        if(game.getNumOfRemainingShips() !=0){
                            ImageView imageView = new ImageView(game);
                            handler = new Handler();

                            imageView.setImageResource(R.drawable.sad_smiley);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    builder.dismiss();
                                }
                            },4000);

                            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        }
                             builder.setCancelable(true);
                    if(!game.isFinishing()) {
                        builder.show();
                    }
                    }
            } else {
                game.cont.getUserBoard()[col][row].setBackgroundResource(R.drawable.incorrect);
            }

        game.getTurn().setText(R.string.userTurn);
        game.getTurn().setTextColor(Color.GREEN);
        game.setAllButtons(true);

    }
}

