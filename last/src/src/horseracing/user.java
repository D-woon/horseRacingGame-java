package horseracing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class user {
	private String ID;
	private int money;
	private int horse; // 미구현
	private int bettingmoney; //배팅한 돈
	private String password;
	
	user() throws ClassNotFoundException, SQLException{
		
		Class.forName("oracle.jdbc.driver.OracleDriver");//기능등록 메모리공간에 올려놓음
		String id= "java"; String pwd ="1234";
		String url="jdbc:oracle:thin:@192.168.19.167:1521:xe";//연결 시도 1521은포트번호 xe는 오라클 버전
		Connection conn=DriverManager.getConnection(url,id,pwd);//등록된 기능사용하기 기능등록이안되면 사용 불가 
		System.out.println("연결 객체:"+conn);
		PreparedStatement pstm= null;
		ResultSet rs=null;
		String sql=null; 
		sql = "select * from horse";
		pstm= conn.prepareStatement(sql);
		rs =pstm.executeQuery();
		while(rs.next()) {//rs next는 다음 값을 불러온다 
		this.ID=rs.getString("id");
        this.password=rs.getString("password");
        this.money=rs.getInt("money");
        this.bettingmoney=rs.getInt("bettingmoney");
		if(this.ID==rs.getString("id")) {break;}
		}
		if(rs!=null)rs.close();
		if(pstm!=null)pstm.close();
		if(conn!=null)conn.close();
		
		
	}


	public String getID() {

		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public int getMoney() throws SQLException, ClassNotFoundException {
		Class.forName("oracle.jdbc.driver.OracleDriver");//기능등록 메모리공간에 올려놓음
		String id= "java"; String pwd ="1234";
		String url="jdbc:oracle:thin:@192.168.19.167:1521:xe";//연결 시도 1521은포트번호 xe는 오라클 버전
		Connection conn=DriverManager.getConnection(url,id,pwd);//등록된 기능사용하기 기능등록이안되면 사용 불가 
		System.out.println("연결 객체:"+conn);
		PreparedStatement pstm= null;
		ResultSet rs=null;
		String sql=null; 
		sql = "update newst set money=? where id=?";
		pstm= conn.prepareStatement(sql);
		pstm.setInt(1,this.money);
		pstm.setString(2,ID);
		pstm.executeUpdate();
		return money;
	}

	public void setMoney(int money) throws ClassNotFoundException, SQLException {
		this.money = money;
		Class.forName("oracle.jdbc.driver.OracleDriver");//기능등록 메모리공간에 올려놓음
		String id= "java"; String pwd ="1234";
		String url="jdbc:oracle:thin:@192.168.19.167:1521:xe";//연결 시도 1521은포트번호 xe는 오라클 버전
		Connection conn=DriverManager.getConnection(url,id,pwd);//등록된 기능사용하기 기능등록이안되면 사용 불가 
		System.out.println("연결 객체:"+conn);
		PreparedStatement pstm= null;
		ResultSet rs=null;
		String sql=null; 
		sql = "update newst set money=? where id=?";
		pstm= conn.prepareStatement(sql);
		pstm.setInt(1,this.money);
		pstm.setString(2,ID);
		pstm.executeUpdate();
		
		
	}

	public int getHorse() {
		return horse;
	}

	public void setHorse(int horse) {
		this.horse = horse;
	}

	public int getBettingmoney() {
		return bettingmoney;
	}

	public void setBettingmoney(int bettingmoney) {
	this.bettingmoney=bettingmoney;
	}
	


















}
