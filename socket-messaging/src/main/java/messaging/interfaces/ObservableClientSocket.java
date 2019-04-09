package messaging.interfaces;

import java.beans.PropertyChangeListener;

public interface ObservableClientSocket extends ReadableSocket, WritableSocket {
  void addListener(String eventName, PropertyChangeListener listener);

  void removeListener(String eventName, PropertyChangeListener listener);
}
