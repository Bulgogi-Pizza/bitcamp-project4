package com.bitcamp.stoneGame;

import com.bitcamp.stoneGame.vo.Player;
import javax.swing.JFrame;

public class GoBoardWithPhysics extends JFrame {

  BoardPanel boardPanel;
  private Player player;

  public GoBoardWithPhysics(Player player) {
    this.player = player;
    this.boardPanel = new BoardPanel(player);
    setTitle("Go Board with Physics");
    setSize(850, 880);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(boardPanel);
    setVisible(true);
  }

  public BoardPanel getBoardPanel() {
    return boardPanel;
  }


}
