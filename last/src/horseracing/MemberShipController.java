package horseracing;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MemberShipController implements Initializable {

	@FXML private AnchorPane member;
	@FXML private Button memberShip;
	@FXML private Button back;
	@FXML private TextField id;
	@FXML private PasswordField password;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		back.setOnAction(e -> backBtn(e));
		memberShip.setOnAction(e -> memberShipBtn(e));
	}
	private Stage primaryStage;	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}		
	
	public void memberShipBtn(ActionEvent actionEvent) {
		Database database = new Database();	
		String insertId = id.getText(); // 입력한 아이디와 
		String insertPwd = password.getText(); // 비밀번호
		database.memberShip(insertId, insertPwd, primaryStage, memberShip, member);
		
	}

	public void backBtn(ActionEvent actionEvent) {
		try {
			AnchorPane root = (AnchorPane) back.getScene().getRoot();
			root.getChildren().remove(member);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}