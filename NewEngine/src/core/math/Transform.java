package core.math;

import core.kernel.Camera;

public class Transform {

	private Vec3f translation, rotation, scaling;

	public Transform() {
		translation = new Vec3f(0,0,0);
		rotation = new Vec3f(0,0,0);
		scaling = new Vec3f(1,1,1);
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
	
	public Vec3f getTranslation() {
		return translation;
	}

	public void setTranslation(Vec3f translation) {
		this.translation = translation;
	}

	public Vec3f getRotation() {
		return rotation;
	}

	public void setRotation(Vec3f rotation) {
		this.rotation = rotation;
	}

	public Vec3f getScaling() {
		return scaling;
	}

	public void setScaling(Vec3f scaling) {
		this.scaling = scaling;
	}
}
