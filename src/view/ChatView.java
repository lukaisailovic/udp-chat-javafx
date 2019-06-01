package view;

import client.Message;
import controller.ClientController;
import controller.ServerController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ChatView extends Stage{
	

	private Label lblChatInfo = new Label();
	
	
	private Label lblRecipientName = new Label("Recipient name:");
	private TextField tfRecipient = new TextField();
	private Button btnChangeRecipient = new Button("Change recipient");
	private Label lblStatus = new Label("You currently can't receive any message");
	
	
	private ListView<Text> chatWindow = new ListView<>();
	
	private TextField tfInput = new TextField();
	private Button btnChat = new Button("Send");
	
	private BorderPane bp = new BorderPane();
	
	private ClientController controller = ClientController.getInstance();
	
	public ChatView() {
		
		
		
		// top 
		HBox hbTop = new HBox(10);
		hbTop.setPadding(new Insets(15, 10, 5, 10));
		hbTop.getChildren().addAll(lblChatInfo,lblRecipientName,tfRecipient,btnChangeRecipient,lblStatus);
		hbTop.setAlignment(Pos.CENTER);
		// end top
		
		
		// center 
		for (int i = 0; i < 20; i++) {
			//chatWindow.getItems().add(" Item "+i);
			
		}
		//chatWindow.getItems().addAll("test","test2");		
		// end center
		
		
		// bottom
		HBox hbBottom = new HBox(10);
		hbBottom.setPadding(new Insets(15,5,15,5));
		hbBottom.setAlignment(Pos.CENTER_LEFT);
		hbBottom.getChildren().addAll(tfInput,btnChat);
		
		tfInput.setPrefWidth(720);
		tfInput.setPrefHeight(35);
		
		btnChat.setPrefHeight(35);
		
		// end bottom
		bp.setTop(hbTop);
		bp.setCenter(chatWindow);
		bp.setBottom(hbBottom);

		Scene sc = new Scene(bp,800,500);
		this.setScene(sc);
		this.setTitle("UDP Chat - Client");
		this.setActions();
		this.setResizable(false);
	}
	
	
	private void setActions() {
		this.btnChat.setOnAction(e -> {
			String chatText = this.tfInput.getText();
			
			if (chatText.contains(";")) {
				this.addAlert();
				return;
			}
			
			Message msg = new Message(chatText, this.controller.getUsername());
			
			this.addMessage(msg,true);
			this.chatWindow.scrollTo(this.chatWindow.getItems().size()-1);
			this.tfInput.setText("");
			String recipient = this.tfRecipient.getText();
			try {
				this.controller.sendMessage(msg,recipient);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		this.btnChangeRecipient.setOnAction(e -> {
			String newRecipient = this.tfRecipient.getText();
			ClientController controller = ClientController.getInstance();
			controller.setRecipient(newRecipient);
			System.out.println("New recipient is "+controller.getRecipient());
			this.chatWindow.getItems().clear();
			this.chatWindow.refresh();
			
			if (!newRecipient.isEmpty()) {
				this.lblStatus.setText("You can receive messages from "+newRecipient);
			} else {
				this.lblStatus.setText("You currently can't receive any message");
			}
			this.setTitle("Chat window ["+controller.getUsername()+ " - "+controller.getRecipient()+" ]");
		});
		
		bp.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
	        if (e.getCode() == KeyCode.ENTER) {
	           this.btnChat.fire();
	           e.consume(); 
	        }
	    });
		
	}
	
	public void addAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText("Character not allowed");
		alert.setContentText("Characted [ ; ] is used as delimiter and is not allowed to be in message");

		alert.showAndWait();
	}
	
	public void loadData() {
		this.lblChatInfo.setText("Signed in as " + this.controller.getUsername());
	}
	
	public void addMessage(Message msg, boolean bold) {
		
		Platform.runLater(new Runnable() {
		
			@Override
			public void run() {	
				Text txt = new Text(msg.toString());
				if (bold) {
					txt.setFont(Font.font("Verdana",FontWeight.EXTRA_LIGHT,12));
				} else {
					txt.setFont(Font.font("Verdana",FontWeight.NORMAL,13));
				}
				chatWindow.getItems().add(txt);			
				chatWindow.refresh();			
			};
				
		});
		
	}
	
	
	
	
}
