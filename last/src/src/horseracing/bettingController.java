package horseracing;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class bettingController implements Initializable{
	private Parent root;
	private Parent parentroot;
	private user player; // 플레이어 정보
	private TextArea ta;
	public static int ready; // 배팅 여부 판별 변수

	
	public void setRoot(Parent root,user player,Parent root2) throws ClassNotFoundException, SQLException
	{
		this.root = root;
		this.player = player;
		this.parentroot = root2;
		ta =  (TextArea)root2.lookup("#info");
		ready = 0;
		Label lb = (Label) root.lookup("#pmoney");
		lb.setText("남은 금액 : "+player.getMoney());
		AddComboBox();
	}
	
	public void AddComboBox() {
		ComboBox<String> cmb = 
				(ComboBox<String>)root.lookup("#combo");
		if(cmb != null)
			cmb.getItems().addAll("1번마","2번마","3번마",
													"4번마","5번마");
	}
	 
	 public void okProc() throws ClassNotFoundException, SQLException // 배팅 확인 버튼
	 {
		 ComboBox<String> cmb = (ComboBox<String>)root.lookup("#combo");
			String horse="";
			TextField tf = (TextField)root.lookup("#money");	
			String money = tf.getText();
			
			
			try
			{
				int money2 = Integer.parseInt(money);
				if(money2<1000)
				{
					ta.appendText("천원 이상을 입력하세요 !\n");
	
				}
				else if(player.getMoney()<money2)
				{
					ta.appendText("걸돈이 부족합니다 !\n");
				}
				else
				{
					player.setBettingmoney(money2);
					ta.appendText(money2+"원을 배팅하였습니다 !\n");
				   Button btn = (Button)this.parentroot.lookup("#startbtn");
				   btn.setDisable(false);
				   
				}
				
				
			}
			catch(Exception e)
			{
				ta.appendText("입력 오류 !\n");
			}
			
			
			
			if(cmb==null){ ta.appendText("배팅을 하지 않았습니다 !\n"); }
			else if(cmb.getValue()==null){
				ta.appendText("경주마를 선택하세요 !\n");
				cmb.requestFocus();
			}else{	horse = cmb.getValue().toString(); 
			   if(horse=="1번마")
			   {
				   player.setHorse(1);
				   
			   }
			   if(horse=="2번마")
			   {
				   player.setHorse(2);
				   
			   }
			   if(horse=="3번마")
			   {
				   player.setHorse(3);
				 
			   }
			   if(horse=="4번마")
			   {
				   player.setHorse(4);
				 
			   }
			   if(horse=="5번마")
			   {
				   player.setHorse(5);
				   
			   }
			}
			
			
			
			
			
			
			
	
			Stage stage = (Stage) root.getScene().getWindow();
	    	
			stage.close();
			ready = 1;
	 }
	 
	 public void cancelProc() // 배팅 취소 버튼
	 {
		 Stage stage = (Stage) root.getScene().getWindow();
	    	stage.close();
	 }
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}

}
