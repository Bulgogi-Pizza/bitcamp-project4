import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GoBoardWithPhysics extends JFrame {

    public GoBoardWithPhysics() {
        setTitle("Go Board with Physics");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new BoardPanel());
        setVisible(true);
    }

    class Stone {
        int x, y;
        Color color;
        int radius = 20;
        double vx = 0, vy = 0; // 속도

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
            if (Math.abs(vx) < 0.01) vx = 0;
            if (Math.abs(vy) < 0.01) vy = 0;
        }
    }

    class BoardPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
        private final int boardSize = 19;
        private final int cellSize = 42; // 각 셀의 크기
        private final int paddingTopBottom = 45;   // 바둑판 위아래 공백
        private final int paddingLeftRight = 300;   // 바둑판 좌우 공백
        private final int boardWidth = cellSize * (boardSize - 1);
        private final int boardHeight = cellSize * (boardSize - 1);
        private final int startX = (1920 - boardWidth) / 2;   // 바둑판 시작 X 좌표
        private final int startY = (1080 - boardHeight) / 2;  // 바둑판 시작 Y 좌표

        private List<Stone> stones;
        private Stone selectedStone = null;
        private int initialX, initialY;
        private int mouseX, mouseY;

        private Timer timer;

        public BoardPanel() {
            stones = new ArrayList<>();
            initializeStones();
            addMouseListener(this);
            addMouseMotionListener(this);
            timer = new Timer(7, this); // 144 FPS로 업데이트
            timer.start();
        }

        private void initializeStones() {
            // 흑색 원 (D16, G16, J16, M16, P16)
            stones.add(new Stone(getXFromColumn('D'), getYFromRow(16), Color.BLACK));
            stones.add(new Stone(getXFromColumn('G'), getYFromRow(16), Color.BLACK));
            stones.add(new Stone(getXFromColumn('J'), getYFromRow(16), Color.BLACK));
            stones.add(new Stone(getXFromColumn('M'), getYFromRow(16), Color.BLACK));
            stones.add(new Stone(getXFromColumn('P'), getYFromRow(16), Color.BLACK));

            // 백색 원 (D4, G4, J4, M4, P4)
            stones.add(new Stone(getXFromColumn('D'), getYFromRow(4), Color.WHITE));
            stones.add(new Stone(getXFromColumn('G'), getYFromRow(4), Color.WHITE));
            stones.add(new Stone(getXFromColumn('J'), getYFromRow(4), Color.WHITE));
            stones.add(new Stone(getXFromColumn('M'), getYFromRow(4), Color.WHITE));
            stones.add(new Stone(getXFromColumn('P'), getYFromRow(4), Color.WHITE));
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
                g.drawLine(startX, startY + i * cellSize, startX + (boardSize - 1) * cellSize, startY + i * cellSize);
                g.drawLine(startX + i * cellSize, startY, startX + i * cellSize, startY + (boardSize - 1) * cellSize);
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
                g.drawLine(selectedStone.x, selectedStone.y, mouseX, mouseY);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            for (Stone stone : stones) {
                if (Math.sqrt(Math.pow(e.getX() - stone.x, 2) + Math.pow(e.getY() - stone.y, 2)) <= stone.radius) {
                    selectedStone = stone;
                    initialX = stone.x;
                    initialY = stone.y;
                    mouseX = e.getX();
                    mouseY = e.getY();
                    break;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedStone != null) {
                int dx = initialX - e.getX();
                int dy = initialY - e.getY();
                selectedStone.vx = dx * 0.1;
                selectedStone.vy = dy * 0.1;
                selectedStone = null;
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
                    double distance = Math.sqrt(Math.pow(s1.x - s2.x, 2) + Math.pow(s1.y - s2.y, 2));
                    if (distance <= s1.radius + s2.radius) {
                        // 충돌 감지
                        double angle = Math.atan2(s2.y - s1.y, s2.x - s1.x);
                        double sin = Math.sin(angle);
                        double cos = Math.cos(angle);

                        // 회전 변환
                        double x1 = 0;
                        double y1 = 0;
                        double x2 = distance * cos;
                        double y2 = distance * sin;

                        // 속도 회전 변환
                        double vx1 = s1.vx * cos + s1.vy * sin;
                        double vy1 = s1.vy * cos - s1.vx * sin;
                        double vx2 = s2.vx * cos + s2.vy * sin;
                        double vy2 = s2.vy * cos - s2.vx * sin;

                        // 충돌 후 속도
                        double newVx1 = vx2;
                        double newVx2 = vx1;

                        // 회전 변환 후 역변환
                        s1.vx = cos * newVx1 - sin * vy1;
                        s1.vy = sin * newVx1 + cos * vy1;
                        s2.vx = cos * newVx2 - sin * vy2;
                        s2.vy = sin * newVx2 + cos * vy2;

                        // 위치 수정
                        double overlap = s1.radius + s2.radius - distance;
                        double correctionFactor = overlap / 2;
                        s1.x -= correctionFactor * cos;
                        s1.y -= correctionFactor * sin;
                        s2.x += correctionFactor * cos;
                        s2.y += correctionFactor * sin;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GoBoardWithPhysics::new);
    }
}
