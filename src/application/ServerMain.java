package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import server.ServerController;


public class ServerMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		ServerController sc = ServerController.getInstance();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
