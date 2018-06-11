package util.maths;

import renderEngine.entities.Camera;
import util.maths.matrices.Matrix4f;
import util.maths.vectors.Vector2f;
import util.maths.vectors.Vector3f;

public class Maths {

	// public static Matrix4f createTransformationMatrix(Vector3f translation,
	// Vector3f rotation, float scale){
	// Matrix4f translationM = new Matrix4f().initTranslation(translation);
	// Matrix4f rotationM = new Matrix4f().initRotation(rotation);
	// Matrix4f scaleM = new Matrix4f().initScaling(new Vector3f(scale, scale,
	// scale));
	//
	// return translationM.mul(rotationM.mul(scaleM));
	// }

	public static Matrix4f createTransformationMatrtix(Vector3f translation, Vector3f rotation, float scale) {

		Matrix4f translationM = new Matrix4f().initTranslation(translation).mul(new Matrix4f().initRotation(rotation)
				.mul(new Matrix4f().initScaling(new Vector3f(scale, scale, scale))));

		return translationM;
	}

	public static Matrix4f createViewMatrtix(Camera camera) {

//		Vector3f negativeCameraPos = camera.getPosition().invert();
//
//		Matrix4f viewM = new Matrix4f().initIdentity()
//				.mul(new Matrix4f().initRotation(new Vector3f(camera.getPitch(), camera.getYaw(), camera.getRoll())))
//				.mul(new Matrix4f().initTranslation(negativeCameraPos));
//
//		return viewM;
		
		org.lwjgl.util.vector.Matrix4f matrix = new org.lwjgl.util.vector.Matrix4f();
		matrix.setIdentity();
		
		org.lwjgl.util.vector.Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), 
				new org.lwjgl.util.vector.Vector3f(1, 0, 0), matrix, matrix);
		org.lwjgl.util.vector.Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), 
				new org.lwjgl.util.vector.Vector3f(0, 1, 0), matrix, matrix);
		org.lwjgl.util.vector.Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), 
				new org.lwjgl.util.vector.Vector3f(0, 0, 1), matrix, matrix);
		
		org.lwjgl.util.vector.Vector3f negativeCameraPos = new 
				org.lwjgl.util.vector.Vector3f(-camera.getPosition().getX(), -camera.getPosition().getY(), 
				-camera.getPosition().getZ());
		
		org.lwjgl.util.vector.Matrix4f.translate(negativeCameraPos, matrix, matrix);
		
		return convertToProgram(matrix);
	}

	public static org.lwjgl.util.vector.Matrix4f convertToOpenGL(Matrix4f matrix) {
		org.lwjgl.util.vector.Matrix4f answer = new org.lwjgl.util.vector.Matrix4f();
		answer.m00 = matrix.get(0, 0);
		answer.m01 = matrix.get(1, 0);
		answer.m02 = matrix.get(2, 0);
		answer.m03 = matrix.get(3, 0);
		
		answer.m10 = matrix.get(0, 1);
		answer.m11 = matrix.get(1, 1);
		answer.m12 = matrix.get(2, 1);
		answer.m13 = matrix.get(3, 1);
		
		answer.m20 = matrix.get(0, 2);
		answer.m21 = matrix.get(1, 2);
		answer.m22 = matrix.get(2, 2);
		answer.m23 = matrix.get(3, 2);
		
		answer.m30 = matrix.get(0, 3);
		answer.m31 = matrix.get(1, 3);
		answer.m32 = matrix.get(2, 3);
		answer.m33 = matrix.get(3, 3);
		
		return answer;
	}

	public static org.lwjgl.util.vector.Vector3f convertToOpenGL(Vector3f vector) {
		org.lwjgl.util.vector.Vector3f answer = new org.lwjgl.util.vector.Vector3f();
		answer.x = vector.getX();
		answer.y = vector.getY();
		answer.z = vector.getZ();
		
		return answer;
	}

	public static Matrix4f convertToProgram(org.lwjgl.util.vector.Matrix4f matrix) {
		Matrix4f answer = new Matrix4f();
		
		answer.set(0, 0, matrix.m00);
		answer.set(0, 1, matrix.m10);
		answer.set(0, 2, matrix.m20);
		answer.set(0, 3, matrix.m30);
		
		answer.set(1, 0, matrix.m01);
		answer.set(1, 1, matrix.m11);
		answer.set(1, 2, matrix.m21);
		answer.set(1, 3, matrix.m31);
		
		answer.set(2, 0, matrix.m02);
		answer.set(2, 1, matrix.m12);
		answer.set(2, 2, matrix.m22);
		answer.set(2, 3, matrix.m32);
		
		answer.set(3, 0, matrix.m03);
		answer.set(3, 1, matrix.m13);
		answer.set(3, 2, matrix.m23);
		answer.set(3, 3, matrix.m33);
		
		return answer;
	}

	public static Vector3f convertToProgram(org.lwjgl.util.vector.Vector3f vector) {
		Vector3f answer = new Vector3f();
		
		answer.setX(vector.x);
		answer.setY(vector.y);
		answer.setZ(vector.z);
		
		return answer;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos){
		float det = (p2.getZ() - p3.getZ()) * (p1.getX() - p3.getX()) + (p3.getX() - p2.getX()) * (p1.getZ() - p3.getZ());
		float l1 = ((p2.getZ() - p3.getZ()) * (pos.getX() - p3.getX()) + (p3.getX() - p2.getX()) * (pos.getY() - p3.getZ())) / det;
		float l2 = ((p3.getZ() - p1.getZ()) * (pos.getX() - p3.getX()) + (p1.getX() - p3.getX()) * (pos.getY() - p3.getZ())) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.getY() + l2 * p2.getY() + l3 * p3.getY();
	}
}
