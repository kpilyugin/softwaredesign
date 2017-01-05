package launcher;

import javafx.application.Application;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Chat;
import model.Messenger;
import view.ChatView;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MessengerApp extends Application {
  public static final int SPACING = 20;

  private final Messenger messenger = new Messenger();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    Label nameLabel = new Label("Name");
    nameLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
    setup(stage, nameLabel);

    Button editName = new Button("Edit name");
    editName.setOnMouseClicked(event -> editName(nameLabel));

    Button createButton = new Button("Create chat");
    createButton.setOnMouseClicked(event -> createChat(stage));

    TabPane chatLayout = new TabPane();
    bindTabs(chatLayout);

    VBox layout = new VBox(
        centered(new HBox(nameLabel, editName, createButton)),
        chatLayout);

    layout.setPrefSize(600, 600);
    Scene scene = new Scene(layout);

    stage.setScene(scene);
    stage.setTitle("Messenger");
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    messenger.shutdown();
    super.stop();
  }

  private void bindTabs(TabPane chatLayout) {
    messenger.getChats().addListener(new MapChangeListener<InetSocketAddress, Chat>() {
      @Override
      public void onChanged(Change<? extends InetSocketAddress, ? extends Chat> change) {
        if (change.wasAdded()) {
          ChatView chatView = new ChatView(change.getValueAdded());
          chatView.setOnClosed(event -> messenger.getChats().remove(change.getKey()));
          chatLayout.getTabs().add(chatView);
        } else {
          chatLayout.getTabs().removeIf(tab -> tab instanceof ChatView &&
              ((ChatView) tab).getChat() == change.getValueRemoved());
        }
      }
    });
  }

  private void createChat(Stage stage) {
    Stage dialog = new Stage();
    dialog.setTitle("Create chat");
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(stage);

    TextField hostField = new TextField("localhost");
    TextField portField = new TextField("0");
    Button create = new Button("Create");
    Button cancel = new Button("Cancel");
    Text state = new Text();
    VBox vBox = new VBox(
        SPACING,
        centered(new HBox(new Text("IP address"), hostField)),
        centered(new HBox(new Text("Port"), portField)),
        centered(new HBox(create, cancel)),
        state
    );
    vBox.setAlignment(Pos.CENTER);

    create.setOnMouseClicked(event -> {
      try {
        InetSocketAddress address = new InetSocketAddress(hostField.getText(), Integer.parseInt(portField.getText()));
        messenger.createChat(address);
        dialog.close();
      } catch (IllegalArgumentException e) {
        state.setText("Incorrect argument: " + e.getMessage());
      } catch (IOException e) {
        state.setText("Connection failed: " + e.getMessage());
        e.printStackTrace();
      } catch (Exception e) {
        state.setText("Unknown error: " + e.getMessage());
        e.printStackTrace();
      }
    });
    cancel.setOnMouseClicked(event -> dialog.close());

    dialog.setScene(new Scene(vBox, 300, 300));
    dialog.show();
  }

  private void setup(Stage stage, Label nameLabel) {
    Stage dialog = new Stage();
    dialog.setTitle("Start messenger");
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.initOwner(stage);

    TextField portField = new TextField("5432");
    TextField nameField = new TextField("");
    Text message = new Text("");
    Button start = new Button("Start");

    VBox vBox = new VBox(
        SPACING,
        centered(new HBox(new Text("Local port"), portField)),
        centered(new HBox(new Text("Username"), nameField)),
        start,
        message
    );

    start.setOnMouseClicked(event -> {
      String name = nameField.getText();
      if (name.isEmpty()) {
        message.setText("Please enter your name");
        return;
      }
      nameLabel.setText(name);
      try {
        message.setText("Creating messenger");
        messenger.setUserName(name);
        messenger.startServer(Integer.parseInt(portField.getText()));
        dialog.close();
      } catch (NumberFormatException e) {
        message.setText("Failed to parse port number");
      } catch (IOException e) {
        message.setText("Unknown error: " + e.getMessage());
        e.printStackTrace();
      }
    });
    vBox.setAlignment(Pos.CENTER);
    dialog.setScene(new Scene(vBox, 300, 300));
    dialog.showAndWait();
  }

  private void editName(Label label) {
    TextInputDialog dialog = new TextInputDialog(label.getText());
    dialog.setTitle("Edit name");
    dialog.setHeaderText("");
    dialog.setContentText("Please enter your name:");
    dialog.showAndWait().ifPresent(name -> {
      label.setText(name);
      messenger.setUserName(name);
    });
  }

  private static HBox centered(HBox hBox) {
    hBox.setPadding(new Insets(SPACING, SPACING, SPACING, SPACING));
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setSpacing(SPACING);
    return hBox;
  }
}
