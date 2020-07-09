package horseracing;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML
	private AnchorPane clientMain;
	@FXML
	private Button memberShip;
	@FXML
	private Button login;
	@FXML
	private Button exit;
	@FXML
	private TextField id;
	@FXML
	private PasswordField password;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		memberShip.setOnAction(e -> memberShipBtn(e));
		login.setOnAction(e -> loginBtn(e));
		exit.setOnAction(e -> exitBtn(e));
	}

	private Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void memberShipBtn(ActionEvent actionEvent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("memberShip.fxml"));
			AnchorPane member = loader.load();
			AnchorPane root = (AnchorPane) memberShip.getScene().getRoot();
			root.getChildren().add(member);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loginBtn(ActionEvent actionEvent) {
		String insertId = id.getText();
		String insertPwd = password.getText();
		
	Database database = new Database();
	database.login(insertId, insertPwd, primaryStage);
	
	}

	public void exitBtn(ActionEvent actionEvent) {
		Platform.exit();
	}

}