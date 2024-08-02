package com.bitcamp.stoneGame;

import com.bitcamp.GoBoardWithPhysics;
import com.bitcamp.stoneGame.vo.Player;
import com.bitcamp.util.ObjectInputStreamStoneGame;
import com.bitcamp.util.ObjectOutputStreamStoneGame;
import com.bitcamp.util.Print;
import com.bitcamp.util.PromptStoneGame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

  String serverAddress;
  int serverPort;
  Player player;

  ObjectOutputStream out;
  ObjectInputStream in;

  Print print = new Print();
  PromptStoneGame prompt = new PromptStoneGame();

  public Client() {
  }

  public static void main(String[] args) {
    Client client = new Client();

    client.initSetting();
    client.execute();
  }

  private void initSetting() {
    serverAddress = prompt.input("서버 IP 주소 >>");
    serverPort = prompt.inputInt("서버 Port 번호 >>");
    String name = prompt.input("플레이어 이름 >>");

    player = new Player(name);
  }

  private void execute() {
    try (Socket socket = new Socket(serverAddress, serverPort)) {
      in = new ObjectInputStreamStoneGame(socket.getInputStream());
      out = new ObjectOutputStreamStoneGame(socket.getOutputStream());

      while (true) {
        try {
          String command = in.readUTF();

          switch (command) {
            case "Connected":
              print.printlnSystem("서버에 연결되었습니다.");
              break;
            case "GetPlayer":
              out.writeObject(player);
              break;
            case "StartTurnGame":
              startTurnGame();
              break;
            case "StartMainGame":
              startMainGame();
              break;
          }
        } catch (Exception e) {
          System.out.println("오류 발생");
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void startMainGame() throws IOException {
    out.writeUTF("MainGame Connected");

    new GoBoardWithPhysics();

    while (true) {
      String command = in.readUTF();

      switch (command) {
        case "PlayerTurn":
          playTurn();
          break;
        case "WaitPlay":
          waitTurn();
          break;
      }
    }
  }

  private void playTurn() {

  }

  private void waitTurn() {

  }

  private void startTurnGame() throws IOException {
    String[] turnGameCommands = {"가위", "바위", "보"};
    System.out.println("선/후공 결정 게임");
    System.out.println("가위 바위 보 중에 선택");

    for (int i = 0; i < turnGameCommands.length; i++) {
      System.out.println(i + 1 + ". " + turnGameCommands[i]);
    }

    int select = prompt.inputIntWithRange(1, 3, "선택 >>");
    out.writeInt(select);
  }
}
