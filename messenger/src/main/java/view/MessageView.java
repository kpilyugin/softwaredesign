package view;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import protocol.ChatMessage;

/**
 * Represents a view of single message.
 */
public class MessageView extends ListCell<ChatMessage> {
  @Override
  protected void updateItem(ChatMessage item, boolean empty) {
    super.updateItem(item, empty);
    if (item == null || empty) {
      setGraphic(null);
      return;
    }
    Text name = new Text(item.getName());
    name.setFont(Font.font(20));

    Text date = new Text(item.getDate().toString());
    date.setFill(Color.BLUE);

    Text message = new Text(item.getMessage());
    HBox hBox = new HBox(
        new VBox(name, date),
        message
    );
    hBox.setSpacing(20);
    hBox.setAlignment(Pos.CENTER_LEFT);
    setGraphic(hBox);
  }
}
