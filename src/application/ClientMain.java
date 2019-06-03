package application;

import client.ClientController;
import javafx.application.Application;
import javafx.stage.Stage;
import shared.NotificationStatus;
import view.ChatView;
import view.LoginView;

public class ClientMain extends Application{
	
	private boolean connected = false;
	private LoginView loginView = new LoginView();
	private ChatView chatView = new ChatView();
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientController controller = ClientController.getInstance();
		controller.setViews(loginView, chatView);
		controller.getLoginView().show();
		controller.startMessageListener();	
			
	}
	
	@Override
	public void stop() throws Exception {
		ClientController.getInstance().notifyServer(NotificationStatus.DISCONNECTED);	
		System.exit(0);
	}
	
	

	
	
	

}
