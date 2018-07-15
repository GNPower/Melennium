package core.maths;

import core.kernel.Camera;

public class Transform {

	private Vector3f translation, rotation, scaling;

	public Transform() {
		translation = new Vector3f(0,0,0);
		rotation = new Vector3f(0,0,0);
		scaling = new Vector3f(1,1,1);
	}
	
	public Matrix4f getWorldMatrix() {
		Matrix4f translationMatrix = new Matrix4f().Translation(translation);
		Matrix4f rotationMatrix = new Matrix4f().Rotation(rotation);
		Matrix4f scalingMatrix = new Matrix4f().Scaling(scaling);
		
		return translationMatrix.mul(scalingMatrix).mul(rotationMatrix);
	}
	
	public Matrix4f getModelMatrix() {
		Matrix4f rotationMatrix = new Matrix4f().Rotation(rotation);
		
		return rotationMatrix;
	}
	
	public Matrix4f getMVPMatrix() {
		return Camera.getInstance().getViewProjectionMatrix().mul(getWorldMatrix());
	}
	
	public Vector3f getTranslation() {
		return translation;
	}

	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScaling() {
		return scaling;
	}

	public void setScaling(Vector3f scaling) {
		this.scaling = scaling;
	}
}
