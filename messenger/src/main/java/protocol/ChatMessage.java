package protocol;

import lombok.Data;

import java.util.Date;

/**
 * Contains data corresponding to a message: text of message,
 * name of sender, port at which server is running at sender side and
 * time when the message is sent.
 * Time is used only for displaying, other values are used in communication protocol {@link ChatProtocol}
 */
@Data
public class ChatMessage {
  private final String name;
  private final String message;
  private final int serverPort;
  private final Date date = new Date();
}
