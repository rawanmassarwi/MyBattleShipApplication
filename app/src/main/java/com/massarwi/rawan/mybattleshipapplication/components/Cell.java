package com.massarwi.rawan.mybattleshipapplication.components;

import android.view.View;
import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;

/*  Cell Class  */
public class Cell extends AppCompatImageButton implements View.OnClickListener {

    //Setting Variables
    private int row;
    private int col;
    private int shipId;
    private boolean isHit;
    //private boolean enable;
    private boolean isShip;
    private boolean besideShip;
    private CellListener listener;

    /*Constractor*/
    public Cell(Context context) {
        super(context);
        setOnClickListener(this);
        this.isHit = false;
        this.isShip = false;
        this.shipId = -1;
        besideShip = false;
    }

    @Override
    public void onClick(View v) {
        listener.buttonClicked(this);
    }

    /*Is Enable Method*//*
    public boolean isEnable() {
        return enable;
    }
*/
    /*Is Ship Method*/
    public boolean isShip() {
        return isShip;
    }

    /*Is BesideShip Method*/
    public boolean isBesideShip() {
        return besideShip;
    }

    /*Is Hit Method*/
    public boolean isHit() {
        return isHit;
    }

    /*Get Row Method*/
    public int getRow() {
        return row;
    }

    /*Get Col Method*/
    public int getCol() {
        return col;
    }

    /*Get Ship ID Method*/
    public int getShipId() {
        return shipId;
    }

    /*Set Listener Method*/
    public void setListener(CellListener listener) {
        this.listener = listener;
    }

    /*Set Position Method*/
    public void setPosition(int col, int row) {
        this.col = col;
        this.row = row;
    }

    /*Set Hit Method*/
    public void setHit(boolean hit) {
        this.isHit = hit;
    }

    /*Set Ship ID Method*/
    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    /*Set Ship Method*/
    public void setShip(boolean ship) {
        this.isShip = ship;
    }
}