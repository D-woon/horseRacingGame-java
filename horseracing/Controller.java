package horseracing;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Controller implements Initializable{
	private Parent root;
	private Label lb1, lb2, lb3, lb4, lb5; 
	private Label money; // 게임에 끝날때마다 금액을 기록할 라벨
    private TextArea ta; // 게임 정보를 표시할 공간
	private int sw=1; // 게임 시작 스위치 1이면 시작한다
	private int win=1; // 1등마가 정하기 위한 변수 특정 말이 결승점을 통과하면 0이 된다
	private int winner; // 이긴 말의 번호를 저장하기 위한 변수
	private ArrayList<horse> arr; // 5마리 말들을 담은 컬렉션 객체
	private player player1; // 플레이어 객체
	Socket socket; // 해당 클라이언트의 소켓 
	public static long randn; // 랜덤 함수의 씨드값
	public static String ff;
	private int money2;
	
	private int IDMoney;

	public int getIDMoney() {
		return IDMoney;
	}
	public void setIDMoney(int iDMoney) {
		IDMoney = iDMoney;
	}

	LoginInfo info;
	
	public int getmoney2()
	{
		return money2;
	}
	
	public void setRoot(Parent root,Socket socket, LoginInfo info)
	{
		this.root = root;
		this.socket = socket;
		this.info = info;
		Select(info.getLoginid());
		player1 = new player(getIDMoney());
		lb1 = (Label)root.lookup("#horse1");
		lb2 = (Label)root.lookup("#horse2");
		lb3 = (Label)root.lookup("#horse3");
		lb4 = (Label)root.lookup("#horse4");
		lb5 = (Label)root.lookup("#horse5");
		ta =  (TextArea)root.lookup("#info");
		money =  (Label)root.lookup("#money");
		money.setText(info.getLoginid()+" : "+player1.getMoney()+"원");
		money2 = player1.getMoney();
		arr = new ArrayList<horse>();
		arr.add(new horse(5.53,3)); arr.add(new horse(5.52,4)); arr.add(new horse(5.55,1.5));
		arr.add(new horse(5,5)); arr.add(new horse(5.54,2));
		
		// 말들을 초기화함 앞에는 속도 뒤에는 배당률
	}
	
	public void Select(String id) { // DB 연결을 위해
		String jdbcUrl = "jdbc:oracle:thin:@192.168.19.170:1521:xe"; // DB의 IP
		String dbId = "java"; String dbPw = "1234";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "select money from horse where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
			setIDMoney(rs.getInt("money"));
		
			}
			} catch (Exception e) {
			e.printStackTrace();
		} finally {	if (pstmt != null)	try {	pstmt.close();	} catch (SQLException ex) {	}
			if (conn != null)	try {	conn.close();	} catch (SQLException ex) {	}
		}
	}

	public void Update(String id, int mon) { // 업데이트용
		String jdbcUrl = "jdbc:oracle:thin:@192.168.19.170:1521:xe"; // DB의 IP
		String dbId = "java"; String dbPw = "1234";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(jdbcUrl, dbId, dbPw);
			sql = "update horse set money = ? where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mon);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			rs.next(); // mon 값만 정확하게 지정해주고, select로 계속 불러오게만 하면 될듯.
			
			} catch (Exception e) {
			e.printStackTrace();
		} finally {	if (pstmt != null)	try {	pstmt.close();	} catch (SQLException ex) {	}
			if (conn != null)	try {	conn.close();	} catch (SQLException ex) {	}
		}
	}
	
	
	 public void auto() { //경기 시작 메소드
		 winner=0; win=1;
		 int i=0;  Random rand = new Random(randn);
		 ta.appendText("경주가 시작됩니다 !\n");
		 Thread th = new Thread() { // 익명 객체
			 @Override
			 public void run() { // 스레드에서 할 일
				 // 기존대로 쓰면 예외 발생(FX 등록 스레드만 쓸 것) 
				 while(sw==1) {
			     // UI 충돌이 예상되는 지점 
					  Platform.runLater(new Runnable() { // 람다식으로도 사용이 가능하다. 
						 double x = lb1.getLayoutX();
						 double x2 = lb2.getLayoutX();
						 double x3 = lb3.getLayoutX();
						 double x4 = lb4.getLayoutX();
						 double x5 = lb5.getLayoutX();
						 // 말들의 현재 x좌표를 가져온다
						 @Override
						 public void run() {
							 // randn의 변화에 따라 일정한 랜덤값을 반환한다.
							 ff = ff.valueOf(rand.nextDouble());
							 double f = Integer.parseInt(ff.substring(2, 3))/2.0;
							 if(f<0) f=-f; // f가 -일 경우
	            			  
							 double f2 = Integer.parseInt(ff.substring(3, 4))/2.0;
							 if(f<0) f2=-f2;
		            		   
							 double f3 = Integer.parseInt(ff.substring(4, 5))/2.0;
							 if(f<0) f3=-f3;
		            		   
							 double f4 = Integer.parseInt(ff.substring(5, 6))/2.0;
							 if(f<0) f4=-f4;
		            		   
							 double f5 = Integer.parseInt(ff.substring(6, 7))/2.0;
							 if(f<0) f5=-f5;
	            		   	            		   
							 x += f+arr.get(0).getSpeed()/20;
							 x2 += f2+arr.get(1).getSpeed()/20;
							 x3 += f3+arr.get(2).getSpeed()/20;
							 x4 += f4+arr.get(3).getSpeed()/20;
							 x5 += f5+arr.get(4).getSpeed()/20;
							 // 랜덤값과 속도를 조합하여 이동할 거리를 정함
	 		            	
							 lb1.setLayoutX(x);
							 lb2.setLayoutX(x2);
							 lb3.setLayoutX(x3);
							 lb4.setLayoutX(x4);
							 lb5.setLayoutX(x5);
							 // 이동시킴
	 		            	
							 if(win==1) {  // 결승선을 통과할 경우 승자말을 정함
								 if(x>915) {
									 winner = 1;
									 win=0;
	 		            		}
	 		            		else if(x2>915) {
	 		            			winner=2;
	 		            			win=0;
	 		            		}
	 		            		else if(x3>915) {
	 		            			winner=3;
	 		            			win=0;
	 		            		}
	 		            		else if(x4>915) {
	 		            			winner=4;
	 		            			win=0;
	 		            		}
	 		            		else if(x5>915) {
	 		            			winner=5;
	 		            			win=0;
	 		            		}
							 }
	 		            	
	 		            	// 한마리가 화면에서 벗어나면 게임이 완전히 종료됨
							 if(x > 1000 || x2 >1000 || x3 >1000 ||x4 >1000 ||x5 >1000 ) {
								 ta.appendText("경주끝!\n");
								 ta.appendText("승자는 "+winner+"번말 입니다\n");

								 if(player1.getHorse()==winner){
									
									 money2 = (money2-player1.getBettingmoney()+(int)(player1.getBettingmoney()*arr.get(winner-1).getDrate()));
								
									 
	 		                	  
									 ta.appendText("우승하였습니다!\n");
									 ta.appendText(money2+"원이 되었습니다!\n");
								 }
	 		            		
								 else {
									 
									 money2 = (money2-player1.getBettingmoney());
									 
									
								 }
								 
								 money.setText(info.getLoginid()+" : "+money2+"원");
								 ta.appendText("★남은 돈은 "+money2+"원 입니다.\n");
	 		                 
								 sw = 0;
								 try {
									 Thread.sleep(2500);
								 } catch (InterruptedException e) {
									 // TODO Auto-generated catch block
									 e.printStackTrace();
								 }
								 // 한게임이 끝난 후 말들 재배치
								 lb1.setLayoutX(23); lb1.setLayoutY(11);
								 lb2.setLayoutX(23); lb2.setLayoutY(77);
								 lb3.setLayoutX(23); lb3.setLayoutY(142);
								 lb4.setLayoutX(23); lb4.setLayoutY(205);
								 lb5.setLayoutX(23); lb5.setLayoutY(270);
								 Button btn = 
										 (Button)root.lookup("#startbtn");
								 btn.setDisable(true); //게임이 종료 후 준비 버튼이 비활성화됨

								 if(player1.getMoney()<1000) { // 남은 돈이 1000원 아래면 자동으로 게임 종료

									 Alert alert = new Alert(Alert.AlertType.WARNING);
									 alert.setContentText("파산 !");
									 alert.show();
									 try {
										 String message="exit2";
										 OutputStream out = socket.getOutputStream();
										 byte[] buffer = message.getBytes("UTF-8");
										 out.write(buffer);
										 out.flush();
										 exitProc();
									 } catch (IOException e) {
										 // TODO Auto-generated catch block
										 e.printStackTrace();
									 }
								 }//파산
							 }//if
						 }
					 });
					 //Platform.runLater(()->{print(percent);}); // 람다식으로도 사용이 가능하다. 
					 try {
						 Thread.sleep(20);
					 } catch (InterruptedException e) {
						 e.printStackTrace();
					 }

				 } 
			 } 
		 };
		 th.start();
	 }//auto

	 public void startProc(long rand,int start) {// 준비 버튼
		 this.randn = rand; // 서버(Client.java)에서 구한 랜덤값을 시드값으로 하여 모든 클라이어트가 공통된 랜덤값을 갖도록 한다. 
		 if(bettingController.ready == 1 && start==1) // 배팅을 했고 모든 플레이어가 준비를 했다면 시작을 한다.
		 {
			 sw = 1;
			auto();
		 }
         
	 }
	 
	 public void bettingProc() throws IOException {// 배팅 버튼 생성
	 	 Stage stage = new Stage();
		 FXMLLoader loader = 
				 new FXMLLoader(getClass().getResource("betting.fxml"));

		 Parent root = loader.load();
		 bettingController ctrler2 = loader.getController();
		 ctrler2.setRoot(root,player1,this.root,money2);
		 stage.setTitle("배팅");
		 stage.setScene(new Scene(root));
		 stage.show();
	 }

	 public void exitProc() throws IOException {// 나가기 버튼
          Update(info.getLoginid(), money2);
			Select(info.getLoginid());
		 
		 this.socket.close();
		 Stage stage = (Stage) root.getScene().getWindow();
		 stage.close();
	 }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		
	}

}
