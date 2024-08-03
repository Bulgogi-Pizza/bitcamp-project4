package com.bitcamp.stoneGame;

import com.bitcamp.stoneGame.vo.Player;
import com.bitcamp.stoneGame.vo.Stone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;


public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

    public final int boardSize = 19;
    public final int cellSize = 42; // 각 셀의 크기
    public final int paddingTopBottom = 45;   // 바둑판 위아래 공백
    public final int paddingLeftRight = 300;   // 바둑판 좌우 공백
    public final int boardWidth = cellSize * (boardSize - 1);
    public final int startX = (1920 - boardWidth) / 2;   // 바둑판 시작 X 좌표
    public final int boardHeight = cellSize * (boardSize - 1);
    public final int startY = (1080 - boardHeight) / 2;  // 바둑판 시작 Y 좌표

    public List<Stone> stones;
    public Stone selectedStone = null;
    public int initialX, initialY;
    public int mouseX, mouseY;
    public Player player;

    public Timer timer;
    public int turn = 0;

    public BoardPanel(Player player) {
        stones = new ArrayList<>();
        initializeStones();
        addMouseListener(this);
        addMouseMotionListener(this);
        timer = new Timer(7, this); // 144 FPS로 업데이트
        timer.start();
        this.player = player;
    }

    private void initializeStones() {

        // 흑색 원 (D16, G16, J16, M16, P16), Player number 1로 지정
        stones.add(new Stone(getXFromColumn('D'), getYFromRow(16), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('G'), getYFromRow(16), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('J'), getYFromRow(16), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('M'), getYFromRow(16), Color.BLACK, 1));
        stones.add(new Stone(getXFromColumn('P'), getYFromRow(16), Color.BLACK, 1));

        // 백색 원 (D4, G4, J4, M4, P4), Player number 2로 지정
        stones.add(new Stone(getXFromColumn('D'), getYFromRow(4), Color.WHITE, 2));
        stones.add(new Stone(getXFromColumn('G'), getYFromRow(4), Color.WHITE, 2));
        stones.add(new Stone(getXFromColumn('J'), getYFromRow(4), Color.WHITE, 2));
        stones.add(new Stone(getXFromColumn('M'), getYFromRow(4), Color.WHITE, 2));
        stones.add(new Stone(getXFromColumn('P'), getYFromRow(4), Color.WHITE, 2));
    }

    private int getXFromColumn(char column) {
        return startX + (column - 'A') * cellSize;
    }

    private int getYFromRow(int row) {
        return startY + (19 - row) * cellSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 바둑판 그리기
        for (int i = 0; i < boardSize; i++) {
            g.drawLine(startX, startY + i * cellSize, startX + (boardSize - 1) * cellSize,
                    startY + i * cellSize);
            g.drawLine(startX + i * cellSize, startY, startX + i * cellSize,
                    startY + (boardSize - 1) * cellSize);
        }

        // 왼쪽에 1부터 19까지 숫자 표시
        for (int i = 0; i < boardSize; i++) {
            g.drawString(String.valueOf(boardSize - i), startX - 20, startY + i * cellSize + 5);
        }

        // 상단에 A부터 T까지 알파벳 표시
        for (int i = 0; i < boardSize; i++) {
            g.drawString(String.valueOf((char) ('A' + i)), startX + i * cellSize - 5, startY - 10);
        }

        // 바둑알 그리기
        for (Stone stone : stones) {
            stone.draw(g);
        }

        // 드래그 라인 그리기
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
            System.out.println("for debugging" + player.isTurn() + player.getPlayerNum());
            System.out.println(stone.getPlayerNum());
            System.out.println(player.getPlayerNum());
            System.out.println(turn);
            if (stone.getPlayerNum() == player.getPlayerNum() && player.isTurn()) {
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

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedStone != null) {
            int dx = initialX - e.getX();
            int dy = initialY - e.getY();
            selectedStone.setVx(dx * 0.1);
            selectedStone.setVy(dy * 0.1);
            selectedStone = null;
            ++turn;
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
        for (Stone stone : stones) {
            stone.move();
        }
        detectCollisions();
        repaint();
    }

    private void detectCollisions() {
        for (int i = 0; i < stones.size(); i++) {
            Stone s1 = stones.get(i);
            for (int j = i + 1; j < stones.size(); j++) {
                Stone s2 = stones.get(j);
                double distance = Math.sqrt(
                        Math.pow(s1.getX() - s2.getX(), 2) + Math.pow(s1.getY() - s2.getY(), 2));
                if (distance <= s1.getRadius() + s2.getRadius()) {
                    // 충돌 감지
                    double angle = Math.atan2(s2.getY() - s1.getY(), s2.getX() - s1.getX());
                    double sin = Math.sin(angle);
                    double cos = Math.cos(angle);

                    // 회전 변환
                    double x1 = 0;
                    double y1 = 0;
                    double x2 = distance * cos;
                    double y2 = distance * sin;

                    // 속도 회전 변환
                    double vx1 = s1.getVx() * cos + s1.getVy() * sin;
                    double vy1 = s1.getVy() * cos - s1.getVx() * sin;
                    double vx2 = s2.getVx() * cos + s2.getVy() * sin;
                    double vy2 = s2.getVy() * cos - s2.getVx() * sin;

                    // 충돌 후 속도
                    double newVx1 = vx2;
                    double newVx2 = vx1;

                    // 회전 변환 후 역변환
                    s1.setVx(cos * newVx1 - sin * vy1);
                    s1.setVy(sin * newVx1 + cos * vy1);
                    s2.setVx(cos * newVx2 - sin * vy2);
                    s2.setVy(sin * newVx2 + cos * vy2);

                    // 위치 수정
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

    public int getTurn() {
        return turn;
    }

    public void updatePlayer(Player player) {
        this.player = player;
    }
}

