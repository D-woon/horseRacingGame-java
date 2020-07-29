package horseracing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.javafx.collections.SetListenerHelper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Database {

	public void memberShip(String insertId, String insertPwd, Stage primaryStage, Button memberShip,
			AnchorPane member) {
		String jdbcUrl = "jdbc:oracle:thin:@192.168.19.167:1521:xe";
		// MySQL 계정
		String dbId = "java";
		// MySQL 계정 비밀번호
		String dbPw = "1234";
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String sql = "";
		String sql2 = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 디비 연결
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select id from horse where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, insertId);
			rs = pstmt.executeQuery();
			Stage dialog = new Stage(StageStyle.UTILITY); // 회원 가입할 경우 아이디의 중복
															// 여부 또는 아이디와 비밀번호를
															// 입력하지 않은 경우에 대한
															// 다이얼로그
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(primaryStage);
			dialog.setTitle("중복확인");

			AnchorPane parent = (AnchorPane) FXMLLoader.load(getClass().getResource("custom_dialog.fxml"));
			Label txtTitle = (Label) parent.lookup("#txtTitle");
			Button btnOk = (Button) parent.lookup("#btnOk");
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);

			if (rs.next()) { // 중복확인
				if (rs.getString("id").equals(insertId)) {
					txtTitle.setText("이미 사용중인 아이디 입니다.");
					btnOk.setOnAction(event -> dialog.close());
					dialog.show();
				}
				// 아이디나 비밀번호를 입력하지 않았을 경우
			} else if (insertId.equals("") || insertPwd.equals("")) {
				txtTitle.setText("아이디와 비밀번호를 입력해주세요.");
				btnOk.setOnAction(event -> dialog.close());
				dialog.show();
			} else { // 회원가입이 가능할 경우
				txtTitle.setText("사용 가능한 아이디입니다. 회원가입을 축하합니다!");
				btnOk.setOnAction(event -> dialog.close());
				dialog.show();

				sql2 = "insert into horse values(?,?,?,?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, insertId);
				pstmt2.setString(2, insertPwd);
				pstmt2.setInt(3, 10000);
				pstmt2.setInt(4,0);
				pstmt2.executeUpdate();
				AnchorPane root = (AnchorPane) memberShip.getScene().getRoot();
				root.getChildren().remove(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
	}

	public void login(String insertId, String insertPwd, Stage primaryStage) {
		String jdbcUrl = "jdbc:oracle:thin:@192.168.19.167:1521:xe";
		// MySQL 계정
		String dbId = "java";
		// MySQL 계정 비밀번호
		String dbPw = "1234";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 디비 연결
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);

			sql = "select id,password from horse where id = ? and password = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, insertId);
			pstmt.setString(2, insertPwd);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (rs.getString("id").equals(insertId) && rs.getString("password").equals(insertPwd)) {
					MainClass mc=new MainClass();
					mc.start(primaryStage);				
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {	if (pstmt != null)	try {	pstmt.close();	} catch (SQLException ex) {	}
			if (conn != null)	try {	conn.close();	} catch (SQLException ex) {	}
		}
	}
}
