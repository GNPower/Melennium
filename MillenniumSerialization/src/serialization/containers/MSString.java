package serialization.containers;

import static serialization.SerializationUtils.*;

import serialization.ContainerType;
import serialization.Type;

public class MSString extends Container{

	public static final byte CONTAINER_TYPE = ContainerType.STRING; //DataStorageType (field, array, object)
	public int count;
	public char[] characters;
	
	private MSString(){	
		size += 1 + 4;
	}
	
	public String getString(){
		return new String(characters);
	}
	
	private void updateSize(){
		size += getDataSize();
	}
	
	public int getBytes(byte[] dest, int pointer){
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		pointer = writeBytes(dest, pointer, count);
		pointer = writeBytes(dest, pointer, characters);
		
		return pointer;
	}
	
	public int getSize(){		
		return size;
	}
	
	public int getDataSize(){
		return characters.length * Type.getSize(Type.CHAR);
	}
	
	public static MSString Create(String  name, String data){
		MSString string = new MSString();
		string.setName(name);
		string.count = data.length();
		string.characters = data.toCharArray();
		string.updateSize();
		return string;
	}
	
	public static MSString deserialize(byte[] data, int pointer){
		byte containerType = readByte(data, pointer++);
		assert(containerType == CONTAINER_TYPE);
		
		MSString result = new MSString();
		
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.count = readInt(data, pointer);
		pointer += 4;
		
		result.characters = new char[result.count];
		readChars(data, pointer, result.characters);
		pointer += result.count * Type.getSize(Type.CHAR);
		
		return result;
	}
}
