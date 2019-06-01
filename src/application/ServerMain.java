package application;
	
import controller.ServerController;
import javafx.application.Application;
import javafx.stage.Stage;


public class ServerMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		
//		ServerView sv = new ServerView();
//		sv.show();
//		
		ServerController sc = ServerController.getInstance();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}