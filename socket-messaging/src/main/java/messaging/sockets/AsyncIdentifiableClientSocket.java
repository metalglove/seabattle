package messaging.sockets;

import java.nio.channels.AsynchronousSocketChannel;

public class AsyncIdentifiableClientSocket {
  private final AsynchronousSocketChannel channel;
  private String name;
  private Integer number;

  public AsyncIdentifiableClientSocket(AsynchronousSocketChannel channel) {
    this.channel = channel;
  }

  public AsynchronousSocketChannel getChannel() {
    return channel;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }
}
