package com.bitcamp.stoneGame.vo;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Stone implements Serializable {

  int x, y;
  Color color;
  int radius = 20;
  double vx = 0, vy = 0; // 속도
  int playerNum;
  boolean visible;

  public Stone(int x, int y, Color color, int playerNum) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.playerNum = playerNum;
    this.visible = true;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public int getPlayerNum() {
    return playerNum;
  }

  public void setPlayerNum(int playerNum) {
    this.playerNum = playerNum;
  }

  public void draw(Graphics g) {
    g.setColor(color);
    g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    g.setColor(Color.BLACK);
    g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius);
  }

  public void move() {
    x += vx;
    y += vy;

    // 마찰로 인해 속도 감소
    vx *= 0.98;
    vy *= 0.98;

    // 속도가 일정 이하가 되면 멈춤
    if (Math.abs(vx) < 0.01) {
      vx = 0;
    }
    if (Math.abs(vy) < 0.01) {
      vy = 0;
    }
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public double getVx() {
    return vx;
  }

  public void setVx(double vx) {
    this.vx = vx;
  }

  public double getVy() {
    return vy;
  }

  public void setVy(double vy) {
    this.vy = vy;
  }

}