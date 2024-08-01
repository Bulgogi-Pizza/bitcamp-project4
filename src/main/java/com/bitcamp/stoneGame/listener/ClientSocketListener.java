package com.bitcamp.stoneGame.listener;

import com.bitcamp.util.Print;
import com.bitcamp.util.PromptStoneGame;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocketListener implements SocketListener {

  PromptStoneGame prompt = new PromptStoneGame();
  Print print = new Print();
  Socket socket;
  ObjectInputStream in;
  ObjectOutputStream out;

  public ClientSocketListener(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
    this.socket = socket;
    this.in = in;
    this.out = out;
  }

  @Override
  public void onConnect() {
    try {
      InetAddress addr = socket.getLocalAddress();
      print.printfSystem("%s : %s 클라이언트 연결", addr.getHostName(), addr.getHostAddress());
      out.writeUTF("Connected");
      out.flush();
    } catch (Exception e) {
      System.out.println("클라이언트 연결 중 오류 발생!");
    }
  }

  @Override
  public void onDisconnect() {

  }
}
