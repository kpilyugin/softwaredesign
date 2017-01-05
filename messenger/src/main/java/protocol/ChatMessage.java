package protocol;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
  private final String name;
  private final String message;
  private final int serverPort;
  private final Date date = new Date();
}
