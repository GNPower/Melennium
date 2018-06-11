package util.cycles;

import util.time.Time;

public class Cycle {

	private float cycleTime;
	private float lastTime;
	
	public Cycle(float cycleTime){
		this.cycleTime = cycleTime;
		lastTime = Time.getTime() / Time.SECOND;
	}
	
	public boolean update(){
		float currentTime = Time.getTime() / Time.SECOND;
		float difference = currentTime - lastTime;
		if(difference > cycleTime){
			lastTime = currentTime;
			return true;
		}
		return false;
	}

	public float getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(float cycleTime) {
		this.cycleTime = cycleTime;
	}
}
