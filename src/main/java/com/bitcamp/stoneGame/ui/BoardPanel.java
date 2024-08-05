package com.bitcamp.stoneGame.ui;

import com.bitcamp.stoneGame.logic.StoneInitializer;
import com.bitcamp.stoneGame.logic.StonePhysics;
import com.bitcamp.stoneGame.vo.Player;
import com.bitcamp.stoneGame.vo.Stone;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener,
    Serializable {
  private static final long serialVersionUID = 1L;

  public final int boardSize = 19;
  public final int cellSize = 42;
  public final int boardWidth = cellSize * (boardSize - 1);
  public final int startX = 50;
  public final int boardHeight = cellSize * (boardSize - 1);
  public final int startY = 50;

  private List<Stone> stones;
  private List<Stone> stonesAction;
  private Stone selectedStone = null;
  private int initialX, initialY;
  private int mouseX, mouseY;
  private Player player;
  private boolean isSaveAction;
  public boolean isDone;
  private Timer timer;

  public BoardPanel(Player player) {
    StoneInitializer initializer = new StoneInitializer();
    this.stones = initializer.initializeStones(startX, startY, cellSize);
    stonesAction = new ArrayList<>();
    addMouseListener(this);
    addMouseMotionListener(this);
    timer = new Timer(7, this);
    timer.start();
    this.player = player;
  }

  public synchronized List<Stone> getAction() {
    isSaveAction = false;
    return stonesAction;
  }

  public synchronized void setStones(List<Stone> stones) {
    this.stones = stones;
    repaint();
  }

  private int getXFromColumn(char column) {
    return startX + (column - 'A') * cellSize;
  }

  private int getYFromRow(int row) {
    return startY + (19 - row) * cellSize;
  }

  public synchronized void setDone(boolean done) {
    this.isDone = done;
    notifyAll();  // 알림
  }

  public synchronized boolean isDone() {
    return this.isDone;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (int i = 0; i < boardSize; i++) {
      g.drawLine(startX, startY + i * cellSize, startX + (boardSize - 1) * cellSize, startY + i * cellSize);
      g.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + (boardSize - 1) * cellSize);
    }

    for (int i = 0; i < boardSize; i++) {
      g.drawString(String.valueOf(boardSize - i), startX - 20, startY + i * cellSize + 5);
    }

    for (int i = 0; i < boardSize; i++) {
      g.drawString(String.valueOf((char) ('A' + i)), startX + i * cellSize - 5, startY - 10);
    }

    for (Stone stone : stones) {
      if (stone.isVisible()) {
        stone.draw(g);
      }
    }

    if (selectedStone != null) {
      g.setColor(Color.RED);
      g.drawLine(selectedStone.getX(), selectedStone.getY(), mouseX, mouseY);
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    for (Stone stone : stones) {
      if (stone.getPlayerNum() == player.getPlayerNum() && player.isTurn()) {
        if (Math.sqrt(Math.pow(e.getX() - stone.getX(), 2) + Math.pow(e.getY() - stone.getY(), 2)) <= stone.getRadius()) {
          selectedStone = stone;
          initialX = stone.getX();
          initialY = stone.getY();
          mouseX = e.getX();
          mouseY = e.getY();
          break;
        }
      }
    }
  }

  // BoardPanel 클래스의 mouseReleased 메서드 수정
  @Override
  public void mouseReleased(MouseEvent e) {
    if (selectedStone != null) {
      int dx = initialX - e.getX();
      int dy = initialY - e.getY();
      selectedStone.setVx(dx * 0.1);
      selectedStone.setVy(dy * 0.1);
      selectedStone = null;

      if (!isSaveAction) {
        stonesAction = new ArrayList<>();
        stonesAction.addAll(stones);
        isSaveAction = true;
      }
      isDone = true;

      synchronized (this) {
        notifyAll();  // 알림
      }
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {
    if (selectedStone != null) {
      mouseX = e.getX();
      mouseY = e.getY();
      repaint();
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void actionPerformed(ActionEvent e) {
    synchronized (this) {
      List<Stone> stonesToRemove = new ArrayList<>();
      for (Stone stone : stones) {
        stone.move();
        if (stone.getX() < startX || stone.getX() > startX + boardWidth || stone.getY() < startY || stone.getY() > startY + boardHeight) {
          stone.setVisible(false);
          stonesToRemove.add(stone);
        }
      }
      stones.removeAll(stonesToRemove);
      StonePhysics.detectCollisions(stones);
    }
    repaint();
  }

  public synchronized void updatePlayer(Player player) {
    this.player = player;
  }
}
