package com.bitcamp.stoneGame;


import java.awt.Color;
import java.awt.Graphics;

public class StoneBoard {
    public static class Stone {

        public int x, y;
        public Color color;
        public int radius = 20;
        public double vx = 0, vy = 0; // 속도

        public Stone(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
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
    }

}
