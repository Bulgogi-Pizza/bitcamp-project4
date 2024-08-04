package com.bitcamp.stoneGame.logic;

import com.bitcamp.stoneGame.vo.Stone;
import java.util.List;

public class StonePhysics {

    public static void detectCollisions(List<Stone> stones) {
        for (int i = 0; i < stones.size(); i++) {
            Stone s1 = stones.get(i);
            for (int j = i + 1; j < stones.size(); j++) {
                Stone s2 = stones.get(j);
                double distance = Math.sqrt(Math.pow(s1.getX() - s2.getX(), 2) + Math.pow(s1.getY() - s2.getY(), 2));
                if (distance <= s1.getRadius() + s2.getRadius()) {
                    double angle = Math.atan2(s2.getY() - s1.getY(), s2.getX() - s1.getX());
                    double sin = Math.sin(angle);
                    double cos = Math.cos(angle);

                    double x1 = 0;
                    double y1 = 0;
                    double x2 = distance * cos;
                    double y2 = distance * sin;

                    double vx1 = s1.getVx() * cos + s1.getVy() * sin;
                    double vy1 = s1.getVy() * cos - s1.getVx() * sin;
                    double vx2 = s2.getVx() * cos + s2.getVy() * sin;
                    double vy2 = s2.getVy() * cos - s2.getVx() * sin;

                    double newVx1 = vx2;
                    double newVx2 = vx1;

                    s1.setVx(cos * newVx1 - sin * vy1);
                    s1.setVy(sin * newVx1 + cos * vy1);
                    s2.setVx(cos * newVx2 - sin * vy2);
                    s2.setVy(sin * newVx2 + cos * vy2);

                    double overlap = s1.getRadius() + s2.getRadius() - distance;
                    double correctionFactor = overlap / 2;
                    s1.setX((int) (s1.getX() - correctionFactor * cos));
                    s1.setY((int) (s1.getY() - correctionFactor * sin));
                    s2.setX((int) (s2.getX() + correctionFactor * cos));
                    s2.setY((int) (s2.getY() + correctionFactor * sin));
                }
            }
        }
    }
}
