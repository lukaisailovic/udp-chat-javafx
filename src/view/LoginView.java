package view;

import client.ClientController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.NotificationStatus;

public class LoginView extends Stage {
	
	private BorderPane bp = new BorderPane();
	private ClientController controller = ClientController.getInstance();
	
	private Label lblUsernmae = new Label("Username:");
	private TextField tfUsername = new TextField();
	
	private Button btnConnect = new Button("Connect");

	
	public LoginView() {	
		// top 
		VBox vbCenter = new VBox(20);
		vbCenter.setPadding(new Insets(15, 10, 5, 10));
		vbCenter.getChildren().addAll(lblUsernmae,tfUsername,btnConnect);
		vbCenter.setAlignment(Pos.CENTER);
		// end top
		
		
		bp.setCenter(vbCenter);
		Scene sc = new Scene(bp,250,400);
		this.setScene(sc);
		this.setTitle("UDP Chat - Login");
		this.setActions();
		this.setResizable(false);
	}
	
	
	private void setActions() {
		this.btnConnect.setOnAction(e -> {
			String usernameText = this.tfUsername.getText();
			try {
				this.controller.setUsername(usernameText);
				this.controller.notifyServer(NotificationStatus.CONNECTED);
				this.controller.showChatView();
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		bp.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
	        if (e.getCode() == KeyCode.ENTER) {
	           this.btnConnect.fire();
	           e.consume(); 
	        }
	    });
	}
}
