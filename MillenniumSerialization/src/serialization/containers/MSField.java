package serialization.containers;

import static serialization.SerializationUtils.*;

import serialization.ContainerType;
import serialization.Type;

public class MSField extends Container{

	public static final byte CONTAINER_TYPE = ContainerType.FIELD; //DataStorageType (field, array, object)
	public byte type;
	public byte[] data;	
	
	private MSField(){		
	}
	
	public byte getByte(){
		return readByte(data, 0);
	}
	
	public short getShort(){
		return readShort(data, 0);
	}
	
	public char getChar(){
		return readChar(data, 0);
	}
	
	public int getInt(){
		return readInt(data, 0);
	}
	
	public long getLong(){
		return readLong(data, 0);
	}
	
	public double getDouble(){
		return readDouble(data, 0);
	}
	
	public float getFloat(){
		return readFloat(data, 0);
	}
	
	public boolean getBoolean(){
		return readBoolean(data, 0);
	}
	
	public int getBytes(byte[] dest, int pointer){
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, data);
		return pointer;
	}
	
	public int getSize(){
		assert(data.length == Type.getSize(type));
		return 1 + 2 + name.length + 1 + data.length;
		//the container type + nameLength variable (a short) + the length of the name + 
		//the type (byte) + element count (int) + the length of the data (in bytes)
	}
	
	public static MSField Byte(String  name, byte value){
		MSField field = new MSField();
		field.setName(name);
		field.type = Type.BYTE;
		field.data = new byte[Type.getSize(Type.BYTE)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static MSField Short(String  name, short value){
		MSField field = new MSField();
		field.setName(name);
		field.type = Type.SHORT;
		field.data = new byte[Type.getSize(Type.SHORT)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static MSField Char(String  name, char value){
		MSField field = new MSField();
		field.setName(name);
		field.type = Type.CHAR;
		field.data = new byte[Type.getSize(Type.CHAR)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static MSField Integer(String  name, int value){
		MSField field = new MSField();
		field.setName(name);
		field.type = Type.INTEGER;
		field.data = new byte[Type.getSize(Type.INTEGER)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static MSField Long(String  name, long value){
		MSField field = new MSField();
		field.setName(name);
		field.type = Type.LONG;
		field.data = new byte[Type.getSize(Type.LONG)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static MSField Float(String  name, float value){
		MSField field = new MSField();
		field.setName(name);
		field.type = Type.FLOAT;
		field.data = new byte[Type.getSize(Type.FLOAT)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static MSField Double(String  name, double value){
		MSField field = new MSField();
		field.setName(name);
		field.type = Type.DOUBLE;
		field.data = new byte[Type.getSize(Type.DOUBLE)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static MSField Boolean(String  name, boolean value){
		MSField field = new MSField();
		field.setName(name);
		field.type = Type.BOOLEAN;
		field.data = new byte[Type.getSize(Type.BOOLEAN)];
		writeBytes(field.data, 0, value);
		return field;
	}
	
	public static MSField deserialize(byte[] data, int pointer){
		byte containerType = readByte(data, pointer++);
		assert(containerType == CONTAINER_TYPE);
		
		MSField result = new MSField();
		
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.type = readByte(data, pointer++);
		
		result.data = new byte[Type.getSize(result.type)];
		readBytes(data, pointer, result.data);
		pointer += Type.getSize(result.type);
		
		return result;
	}
}
