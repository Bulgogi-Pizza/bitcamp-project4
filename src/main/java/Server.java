import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 8888;
    private static Board board = new Board();
    private static boolean isBlackTurn = true;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("서버가 실행 중입니다...");
            board.displayBoard();
            System.out.println("흑 선공");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("클라이언트가 연결되었습니다.");

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    String input;
                    while ((input = in.readLine()) != null) {
                        System.out.println("클라이언트로부터 받은 메시지: " + input);
                        handleCommand(input, out);
                        board.displayBoard();

                        out.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 클라이언트로부터 받은 명령을 처리하는 메서드
    private static void handleCommand(String command, PrintWriter out) {
        String[] parts = command.split(" ");
        String pieceType = isBlackTurn ? "B" : "W";

        // 첫 번째 부분은 좌표 (예: D4)
        String position = parts[0];
        int startY = position.charAt(0) - 'A'; // 문자를 인덱스로 변환
        int startX = Integer.parseInt(position.substring(1)) - 1;

        if (board.board[startX][startY] != pieceType.charAt(0)) {
            out.println("상대편 돌입니다. 다시 입력하세요.");
            return;
        }

        // 두 번째 부분은 목표 좌표 (예: H8)
        String targetPosition = parts[1];
        int targetY = targetPosition.charAt(0) - 'A'; // 문자를 인덱스로 변환
        int targetX = Integer.parseInt(targetPosition.substring(1)) - 1;

        // 세 번째 부분은 강도
        int strength = Integer.parseInt(parts[2].split(":")[1]);

        if (board.movePiece(startX, startY, targetX, targetY, strength, isBlackTurn)) {
            out.println("이동 성공");
            isBlackTurn = !isBlackTurn; // 턴 전환
            out.println(isBlackTurn ? "흑 차례" : "백 차례");
        } else {
            out.println("이동 실패: 잘못된 목표 위치.");
        }
    }
}
