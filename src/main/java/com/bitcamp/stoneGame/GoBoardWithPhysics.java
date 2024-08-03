package com.bitcamp.stoneGame;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GoBoardWithPhysics extends JFrame {

    public GoBoardWithPhysics() {
        setTitle("Go Board with Physics");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new BoardPanel());
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GoBoardWithPhysics::new);
    }


}
