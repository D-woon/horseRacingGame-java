package horseracing;

public class horse {
  private double speed; //말을 속도
  private double drate; // 배당률

  horse(double speed,double drate)
  {
	  this.speed = speed;
      this.drate = drate;
  }
  
public double getSpeed() {
	return speed;
}

public void setSpeed(double speed) {
	this.speed = speed;
}

public double getDrate() {
	return drate;
}

public void setDrate(double drate) {
	this.drate = drate;
}
  
  
}
