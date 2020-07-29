package horseracing;

public class horse {
  private double speed;
  private double drate;

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
