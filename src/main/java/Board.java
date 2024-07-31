import java.util.Random;

public class Board {
    public final char[][] board;
    private final int width = 42;
    private final int height = 45;
    private int blackLost = 0;
    private int whiteLost = 0;

    public Board() {
        board = new char[height][width];
        initializeBoard();
    }

    // 바둑판 초기화
    private void initializeBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = '.';
            }
        }
        // 흑돌 초기 위치 배치
        for (int i = 3; i <= 11; i += 2) {
            board[3][i] = 'B';
        }
        // 백돌 초기 위치 배치
        for (int i = 3; i <= 11; i += 2) {
            board[15][i] = 'W';
        }
    }

    // 바둑판 표시
    public void displayBoard() {
        clearConsole();
        // 열 헤더 표시 (A-T)
        System.out.print("  ");
        for (char c = 'A'; c <= 'T'; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        // 행 헤더와 함께 바둑판 표시 (1-19)
        for (int i = 0; i < 19; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 20; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        // 잃은 돌 표시
        System.out.println("흑이 잃은 돌: " + blackLost);
        System.out.println("백이 잃은 돌: " + whiteLost);
    }

    // 콘솔 창을 지우는 메서드
    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 알을 이동시키는 메서드
    public boolean movePiece(int startX, int startY, int targetX, int targetY, int strength, boolean isBlackTurn) {
        char pieceType = isBlackTurn ? 'B' : 'W';

        if (startX < 0 || startX >= height || startY < 0 || startY >= width ||
                targetX < 0 || targetX >= height || targetY < 0 || targetY >= width ||
                board[startX][startY] != pieceType) {
            return false; // 잘못된 이동
        }

        // 목표 위치와 강도에 무작위 오차 적용
        Random random = new Random();
        targetX += random.nextInt(5) - 2; // -2, -1, 0, +1, +2
        targetY += random.nextInt(5) - 2; // -2, -1, 0, +1, +2
        strength += random.nextInt(7) - 3; // -3, -2, -1, 0, +1, +2, +3

        targetX = Math.max(0, Math.min(height - 1, targetX));
        targetY = Math.max(0, Math.min(width - 1, targetY));

        // 이동 애니메이션
        int deltaX = targetX - startX;
        int deltaY = targetY - startY;
        int steps = Math.max(Math.abs(deltaX), Math.abs(deltaY));

        int currentX = startX;
        int currentY = startY;
        for (int i = 1; i <= steps; i++) {
            int newX = startX + (deltaX * i) / steps;
            int newY = startY + (deltaY * i) / steps;
            board[currentX][currentY] = '.';
            board[newX][newY] = pieceType;
            displayBoard();
            try {
                Thread.sleep(50); // 짧은 지연으로 애니메이션 효과
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentX = newX;
            currentY = newY;
        }

        // 최종 이동 위치
        if (board[targetX][targetY] == '.') {
            board[targetX][targetY] = pieceType;
            board[startX][startY] = '.';
            return true;
        } else {
            return handleCollision(startX, startY, targetX, targetY, deltaX, deltaY, pieceType);
        }
    }

    // 충돌 처리 메서드
    private boolean handleCollision(int startX, int startY, int targetX, int targetY, int deltaX, int deltaY, char pieceType) {
        char opponentType = (pieceType == 'B') ? 'W' : 'B';

        if (board[targetX][targetY] == opponentType) {
            int bounceX = targetX + deltaX;
            int bounceY = targetY + deltaY;
            bounceX = Math.max(0, Math.min(height - 1, bounceX));
            bounceY = Math.max(0, Math.min(width - 1, bounceY));

            if (board[bounceX][bounceY] == '.') {
                board[bounceX][bounceY] = opponentType;
                board[targetX][targetY] = pieceType;
                board[startX][startY] = '.';
                displayBoard();
                checkOutOfBounds(bounceX, bounceY, opponentType);
                System.out.println("공격 성공!");
                return true;
            } else {
                board[targetX][targetY] = pieceType;
                board[startX][startY] = '.';
                displayBoard();
                System.out.println("공격 성공!");
                return true;
            }
        } else {
            return false;
        }
    }

    // 바둑판 밖으로 튕겨 나간 돌 처리
    private void checkOutOfBounds(int x, int y, char pieceType) {
        if (x < 0 || x >= height || y < 0 || y >= width) {
            if (pieceType == 'B') {
                blackLost++;
            } else {
                whiteLost++;
            }
        }
    }
}
