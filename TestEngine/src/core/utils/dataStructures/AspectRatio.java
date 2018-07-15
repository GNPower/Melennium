package core.utils.dataStructures;

public class AspectRatio {

	private int wFactor, hFactor;
	
	public AspectRatio(int wFactor, int hFactor) {
		this.wFactor = wFactor;
		this.hFactor = hFactor;
	}
	
	public String getAspectRatio() {
		return wFactor + ":" + hFactor;
	}
	
	public double getAspect() {
		return (double) wFactor / (double) hFactor;
	}
}
