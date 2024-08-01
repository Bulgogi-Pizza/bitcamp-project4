package com.bitcamp.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectOutputStreamStoneGame extends ObjectOutputStream {

  public ObjectOutputStreamStoneGame(OutputStream out) throws IOException {
    super(out);
  }

  @Override
  public void writeUTF(String str) throws IOException {
    super.writeUTF(str);
    super.flush();
  }

  @Override
  public void writeInt(int val) throws IOException {
    super.writeInt(val);
    super.flush();
  }
}
