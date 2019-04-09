package messaging.utilities;

import messaging.messages.Message;

import java.io.*;

public class MessageConverter {
  public static byte[] convertToBytes(Message message) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutput out = new ObjectOutputStream(bos)) {
      out.writeObject(message);
      return bos.toByteArray();
    }
  }

  public static Message convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
         ObjectInput in = new ObjectInputStream(bis)) {
      return (Message) in.readObject();
    }
  }
}