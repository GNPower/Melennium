package renderEngine.entities;

import renderEngine.models.Model;
import renderEngine.terrains.Terrain;
import util.interfacing.accessories.KeyboardInput;
import util.interfacing.accessories.Keys;
import util.maths.vectors.Vector3f;
import util.time.Time;

public class Player extends Entity{
	
	private static final float RUN_SPEED = 60;
	private static final float TURN_SPEED = 60;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;

	private float currentSpeed = 0;
	private float currentTurn = 0;
	private float upSpeed = 0;
	
	private boolean isInAir = false;
	
	public Player(Model model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}

	public void move(Terrain terrain){
//		if(!camera.isFollowPlayer())
//			return;
		checkInputs();
		super.increaseRotation(0, currentTurn * Time.getDelta(), 0);
		float distance = currentSpeed * Time.getDelta();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotation().getY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotation().getY())));
		super.increasePosition(dx, 0, dz);
		upSpeed += GRAVITY * Time.getDelta();
		super.increasePosition(0, upSpeed * Time.getDelta(), 0);
		
		float terrainHeight = terrain.getHeight(super.getPosition().getX(), super.getPosition().getZ());
		if(super.getPosition().getY() < terrainHeight){
			upSpeed = 0;
			isInAir = false;
			super.getPosition().setY(terrainHeight);
		}
	}
	
	private void jump(){
		if(isInAir)
			return;
		this.upSpeed = JUMP_POWER;
		isInAir = true;
	}
	
	public void checkInputs(){
		currentSpeed = 0;
		currentTurn = 0;
		if(KeyboardInput.getKey(Keys.KEY_W)){
			currentSpeed += RUN_SPEED;
		}
		if(KeyboardInput.getKey(Keys.KEY_S)){
			currentSpeed += -RUN_SPEED;
		}
		if(KeyboardInput.getKey(Keys.KEY_D)){
			currentTurn -= TURN_SPEED;
		}
		if(KeyboardInput.getKey(Keys.KEY_A)){
			currentTurn += TURN_SPEED;
		}
		
		if(KeyboardInput.getKey(Keys.KEY_SPACE)){
			jump();
		}
	}
}
