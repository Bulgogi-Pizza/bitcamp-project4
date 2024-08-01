package com.bitcamp.stoneGame;

import com.bitcamp.stoneGame.handler.ClientHandler;
import com.bitcamp.stoneGame.vo.Player;
import com.bitcamp.util.Ansi;
import com.bitcamp.util.Print;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  Player player1;
  Player player2;
  Print print = new Print();
  ClientHandler clientHandler1;
  ClientHandler clientHandler2;

  public Server() {
  }

  public static void main(String[] args) {
    Server server = new Server();

    server.execute();
  }

  public void execute() {
    try (ServerSocket serverSocket = new ServerSocket(8888)) {
      Socket socket1 = serverSocket.accept();
      clientHandler1 = new ClientHandler(socket1);
      clientHandler1.start();

      Socket socket2 = serverSocket.accept();
      clientHandler2 = new ClientHandler(socket2);
      clientHandler2.start();

      player1 = clientHandler1.getPlayer();
      player2 = clientHandler2.getPlayer();

      print.printlnSystem("디버깅용, 실행 성공");

      turnGameStart();

    } catch (Exception e) {
      System.out.println("클라이언트 연결 중 오류 발생");
    }
  }

  public void turnGameStart() throws IOException {
    while (true) {
      clientHandler1.out.writeUTF("StartTurnGame");
      clientHandler2.out.writeUTF("StartTurnGame");

      int select1 = clientHandler1.in.readInt();
      int select2 = clientHandler2.in.readInt();

      if (select1 == select2) {
        printAndSendMessage(strToSystemStyle("비겼습니다."));
      } else {
        if (select1 - select2 == 1 || select1 - select2 == -2) {
          printAndSendMessage(strToSystemStyle(player1.getName() + " 승리"));
          printAndSendMessage(strToSystemStyle(player1.getName() + "가 선공으로 시작합니다."));
          break;
        } else {
          printAndSendMessage(strToSystemStyle(player2.getName() + " 승리"));
          printAndSendMessage(strToSystemStyle(player2.getName() + "가 선공으로 시작합니다."));
          break;
        }
      }
    }
  }

  private void printAndSendMessage(String msg) throws IOException {
    System.out.println(msg);
    clientHandler1.out.writeUTF(msg);
    clientHandler2.out.writeUTF(msg);
  }

  private String strToSystemStyle(String msg) {
    return "\n" + Ansi.RED.getName() + "[System] " + msg + Ansi.INIT.getName() + "\n";
  }
}
