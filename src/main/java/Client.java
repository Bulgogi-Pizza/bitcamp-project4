import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            out.flush();

            System.out.println("서버에 연결되었습니다.");

            while (true) {
                System.out.print("돌의 위치를 입력하세요 (예: D4): ");
                String position = scanner.nextLine().toUpperCase();
                System.out.print("목표 위치를 입력하세요 (예: H8): ");
                String targetPosition = scanner.nextLine().toUpperCase();
                System.out.print("강도를 입력하세요 (1-19): ");
                int strength = scanner.nextInt();
                scanner.nextLine(); // 개행 문자 처리

                String command = position + " " + targetPosition + " STRENGTH:" + strength;
                out.println(command);
                String response = in.readLine();
                System.out.println("서버 응답: " + response);

                if (command.equalsIgnoreCase("exit")) {
                    break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
