package horseracing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//한 명의 클라이언트와 통신하도록 해주는 클라이언트 클래스
public class Client {
  String Id;
  Socket socket;
  public static int start; //  클라이언트 수
  public static int startcheck=0; // 클라이언트들의 준비 판별 변수, 클라이언트 수가 같으면 신호를 보내 게임을 시작시킨다.
  public static long rand=0; // 랜덤 함수 씨드값
  
  public Client(Socket socket)
  {
	  this.socket = socket;
	  this.start = GameServer.clients.size();
	  
	  receive();
  }
  
//반복적으로 클라이언트로부터 메시지를 받는 메소드
public void receive() {
	  Runnable thread = new Runnable() {
		
		@Override
		public void run() {
			boolean bl=true;
			try
			{
				while(bl) {
					start = GameServer.clients.size(); //자료를 받을 때마다 클라이언트 수 확인
					System.out.println("클라이언트 수 : "+GameServer.clients.size());
					InputStream in = socket.getInputStream();
					byte[] buffer = new byte[512];
					
					int length = in.read(buffer);
					if(length==-1) throw new IOException();
					System.out.println("[메시지 수신 성공]"
							+socket.getRemoteSocketAddress()
							+": "+Thread.currentThread().getName());
					String message = new String(buffer,0,length,"UTF-8");
					if(message.contains(":")==false) startcheck++; // 채팅이 아니라면(준비 버튼을 눌렀다면) 체크 변수를 1증가시킨다.
					System.out.println("슽타트 체크"+startcheck);
					rand = (long)(Math.random()*1000000); //난수 생성
					
					for(int i=0; i<GameServer.clients.size(); i++)
					{
						GameServer.clients.get(i).send(message);

					}
					/*
					for(Client client : GameServer.clients) {
						client.send(message); //모든 클라이언트에 메시지 전달
					}
					*/
					
				}
				
			}
			catch(Exception e) {
			try {
				System.out.println("[메시지 수신 오류]"
						+socket.getRemoteSocketAddress()
				+": "+Thread.currentThread().getName());
			GameServer.clients.remove(Client.this);
			socket.close();
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
			}
			}
			
		}//run
	};
	GameServer.threadPool.submit(thread);
  }
  
  // 해당 클라이언트에게 메시지를 전송하는 메소드
  public void send(String message)
  {
	  Runnable thread = new Runnable() {
		
		@Override
		public void run() {
try {

	OutputStream out = socket.getOutputStream();
	System.out.println("스타트 "+start);
	System.out.println("스타트체크 "+startcheck);
	if(message.contains(":")) // 받은 내용이 채팅이면
	{
		System.out.println("채팅성공");
		byte[] buffer = message.getBytes("UTF-8");
		out.write(buffer);
		out.flush();
		
	}
	
	
	if(startcheck==start) // 모든 플레이어들이 준비를 하였다면
	{
		System.out.println("겜시작");
		byte[] buffer = new String(".s."+rand).getBytes("UTF-8"); //.s.로 채팅 메시지와 구분을 짓는다.
		out.write(buffer);
		out.flush();
		startcheck=0;
	}
	else // 아직 모든 플레이어가 준비를 안 눌렀을 경우 .d.로 구분을 지어 게임이 시작하지 않도록 한다
	{
		System.out.println("대기중..");
		byte[] buffer = new String(".d."+rand).getBytes("UTF-8");
		out.write(buffer);
		out.flush();
		
	}
   
	
	
	
}
catch(Exception e) {
	try {
		System.out.println("[메시지 송신 오류]"
				+socket.getRemoteSocketAddress()
				+": "+Thread.currentThread().getName());
		GameServer.clients.remove(Client.this);
		socket.close();
		
	}
	catch(Exception e2) {
		e2.printStackTrace();
	}
}
			
		}//run
	};
	
		GameServer.threadPool.submit(thread);	
  }//send
  
  
  
}
