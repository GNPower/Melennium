package core.kernel;

import org.lwjgl.input.Mouse;

import core.kernel.input.Input;
import core.kernel.input.Keys;
import core.maths.Matrix4f;
import core.maths.Quaternion;
import core.maths.Vector3f;
import core.utils.Constants;

public class Camera {
	
	private static Camera instance = null;

	private final Vector3f yAxis = new Vector3f(0,1,0);
	
	private Vector3f position;
	private Vector3f previousPosition;
	private Vector3f forward;
	private Vector3f previousForward;
	private Vector3f up;
	private float movAmt = 0.1f;
	private float maxSpeed = 24.0f;
	private float rotAmt = 0.8f;
	private float maxRot = 0.02f;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Matrix4f viewProjectionMatrix;
	private Matrix4f previousViewMatrix;
	private Matrix4f previousViewProjectionMatrix;
	private boolean cameraMoved;
	private boolean cameraRotated;
	
	private float width;
	private float height;
	private float fovY;

	private float rotYstride;
	private float rotYamt;
	private float rotYcounter;
	private boolean rotYInitiated = false;
	private float rotXstride;
	private float rotXamt;
	private float rotXcounter;
	private boolean rotXInitiated = false;
	private float mouseSensitivity = 0.8f;
	
	private Quaternion[] frustumPlanes = new Quaternion[6];
	private Vector3f[] frustumCorners = new Vector3f[8];
	  
	public static Camera getInstance() 
	{
	    if(instance == null) 
	    {
	    	instance = new Camera();
	    }
	      return instance;
	}
	
	protected Camera()
	{
		this(new Vector3f(-134,109,-43), new Vector3f(-0.915f,-0.36f,0.183f).normalize(), new Vector3f(-0.353f,0.933f,0.071f));
		setProjection(70, Window.getInstance().getWidth(), Window.getInstance().getHeight());
		setViewMatrix(new Matrix4f().View(this.getForward(), this.getUp()).mul(
				new Matrix4f().Translation(this.getPosition().mul(-1))));
		previousViewMatrix = new Matrix4f().Zero();
		viewProjectionMatrix = new Matrix4f().Zero();
		previousViewProjectionMatrix = new Matrix4f().Zero();
	}
	
	private Camera(Vector3f position, Vector3f forward, Vector3f up)
	{
		setPosition(position);
		setForward(forward);
		setUp(up);
		up.normalize();
		forward.normalize();
	}
	
	public void update()
	{
		setPreviousPosition(new Vector3f(position));
		setPreviousForward(new Vector3f(forward));
		cameraMoved = false;
		cameraRotated = false;
		
		movAmt += (0.004f * Input.getInstance().getScrollOffset());
		if(movAmt < 0.2f)
			movAmt = 0.2f;
		movAmt = Math.min(maxSpeed, movAmt);
		
		if(Input.getInstance().getKey(Keys.KEY_W))
			move(getForward(), movAmt);
		if(Input.getInstance().getKey(Keys.KEY_S))
			move(getForward(), -movAmt);
		if(Input.getInstance().getKey(Keys.KEY_A))
			move(getLeft(), movAmt);
		if(Input.getInstance().getKey(Keys.KEY_D))
			move(getRight(), movAmt);
				
		if(Input.getInstance().getKey(Keys.KEY_UP))
			rotateX(-rotAmt/8f);
		if(Input.getInstance().getKey(Keys.KEY_DOWN))
			rotateX(rotAmt/8f);
		if(Input.getInstance().getKey(Keys.KEY_LEFT))
			rotateY(-rotAmt/8f);
		if(Input.getInstance().getKey(Keys.KEY_RIGHT))
			rotateY(rotAmt/8f);
		
		// free mouse rotation
		if(Input.getInstance().isButtonPushed(2))
		{
			float dy = Input.getInstance().getLockedCursorPosition().getY() - Input.getInstance().getCursorPosition().getY();
			float dx = Input.getInstance().getLockedCursorPosition().getX() - Input.getInstance().getCursorPosition().getX();
			
			// y-axxis rotation
			
			if (dy != 0){
				rotYstride = Math.abs(dy * 0.01f);
				rotYamt = -dy;
				rotYcounter = 0;
				rotYInitiated = true;
			}
			
			if (rotYInitiated ){
				
				// up-rotation
				if (rotYamt < 0){
					if (rotYcounter > rotYamt){
						rotateX(-rotYstride * mouseSensitivity);
						rotYcounter -= rotYstride;
						rotYstride *= 0.98;
					}
					else rotYInitiated = false;
				}
				// down-rotation
				else if (rotYamt > 0){
					if (rotYcounter < rotYamt){
						rotateX(rotYstride * mouseSensitivity);
						rotYcounter += rotYstride;
						rotYstride *= 0.98;
					}
					else rotYInitiated = false;
				}
			}
			
			// x-axxis rotation
			if (dx != 0){
				rotXstride = Math.abs(dx * 0.01f);
				rotXamt = dx;
				rotXcounter = 0;
				rotXInitiated = true;
			}
			
			if (rotXInitiated){
				
				// up-rotation
				if (rotXamt < 0){
					if (rotXcounter > rotXamt){
						rotateY(rotXstride * mouseSensitivity);
						rotXcounter -= rotXstride;
						rotXstride *= 0.96;
					}
					else rotXInitiated = false;
				}
				// down-rotation
				else if (rotXamt > 0){
					if (rotXcounter < rotXamt){
						rotateY(-rotXstride * mouseSensitivity);
						rotXcounter += rotXstride;
						rotXstride *= 0.96;
					}
					else rotXInitiated = false;
				}
			}
			
			Mouse.setCursorPosition((int)Input.getInstance().getLockedCursorPosition().getX(), 
					(int)Input.getInstance().getLockedCursorPosition().getY());
		}
		
		if (!position.equals(previousPosition)){
			cameraMoved = true;	
		}
		
		if (!forward.equals(previousForward)){
			cameraRotated = true;
		}
		
		setPreviousViewMatrix(viewMatrix);
		setPreviousViewProjectionMatrix(viewProjectionMatrix);
		setViewMatrix(new Matrix4f().View(this.getForward(), this.getUp()).mul(
				new Matrix4f().Translation(this.getPosition().mul(-1))));
		setViewProjectionMatrix(projectionMatrix.mul(viewMatrix));
		
//		System.out.println("X: " + position.getX() + "\tY: " + position.getY() + "\tZ: " + position.getZ());
	}
	
	public void move(Vector3f dir, float amount)
	{
		Vector3f newPos = position.add(dir.mul(amount));	
		setPosition(newPos);
	}
	
	public void rotateY(float angle)
	{
		Vector3f hAxis = yAxis.cross(forward).normalize();
		
		forward.rotate(angle, yAxis).normalize();
		
		up = forward.cross(hAxis).normalize();
	}
	
	public void rotateX(float angle)
	{
		Vector3f hAxis = yAxis.cross(forward).normalize();

		forward.rotate(angle, hAxis).normalize();
		
		up = forward.cross(hAxis).normalize();
	}
	
	public Vector3f getLeft()
	{
		Vector3f left = forward.cross(up);
		left.normalize();
		return left;
	}
	
	public Vector3f getRight()
	{
		Vector3f right = up.cross(forward);
		right.normalize();
		return right;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}
	
	public  void setProjection(float fovY, float width, float height)
	{
		this.fovY = fovY;
		this.width = width;
		this.height = height;
		
		this.projectionMatrix = new Matrix4f().PerspectiveProjection(fovY, width, height, Constants.ZNEAR, Constants.ZFAR);
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getForward() {
		return forward;
	}

	public void setForward(Vector3f forward) {
		this.forward = forward;
	}

	public Vector3f getUp() {
		return up;
	}

	public void setUp(Vector3f up) {
		this.up = up;
	}

	public Quaternion[] getFrustumPlanes() {
		return frustumPlanes;
	}
	
	public float getFovY(){
		return this.fovY;
	}
	
	public float getWidth(){
		return this.width;
	}

	public float getHeight(){
		return this.height;
	}
	
	public void setViewProjectionMatrix(Matrix4f viewProjectionMatrix) {
		this.viewProjectionMatrix = viewProjectionMatrix;
	}
	
	public Matrix4f getViewProjectionMatrix() {
		return viewProjectionMatrix;
	}

	public Matrix4f getPreviousViewProjectionMatrix() {
		return previousViewProjectionMatrix;
	}

	public void setPreviousViewProjectionMatrix(
			Matrix4f previousViewProjectionMatrix) {
		this.previousViewProjectionMatrix = previousViewProjectionMatrix;
	}

	public Matrix4f getPreviousViewMatrix() {
		return previousViewMatrix;
	}

	public void setPreviousViewMatrix(Matrix4f previousViewMatrix) {
		this.previousViewMatrix = previousViewMatrix;
	}

	public Vector3f[] getFrustumCorners() {
		return frustumCorners;
	}

	public boolean isCameraMoved() {
		return cameraMoved;
	}

	public boolean isCameraRotated() {
		return cameraRotated;
	}
	
	public Vector3f getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(Vector3f previousPosition) {
		this.previousPosition = previousPosition;
	}
	
	public Vector3f getPreviousForward() {
		return previousForward;
	}

	private void setPreviousForward(Vector3f previousForward) {
		this.previousForward = previousForward;
	}
}