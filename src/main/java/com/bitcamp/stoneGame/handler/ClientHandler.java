package com.bitcamp.stoneGame.handler;

import com.bitcamp.stoneGame.listener.ClientSocketListener;
import com.bitcamp.stoneGame.vo.Player;
import com.bitcamp.util.Print;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler extends Thread {

  public ObjectInputStream in;
  public ObjectOutputStream out;
  Print print = new Print();
  InetAddress addr;
  private boolean turn;
  private Socket clientSocket;
  private ClientSocketListener socketListener;

  public ClientHandler(Socket socket) throws IOException {
    this.clientSocket = socket;
    this.out = new ObjectOutputStream(clientSocket.getOutputStream());
    this.in = new ObjectInputStream(clientSocket.getInputStream());
    this.socketListener = new ClientSocketListener(clientSocket, in, out);
    this.addr = clientSocket.getLocalAddress();

    socketListener.onConnect();
  }

  public Player getPlayer() {
    try {
      out.writeUTF("GetPlayer");
      out.flush();
      return (Player) in.readObject();
    } catch (Exception e) {
      System.out.println("플레이어 정보 수신 중 오류 발생!");
      e.printStackTrace();
      return null;
    }
  }

  public String getHostName() {
    return addr.getHostName();
  }

  public String getHostAddress() {
    return addr.getHostAddress();
  }

  @Override
  public void run() {
  }
}
