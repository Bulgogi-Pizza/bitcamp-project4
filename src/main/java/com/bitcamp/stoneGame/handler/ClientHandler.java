package com.bitcamp.stoneGame.handler;

import com.bitcamp.stoneGame.listener.ClientSocketListener;
import com.bitcamp.stoneGame.vo.Player;
import com.bitcamp.util.ObjectInputStreamStoneGame;
import com.bitcamp.util.ObjectOutputStreamStoneGame;
import com.bitcamp.util.Print;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler extends Thread {

  public ObjectInputStreamStoneGame in;
  public ObjectOutputStreamStoneGame out;
  Print print = new Print();
  InetAddress addr;
  private boolean turn;
  private Socket clientSocket;
  private ClientSocketListener socketListener;

  public ClientHandler(Socket socket) throws IOException {
    this.clientSocket = socket;
    this.out = new ObjectOutputStreamStoneGame(clientSocket.getOutputStream());
    this.in = new ObjectInputStreamStoneGame(clientSocket.getInputStream());
    this.socketListener = new ClientSocketListener(clientSocket, in, out);
    this.addr = clientSocket.getLocalAddress();

    socketListener.onConnect();
  }

  public Player getPlayer() {
    try {
      out.writeUTF("GetPlayer");
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
