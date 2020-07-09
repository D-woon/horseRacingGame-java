package horseracing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class MainClass extends Application{
	Socket socket;
	Controller ctrler;
	TextArea textArea;
	LoginInfo info;
	
	// 클라이언트 프로그램의 작동을 시작하는 메소드
		public void startClient(String IP,int port)
		{
			Thread thread = new Thread() {
				public void run() {
					try {
					socket = new Socket(IP,port);
					receive();
					}
					catch(Exception e) {
						if(!socket.isClosed()) {
							stopClient();
							System.out.println("[서버 접속 실패]");
							Platform.exit();
						}
					}
				}
			};
			thread.start();
		}
		
		//클라이언트 프로그램의 작동을 종료하는 메소드
		public void stopClient()
		{
			try {
			if(socket != null && !socket.isClosed())
				socket.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//서버로부터 메시지를 전달받는 메소드
		public void receive()
		{
		  while(true) {
			  try {
		InputStream in = socket.getInputStream();
		byte[] buffer = new byte[512];
		int length = in.read(buffer);
		if(length==-1)throw new IOException();
		String message = new String(buffer,0,length,"UTF-8");
		System.out.println(message);
		Platform.runLater(()->{ // .s.가 포함된 메시지를 서버로부터 받으면 게임을 시작한다.
			if(message.contains(".s."))
			{
			  String randd = message.substring(3);
			  long rand = Long.parseLong(randd);
				ctrler.startProc(rand,1); // 1을 보내 게임 시작한다
			}
			else if(message.contains(":")) // :가 포함된 메시지를 서버로부터 받으면 메시지가 채팅창에 입력된다.
			{
				textArea.appendText(message);
			}
			else 
			{
				String randd = message.substring(3);
				  long rand = Long.parseLong(randd);
					ctrler.startProc(rand,0); //0을 보내 대기한다.
			}
			
		});
			  }
			  catch(Exception e) {
				  stopClient();
				  break;
			  }
		  }
		}
		
		//서버로 메시지를 전송하는 메소드
		public void send(String message)
		{
			Thread thread = new Thread() {
				public void run()
				{
					try {
						OutputStream out = socket.getOutputStream();
						byte[] buffer = message.getBytes("UTF-8");
						out.write(buffer);
						out.flush();
						}
						catch(Exception e) {
							stopClient();
						}
				}
				
			};
		thread.start();
			
		
		}
		
		
	public void start(Stage primaryStage, LoginInfo info) throws IOException 
	{	
		
		startClient("192.168.0.9",9875); //접속할 서버 아이피와 포트번호 입력
		FXMLLoader loader = 
				new FXMLLoader(getClass().getResource("horse.fxml"));
		Parent root = loader.load();
		
		//준비 버튼 클릭시 동작할 내용
		Button btn = 
				(Button)root.lookup("#startbtn");
		btn.setOnAction(event->{
			btn.setDisable(true);
			textArea.appendText(info.getLoginid()+"님 준비 완료. 다른 플레이어를 기다립니다\n");
			send("z");
		});
		
		//나가기 버튼 클릭시 동작할 내용
				Button btn2 = 
						(Button)root.lookup("#exit");
				btn2.setOnAction(event->{
					System.out.println("exit");
					send("exit");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						this.socket.close();
						ctrler.exitProc();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		
		
		//채팅창에 글을 입력하고 엔터를 누를시
		TextField input = (TextField)root.lookup("#chat");
		input.setOnAction(event->{
			send(info.getLoginid()+" : "+input.getText()+"\n");
			input.setText("");
			input.requestFocus();
		});
		textArea = (TextArea)root.lookup("#chatview");
		
		Scene scene = new Scene(root);
		ctrler = loader.getController();
		ctrler.setRoot(root,this.socket, info);
		primaryStage.setTitle("경마게임");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	// 프로그램의 진입점
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
