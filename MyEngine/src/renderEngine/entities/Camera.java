package renderEngine.entities;

import org.lwjgl.input.Mouse;

import coreEngine.screen.Window;
import util.interfacing.accessories.KeyboardInput;
import util.interfacing.accessories.Keys;
import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector3f;

public class Camera {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	private Matrix4f projectionM;

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch; // how high or low the camera is aimed (rot around the x axis)
	private float yaw; //how left or right the camera is aimed (rot around the y axis)
	private float roll; //how far the camera is tilted (rot around the z axis)
	
	private boolean followPlayer = false;
	
	private Player player;
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f lastPos = new Vector3f(0,0,0);
	
	public Camera(Player player){
		projectionM = new Matrix4f().initProjection(FOV, Window.getWidth(), Window.getHeight(), NEAR_PLANE, FAR_PLANE);
		this.player = player;
	}
	
	public Camera(){
		projectionM = new Matrix4f().initProjection(FOV, Window.getWidth(), Window.getHeight(), NEAR_PLANE, FAR_PLANE);
	}
	
	public void move(){
//		if(followPlayer)

			moveWithPlayer();
//			System.out.println("LP: " + lastPos.toString() + "\nNP: " + position.toString());
//		else
//			moveIndependant();
		//moveIndependant();
	}
	
	private void moveWithPlayer(){
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		
		float horizontalDistance = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		float verticalDistance = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
		this.yaw = 180 - (player.getRotation().getY() + angleAroundPlayer);
	}
	
	private void moveIndependant(){
		float sVal = 0;
		float fVal = 0;
		float yVal = 0;		
		
		if(KeyboardInput.getKey(Keys.KEY_W)){
			fVal -=0.02f;
		}
		if(KeyboardInput.getKey(Keys.KEY_S)){
			fVal +=0.02f;
		}
		if(KeyboardInput.getKey(Keys.KEY_A)){
			sVal -=0.02f;
		}
		if(KeyboardInput.getKey(Keys.KEY_D)){
			sVal +=0.02f;
		}

		if(KeyboardInput.getKey(Keys.KEY_Q)){
			yVal -= 0.02f;
		}
		if(KeyboardInput.getKey(Keys.KEY_E)){
			yVal += 0.02f;
		}
		
		if(KeyboardInput.getKey(Keys.KEY_UP)){
			pitch -= 0.2f;
		}
		if(KeyboardInput.getKey(Keys.KEY_DOWN)){
			pitch += 0.2f;
		}
		if(KeyboardInput.getKey(Keys.KEY_LEFT)){
			yaw -= 0.2f;
		}
		if(KeyboardInput.getKey(Keys.KEY_RIGHT)){
			yaw += 0.2f;
		}
		
		float x1Val = (float) (sVal * Math.cos(Math.toRadians(yaw)));
		float z1Val = (float) (sVal * Math.sin(Math.toRadians(yaw)));
		
		float x2Val = (float) (fVal * Math.cos(Math.toRadians(90 + yaw)));
		float z2Val = (float) (fVal * Math.sin(Math.toRadians(90 + yaw)));
		
		position.add(new Vector3f(x1Val + x2Val,yVal, z1Val + z2Val));
	}
	
	private void calculateCameraPosition(float horD, float verD){
		float theta = player.getRotation().getY() + angleAroundPlayer;
		float xOff = (float) (horD * Math.sin(Math.toRadians(theta)));
		float zOff = (float) (horD * Math.cos(Math.toRadians(theta)));
		
		position.setY(player.getPosition().getY() + verD);
		position.setX(player.getPosition().getX() - xOff);
		position.setZ(player.getPosition().getZ() - zOff);
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}			
	}
	
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionM;
	}

	public boolean isFollowPlayer() {
		return followPlayer;
	}

	public void setFollowPlayer(boolean followPlayer) {
		this.followPlayer = followPlayer;
	}
	
	public boolean isMoved() {
		if(lastPos.getX() == position.getX() && lastPos.getY() == position.getY() && lastPos.getZ() == position.getZ())
			return false;
		lastPos.set(position);
		return true;
	}
}
