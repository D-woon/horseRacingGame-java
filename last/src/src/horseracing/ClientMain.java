package horseracing;

import horseracing.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Client Page");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
		Parent root = loader.load();
		ClientController controller = loader.getController(); // 이 두줄의 코드가 있어야
		controller.setPrimaryStage(primaryStage); // 화면전환이 제대로 동작하네요 ㅎㅎ
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
