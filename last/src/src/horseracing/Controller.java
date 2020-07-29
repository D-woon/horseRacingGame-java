package horseracing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
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
	private Label lb1; // 라벨 1~5 = 말 5마리
	private Label lb2;
	private Label lb3;
	private Label lb4;
	private Label lb5;
	private Label money; // 게임에 끝날때마다 금액을 기록할 라벨
    private TextArea ta; // 게임 정보를 표시할 공간
	private int sw=1; // 게임 시작 스위치 1이면 시작한다
	private int win=1; // 1등마가 정하기 위한 변수 특정 말이 결승점을 통과하면 0이 된다
	private int winner; // 이긴 말의 번호를 저장하기 위한 변수
	private ArrayList<horse> arr; // 5마리 말들을 담은 컬렉션 객체
	private user player1; // 플레이어 객체
	Socket socket; // 해당 클라이언트의 소켓 
	public static long randn;
	public static String ff;


	
	
	public void setRoot(Parent root,Socket socket) throws ClassNotFoundException, SQLException
	{
		user player1 = new user();
		this.root = root;
		this.socket = socket;
		lb1 = (Label)root.lookup("#horse1");
		lb2 = (Label)root.lookup("#horse2");
		lb3 = (Label)root.lookup("#horse3");
		lb4 = (Label)root.lookup("#horse4");
		lb5 = (Label)root.lookup("#horse5");
		ta =  (TextArea)root.lookup("#info");
		money =  (Label)root.lookup("#money");
		money.setText(player1.getID()+":"+(player1).getMoney()+"원");
		arr = new ArrayList<horse>();
		arr.add(new horse(5.53,3)); arr.add(new horse(5.52,4)); arr.add(new horse(5.55,1.5));
		arr.add(new horse(5,5)); arr.add(new horse(5.54,2));
		// 말들을 초기화함 앞에는 속도 뒤에는 배당률
	}
	

	 public void auto() { //경기 시작 메소드
		 winner=0; win=1;
		 int i=0; Random rand = new Random(randn);
		 ta.appendText("경주가 시작됩니다 !\n");
	      Thread th = new Thread() { // 익명 객체
	         @Override
	         public void run() { // 스레드에서 할 일
	            // 기존대로 쓰면 예외 발생(FX 등록 스레드만 쓸 것) 
	        	 try {
					player1 = new user();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
	 		            	
	 		            	if(win==1)  // 결승선을 통과할 경우 승자말을 정함
	 		            	{
	 		            		
	 		            		if(x>915)
	 		            		{
	 		            			winner = 1;
	 		            			
	 		            			win=0;
	 		            		}
	 		            		else if(x2>915)
	 		            		{
	 		            			winner=2;
	 		            			
	 		            			win=0;
	 		            		}
	 		            		else if(x3>915)
	 		            		{
	 		            			winner=3;
	 		            			
	 		            			win=0;
	 		            		}
	 		            		else if(x4>915)
	 		            		{
	 		            			winner=4;
	 		            			
	 		            			win=0;
	 		            		}
	 		            		else if(x5>915)
	 		            		{
	 		            			winner=5;
	 		            	
	 		            			win=0;
	 		            		}
	 		            		
	 		            		
	 		            		
	 		            		
	 		            		
	 		            	}
	 		            	
	 		            	// 한마리가 화면에서 벗어나면 게임이 완전히 종료됨
	 		            	if(x > 1000 || x2 >1000 || x3 >1000 ||x4 >1000 ||x5 >1000 )
	 		            	{
	 		            		ta.appendText("경주끝!\n");
	 		            		ta.appendText("승자는 "+winner+"말 입니다\n");

	 		                   if(player1.getHorse()==winner)
	 		            		{
	 		          try {
						player1.setMoney( player1.getMoney()-player1.getBettingmoney()
								  +(int)(player1.getBettingmoney()*arr.get(winner-1).getDrate()));
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 		         ta.appendText("당첨되었습니다!\n");
	 		        try {
						ta.appendText(player1.getMoney()+"원이 되었습니다!\n");
					} catch (ClassNotFoundException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 		        
	 		            		}
	 		            		
	 		            		else
	 		            		{
	 		            			try {
										player1.setMoney(player1.getMoney()-player1.getBettingmoney());
									} catch (ClassNotFoundException | SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	 		            			ta.appendText(player1.getBettingmoney()+"원을 잃었습니다!\n");
	
	 		            		}
	 		                  try {
								money.setText("player1 : "+player1.getMoney()+"원");
							} catch (ClassNotFoundException | SQLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
	 		                   try {
								ta.appendText("★남은 돈은 "+player1.getMoney()+"원 입니다.\n");
							} catch (ClassNotFoundException | SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	 		                 
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
	 		           			
	 		           			try {
									if(player1.getMoney()<1000) // 남은 돈이 1000원 아래면 자동으로 게임 종료
									{
									Alert alert = new Alert(Alert.AlertType.WARNING);
									alert.setContentText("파산 !");
									alert.show();
       	try {
									exitProc();
} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
}
									}
								} catch (ClassNotFoundException | SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	 		           		
	 		                }
	 		          
	 		           randn++; // 씨드값을 증가하여 랜덤값을 변경한다
	                  }
	            	  
	               });
	               //Platform.runLater(()->{print(percent);}); // 람다식으로도 사용이 가능하다. 
	               try {
	                  Thread.sleep(20);
	               } catch (InterruptedException e) {
	                  e.printStackTrace();
	               }
	          
	            }//while 끝지점 
	         } 
	      };
	      th.start();
	   }//auto
	
	 public void startProc(long rand,int start) // 준비 버튼
	 {
		 this.randn = rand; // 서버(Client.java)에서 구한 랜덤값을 시드값으로 하여 모든 클라이어트가 공통된 랜덤값을 갖도록 한다. 
		 
			
			 if(bettingController.ready == 1 && start==1) // 배팅을 했고 모든 플레이어가 준비를 했다면 시작을 한다.
			 {
				 
				 sw = 1;
				 auto();
			 }
			
		 
		 
		 
	 }
	 
	 public void bettingProc() throws IOException // 배팅 버튼 생성
, ClassNotFoundException, SQLException
	 {   user player1 = new user();
	 
		 Stage stage = new Stage();
			FXMLLoader loader = 
					new FXMLLoader(getClass().getResource("betting.fxml"));
	
	    	Parent root = loader.load();
	    	bettingController ctrler2 = loader.getController();
		    ctrler2.setRoot(root,player1,this.root);
			stage.setTitle("배팅");
			stage.setScene(new Scene(root));
			stage.show();
	 }

	 public void exitProc() throws IOException // 나가기 버튼
	 {
		 this.socket.close();
		 Stage stage = (Stage) root.getScene().getWindow();
	    	stage.close();
	    	
	 }
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}

}
