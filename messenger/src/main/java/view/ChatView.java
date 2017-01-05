package view;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import lombok.Getter;
import model.Chat;
import protocol.ChatMessage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Represents a view for {@link Chat}.
 * Consists of a list view for chat history and text area for new message.
 */
public class ChatView extends Tab {
  private static final Logger LOGGER = Logger.getLogger(ChatView.class.getName());

  @Getter
  private final Chat chat;

  public ChatView(Chat chat) {
    this.chat = chat;

    ListView<ChatMessage> messages = new ListView<>(chat.getMessages());
    messages.setCellFactory(p -> new MessageView());
    TextArea messageArea = new TextArea();
    messageArea.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        if (event.isControlDown() || event.isMetaDown()) {
          messageArea.setText(messageArea.getText() + "\n");
          messageArea.end();
          return;
        }
        try {
          chat.sendMessage(messageArea.getText());
          Platform.runLater(() -> messageArea.setText("")); // without runLater newline character is inserted
        } catch (IOException e) {
          LOGGER.warning("Failed sending message: " + e.getMessage());
          e.printStackTrace();
        }
      }
    });
    textProperty().bind(chat.getFriendName());
    setContent(new VBox(messages, messageArea));
  }
}
