package com.bitcamp.stoneGame;

import com.bitcamp.stoneGame.main.GoBoardWithPhysics;
import com.bitcamp.stoneGame.ui.BoardPanel;
import com.bitcamp.stoneGame.vo.Player;
import com.bitcamp.stoneGame.vo.Stone;
import com.bitcamp.util.Print;
import com.bitcamp.util.PromptStoneGame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

  String serverAddress;
  int serverPort;
  Player player;
  List<Stone> stoneList = new ArrayList<>();

  ObjectOutputStream out;
  ObjectInputStream in;

  Print print = new Print();
  PromptStoneGame prompt = new PromptStoneGame();
  GoBoardWithPhysics goBoard;
  BoardPanel boardPanel;

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
      in = new ObjectInputStream(socket.getInputStream());
      out = new ObjectOutputStream(socket.getOutputStream());

      while (true) {
        try {
          String command = in.readUTF();

          switch (command) {
            case "SetPlayerNum":
              player.setPlayerNum(in.readInt());
              break;
            case "Connected":
              print.printlnSystem("서버에 연결되었습니다.");
              break;
            case "GetPlayer":
              out.writeObject(player);
              out.flush();
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
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void startMainGame() throws IOException, InterruptedException, ClassNotFoundException {
    out.writeUTF("MainGame Connected");
    out.flush();
    String command;
    goBoard = new GoBoardWithPhysics(player);
    boardPanel = goBoard.getBoardPanel();

    while (true) {
      if (in.available() > 0) {
        command = in.readUTF();

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
  }

  private void playTurn() throws InterruptedException, IOException {
    player.setTurn(true);
    boardPanel.updatePlayer(player);
    System.out.println("턴 시작");

    synchronized (boardPanel) {
      while (!boardPanel.isDone()) {
        boardPanel.wait();  // 대기
      }
      boardPanel.setDone(false);
    }

    out.writeUTF("done");
    out.writeObject(boardPanel.getAction());
    out.flush();

    System.out.println("턴 종료");
  }

  private void waitTurn() throws InterruptedException, IOException, ClassNotFoundException {
    player.setTurn(false);
    boardPanel.updatePlayer(player);
    System.out.println("대기 중");

    try {
      String msg = in.readUTF();
      if (msg.equals("done")) {
        List<Stone> receivedStones = (List<Stone>) in.readObject();
        synchronized (boardPanel) {
          boardPanel.setStones(receivedStones);
        }
        System.out.println("대기 완료");
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      throw e;
    }
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
    out.flush();
  }
}
