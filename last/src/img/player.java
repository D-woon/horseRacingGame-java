package horseracing;

public class player {
  private int money; //µ·
  private int horse; // ¹Ì±¸Çö
  private int bettingmoney; //¹èÆÃÇÑ µ·
  private String name; //ÀÌ¸§

player(int money)
{
	this.money = money;
}
  
  
public int getMoney() {
	return money;
}

public void setMoney(int money) {
	this.money = money;
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
	this.bettingmoney = bettingmoney;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}
  
  
}
