package serialization.containers;

import static serialization.SerializationUtils.*;
import serialization.ContainerType;
import serialization.Type;

public class MSArray extends Container{

	public static final byte CONTAINER_TYPE = ContainerType.ARRAY; //DataStorageType (field, array, object)
	public byte type;
	public int count;
	public byte[] data;
	
	public short[] shortData;
	public char[] charData;
	public int[] intData;	
	public long[] longData;
	public float[] floatData;
	public double[] doubleData;
	public boolean[] booleanData;
	
	private MSArray(){	
		size += 1 + 1 + 4;
	}
	
	private void updateSize(){
		size += getDataSize();
	}
	
	public int getBytes(byte[] dest, int pointer){
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		pointer = writeBytes(dest, pointer, type);
		pointer = writeBytes(dest, pointer, count);
		
		switch(type){
		case Type.BYTE:
			pointer = writeBytes(dest, pointer, data);
			break;
		case Type.SHORT:
			pointer = writeBytes(dest, pointer, shortData);
			break;
		case Type.CHAR:
			pointer = writeBytes(dest, pointer, charData);
			break;
		case Type.INTEGER:
			pointer = writeBytes(dest, pointer, intData);
			break;
		case Type.LONG:
			pointer = writeBytes(dest, pointer, longData);
			break;
		case Type.FLOAT:
			pointer = writeBytes(dest, pointer, floatData);
			break;
		case Type.DOUBLE:
			pointer = writeBytes(dest, pointer, doubleData);
			break;
		case Type.BOOLEAN:
			pointer = writeBytes(dest, pointer, booleanData);
			break;
		}		
		return pointer;
	}
	
	public int getSize(){		
		return size;
	}
	
	public int getDataSize(){
		switch(type){
		case Type.BYTE:		return data.length * Type.getSize(Type.BYTE);
		case Type.SHORT:	return shortData.length * Type.getSize(Type.SHORT);
		case Type.CHAR:		return charData.length * Type.getSize(Type.CHAR);
		case Type.INTEGER:	return intData.length * Type.getSize(Type.INTEGER);
		case Type.LONG:		return longData.length * Type.getSize(Type.LONG);
		case Type.FLOAT:	return floatData.length * Type.getSize(Type.FLOAT);
		case Type.DOUBLE:	return doubleData.length * Type.getSize(Type.DOUBLE);
		case Type.BOOLEAN:	return booleanData.length * Type.getSize(Type.BOOLEAN); 
		}
		return 0;
	}
	
	public static MSArray Byte(String  name, byte[] data){
		MSArray array = new MSArray();
		array.setName(name);
		array.type = Type.BYTE;
		array.count = data.length;
		array.data = data;
		array.updateSize();
		return array;
	}
	
	public static MSArray Short(String  name, short[] data){
		MSArray array = new MSArray();
		array.setName(name);
		array.type = Type.SHORT;
		array.count = data.length;
		array.shortData = data;
		array.updateSize();
		return array;
	}
	
	public static MSArray Char(String  name, char[] data){
		MSArray array = new MSArray();
		array.setName(name);
		array.type = Type.CHAR;
		array.count = data.length;
		array.charData = data;
		array.updateSize();
		return array;
	}
	
	public static MSArray Integer(String  name, int[] data){
		MSArray array = new MSArray();
		array.setName(name);
		array.type = Type.INTEGER;
		array.count = data.length;
		array.intData = data;
		array.updateSize();
		return array;
	}
	
	public static MSArray Long(String  name, long[] data){
		MSArray array = new MSArray();
		array.setName(name);
		array.type = Type.LONG;
		array.count = data.length;
		array.longData = data;
		array.updateSize();
		return array;
	}
	
	public static MSArray Float(String  name, float[] data){
		MSArray array = new MSArray();
		array.setName(name);
		array.type = Type.FLOAT;
		array.count = data.length;
		array.floatData = data;
		array.updateSize();
		return array;
	}
	
	public static MSArray Double(String  name, double[] data){
		MSArray array = new MSArray();
		array.setName(name);
		array.type = Type.DOUBLE;
		array.count = data.length;
		array.doubleData = data;
		array.updateSize();
		return array;
	}
	
	public static MSArray Boolean(String  name, boolean[] data){
		MSArray array = new MSArray();
		array.setName(name);
		array.type = Type.BOOLEAN;
		array.count = data.length;
		array.booleanData = data;
		array.updateSize();
		return array;
	}
	
	public static MSArray deserialize(byte[] data, int pointer){
		byte containerType = readByte(data, pointer++);
		assert(containerType == CONTAINER_TYPE);
		
		MSArray result = new MSArray();
		
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.type = readByte(data, pointer++);
		
		result.count = readInt(data, pointer);
		pointer += 4;
		
		switch(result.type){
		case Type.BYTE:
			result.data = new byte[result.count];
			readBytes(data, pointer, result.data);
			break;
		case Type.SHORT:
			result.shortData = new short[result.count];
			readShorts(data, pointer, result.shortData);
			break;
		case Type.CHAR:
			result.charData = new char[result.count];
			readChars(data, pointer, result.charData);
			break;
		case Type.INTEGER:
			result.intData = new int[result.count];
			readInts(data, pointer, result.intData);
			break;
		case Type.LONG:
			result.longData = new long[result.count];
			readLongs(data, pointer, result.longData);
			break;
		case Type.FLOAT:
			result.floatData = new float[result.count];
			readFloats(data, pointer, result.floatData);
			break;
		case Type.DOUBLE:
			result.doubleData = new double[result.count];
			readDoubles(data, pointer, result.doubleData);
			break;
		case Type.BOOLEAN:
			result.booleanData = new boolean[result.count];
			readBooleans(data, pointer, result.booleanData);
			break;
		}	
		pointer += result.count * Type.getSize(result.type);
		
		return result;
	}
	
	public String getString(){
		String result = "";
		switch(type){
		case Type.BYTE:
			result += "[";
			for(int i = 0; i < data.length; i++){
				result += data[i];
				result += ", ";
			}
			result += "]";
			break;
		case Type.SHORT:
			result += "[";
			for(int i = 0; i < shortData.length; i++){
				result += shortData[i];
				result += ", ";
			}
			result += "]";
			break;
		case Type.CHAR:
			result += "[";
			for(int i = 0; i < charData.length; i++){
				result += charData[i];
				result += ", ";
			}
			result += "]";
			break;
		case Type.INTEGER:
			result += "[";
			for(int i = 0; i < intData.length; i++){
				result += intData[i];
				result += ", ";
			}
			result += "]";
			break;
		case Type.LONG:
			result += "[";
			for(int i = 0; i < longData.length; i++){
				result += longData[i];
				result += ", ";
			}
			result += "]";
			break;
		case Type.FLOAT:
			result += "[";
			for(int i = 0; i < floatData.length; i++){
				result += floatData[i];
				result += ", ";
			}
			result += "]";
			break;
		case Type.DOUBLE:
			result += "[";
			for(int i = 0; i < doubleData.length; i++){
				result += doubleData[i];
				result += ", ";
			}
			result += "]";
			break;
		case Type.BOOLEAN:
			result += "[";
			for(int i = 0; i < booleanData.length; i++){
				result += booleanData[i];
				result += ", ";
			}
			result += "]";
			break;
		}
		return result;
	}
}
