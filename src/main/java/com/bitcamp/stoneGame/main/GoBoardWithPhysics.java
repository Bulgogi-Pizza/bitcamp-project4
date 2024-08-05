package com.bitcamp.stoneGame.main;

import com.bitcamp.stoneGame.ui.BoardPanel;
import com.bitcamp.stoneGame.vo.Player;
import javax.swing.JFrame;

public class GoBoardWithPhysics extends JFrame {

  private BoardPanel boardPanel;

  public GoBoardWithPhysics(Player player) {
    this.boardPanel = new BoardPanel(player);
    setTitle("Go Board with Physics");
    setSize(1050, 880);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(boardPanel);
    setVisible(true);
  }

  public BoardPanel getBoardPanel() {
    return boardPanel;
  }
}
