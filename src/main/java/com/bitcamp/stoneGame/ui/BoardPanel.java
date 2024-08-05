package com.bitcamp.stoneGame.ui;

import com.bitcamp.stoneGame.logic.StoneInitializer;
import com.bitcamp.stoneGame.logic.StonePhysics;
import com.bitcamp.stoneGame.vo.Player;
import com.bitcamp.stoneGame.vo.Stone;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener,
    ActionListener,
    Serializable {

  public static final double MAX_VELOCITY = 20.0;
  private static final long serialVersionUID = 1L;

  public final int boardSize = 19;
  public final int cellSize = 42;
  public final int boardWidth = cellSize * (boardSize - 1);
  public final int startX = 50;
  public final int boardHeight = cellSize * (boardSize - 1);
  public final int startY = 50;
  public boolean isDone;
  private List<Stone> stones;
  private List<Stone> stonesAction;
  private Stone selectedStone = null;
  private int initialX, initialY;
  private int mouseX, mouseY;
  private Player player;
  private boolean isSaveAction;
  private Timer timer;
  private boolean isDefeatPlayer1;
  private boolean isDefeatPlayer2;

  private JLabel turnLabel; // 추가한 코드: 현재 턴 플레이어 출력
  private JLabel stoneCountLabel1;
  private JLabel stoneCountLabel2;
  private JLabel scoreLabel;
  private JProgressBar powerGauge;

  private int player1Score;
  private int player2Score;

  public BoardPanel(Player player) {
    StoneInitializer initializer = new StoneInitializer();
    this.stones = initializer.initializeStones(startX, startY, cellSize);
    stonesAction = new ArrayList<>();
    addMouseListener(this);
    addMouseMotionListener(this);
    timer = new Timer(7, this);
    timer.start();
    this.player = player;

    turnLabel = new JLabel(); // 추가한 코드: 현재 턴 플레이어 출력
    stoneCountLabel1 = new JLabel();
    stoneCountLabel2 = new JLabel();
    scoreLabel = new JLabel();
    turnLabel.setHorizontalAlignment(SwingConstants.CENTER); // 추가한 코드: 현재 턴 플레이어 출력
    stoneCountLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    stoneCountLabel2.setHorizontalAlignment(SwingConstants.CENTER);
    scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // 폰트 크기 조정
    turnLabel.setFont(new Font("Gothic", Font.BOLD, 14)); // 추가한 코드: 현재 턴 플레이어 출력
    stoneCountLabel1.setFont(new Font("Gothic", Font.BOLD, 14));
    stoneCountLabel2.setFont(new Font("Gothic", Font.BOLD, 14));
    scoreLabel.setFont(new Font("Gothic", Font.BOLD, 14));

    powerGauge = new JProgressBar(SwingConstants.VERTICAL); // 추가한 코드: 세기 게이지를 아래에서 위로 올라가는 식으로 수정
    powerGauge.setPreferredSize(new Dimension(50, 200)); // 크기 조정
    powerGauge.setStringPainted(true);

    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new GridLayout(5, 1)); // 추가한 코드: 현재 턴 플레이어 출력 추가로 인해 4행으로 변경
    infoPanel.add(turnLabel); // 추가한 코드: 현재 턴 플레이어 출력
    infoPanel.add(stoneCountLabel1);
    infoPanel.add(stoneCountLabel2);
    infoPanel.add(scoreLabel);
    infoPanel.add(powerGauge);

    setLayout(null); // Absolute Layout 사용
    infoPanel.setBounds(boardWidth + 60, startY, 100, boardHeight); // 위치 조정
    add(infoPanel);

    updateStoneCount();
  }

  private void updateStoneCount() {
    int blackCount = 0;
    int whiteCount = 0;
    for (Stone stone : stones) {
      if (stone.getPlayerNum() == 1) {
        blackCount++;
      } else if (stone.getPlayerNum() == 2) {
        whiteCount++;
      }
    }

    stoneCountLabel1.setText("백돌: " + whiteCount);
    stoneCountLabel2.setText("흑돌: " + blackCount);
    scoreLabel.setText(
        String.format(player.getName()));
    turnLabel.setText(player.isTurn() ? "현재 턴: " + (player.getPlayerNum() == 1 ? "흑" : "백")
        : ""); // 추가한 코드: 현재 턴 플레이어 출력
  }

  private void updatePowerGauge(double power) {
    powerGauge.setValue((int) (power / MAX_VELOCITY * 100));
  }

  public boolean isDefeatPlayer1() {
    return isDefeatPlayer1;
  }

  public boolean isDefeatPlayer2() {
    return isDefeatPlayer2;
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

  public synchronized boolean isDone() {
    return this.isDone;
  }

  public synchronized void setDone(boolean done) {
    this.isDone = done;
    notifyAll();  // 알림
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (int i = 0; i < boardSize; i++) {
      g.drawLine(startX, startY + i * cellSize, startX + (boardSize - 1) * cellSize,
          startY + i * cellSize);
      g.drawLine(startX + i * cellSize, startY, startX + i * cellSize,
          startY + (boardSize - 1) * cellSize);
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
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
    for (Stone stone : stones) {
      if (stone.getPlayerNum() == player.getPlayerNum() && player.isTurn() && stone.isVisible()) {
        if (Math.sqrt(Math.pow(e.getX() - stone.getX(), 2) + Math.pow(e.getY() - stone.getY(), 2))
            <= stone.getRadius()) {
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
      double vx = dx * 0.1;
      double vy = dy * 0.1;
      double power = Math.sqrt(vx * vx + vy * vy);

      if (power > MAX_VELOCITY) {
        double scale = MAX_VELOCITY / power;
        vx *= scale;
        vy *= scale;
      }

      selectedStone.setVx(vx);
      selectedStone.setVy(vy);
      selectedStone = null;

      if (!isSaveAction) {
        stonesAction = new ArrayList<>();
        stonesAction.addAll(stones);
        isSaveAction = true;
      }
      updateStoneCount();
      updatePowerGauge(power);
      setDone(true);

      synchronized (this) {
        notifyAll();  // 알림
      }


    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (selectedStone != null) {
      mouseX = e.getX();
      mouseY = e.getY();
      repaint();
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    synchronized (this) {
      List<Stone> stonesToRemove = new ArrayList<>();
      for (Stone stone : stones) {
        stone.move();
        if (stone.getX() < startX || stone.getX() > startX + boardWidth || stone.getY() < startY
            || stone.getY() > startY + boardHeight) {
          stone.setVisible(false);
          stonesToRemove.add(stone);
        }
      }
      stones.removeAll(stonesToRemove);

      int numOf1 = 0;
      int numOf2 = 0;
      for (Stone stone : stones) {
        if (stone.getPlayerNum() == 1 && stone.isVisible()) {
          ++numOf1;
        } else if (stone.getPlayerNum() == 2 && stone.isVisible()) {
          ++numOf2;
        }
      }

      player1Score = 5 - numOf2;
      player2Score = 5 - numOf1;

      if (numOf1 == 0) {
        isDefeatPlayer1 = true;
        setDone(true);
      }
      if (numOf2 == 0) {
        isDefeatPlayer2 = true;
        setDone(true);
      }
      StonePhysics.detectCollisions(stones);
    }
    repaint();
  }

  public synchronized void updatePlayer(Player player) {
    this.player = player;
    updateStoneCount();
  }
}
