package com.bitcamp.stoneGame.vo;

import java.io.Serializable;

public class Player implements Serializable {

  private String name;
  private int playerNum;
  private boolean turn = false;

  public Player(String name) {
    this.name = name;
  }

  public boolean isTurn() {
    return turn;
  }

  public void setTurn(boolean turn) {
    this.turn = turn;
  }

  public int getPlayerNum() {
    return playerNum;
  }

  public void setPlayerNum(int playerNum) {
    this.playerNum = playerNum;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
