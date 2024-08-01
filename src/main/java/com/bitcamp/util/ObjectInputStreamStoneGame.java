package com.bitcamp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ObjectInputStreamStoneGame extends ObjectInputStream {

  public ObjectInputStreamStoneGame(InputStream in) throws IOException {
    super(in);
  }

}
