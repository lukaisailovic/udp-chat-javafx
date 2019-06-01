package view;

import controller.ServerController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ServerView extends Stage{
	

	private Label lblServerInfo = new Label("Server started on port 8080");
	
	private ListView<String> chatWindow = new ListView<>();
	
	private TextField tfInput = new TextField();
	private Button btnChat = new Button("Send");
	
	private BorderPane bp = new BorderPane();
	
	public ServerView() {
		
		
		
		// top 
		HBox hbTop = new HBox(10);
		hbTop.setPadding(new Insets(15, 10, 5, 10));
		hbTop.getChildren().addAll(lblServerInfo);
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
		this.setTitle("UDP Chat - Server");
		this.setActions();
	}
	
	
	private void setActions() {
		this.btnChat.setOnAction(e -> {
			String chatText = this.tfInput.getText();
			this.chatWindow.getItems().add(chatText);
			this.chatWindow.scrollTo(this.chatWindow.getItems().size()-1);
			this.tfInput.setText("");
			
			ServerController sc = ServerController.getInstance();
			sc.testCall(chatText);
		});
		
		bp.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
	        if (e.getCode() == KeyCode.ENTER) {
	           this.btnChat.fire();
	           e.consume(); 
	        }
	    });
		
	}
}
