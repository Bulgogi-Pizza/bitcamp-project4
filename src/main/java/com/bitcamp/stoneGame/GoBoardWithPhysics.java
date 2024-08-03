package com.bitcamp.stoneGame;

import com.bitcamp.stoneGame.vo.Player;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GoBoardWithPhysics extends JFrame {

    private Player player;
    BoardPanel boardPanel;

    public GoBoardWithPhysics(Player player) {
        this.player = player;
        this.boardPanel = new BoardPanel(player);
        setTitle("Go Board with Physics");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(boardPanel);
        setVisible(true);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }


}
