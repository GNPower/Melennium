package util.time;

public class Timer {

	private double cycleTime;
	private long lastCycleTime = 0;
	private boolean hasCycled = false;
	
	public Timer(double cycleTimeSeconds) {
		cycleTime = cycleTimeSeconds;
	}
	
	public boolean update() {
		if((Time.getTime() / Time.SECOND) - (lastCycleTime / Time.SECOND) >= cycleTime) {
			hasCycled = true;
			lastCycleTime = Time.getTime();
		}
		else
			hasCycled = false;
		
		return hasCycled;
	}
}
