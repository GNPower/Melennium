package serialization.containers;

import static serialization.SerializationUtils.*;

import java.util.ArrayList;
import java.util.List;

import serialization.ContainerType;

public class MSObject extends Container{

	public static final byte CONTAINER_TYPE = ContainerType.OBJECT; //DataStorageType (field, array, object)
	
	private short fieldCount;
	public List<MSField> fields = new ArrayList<MSField>();
	private short stringCount;
	public List<MSString> strings = new ArrayList<MSString>();
	private short arrayCount;
	public List<MSArray> arrays = new ArrayList<MSArray>();		
		
	public MSObject(String name){
		setName(name);
		size += 1 + 2 + 2 + 2;
	}
	
	private MSObject(){		
	}
	
	public void addField(MSField field){
		fields.add(field);
		size += field.getSize();
		
		fieldCount = (short) fields.size();
	}
	
	public void addString(MSString string){
		strings.add(string);
		size += string.getSize();
		
		stringCount = (short) strings.size();
	}
	
	public void addArray(MSArray array){
		arrays.add(array);
		size += array.getSize();
		
		arrayCount = (short) arrays.size();
	}
	
	public int getSize(){
//		int size = 1 + 2 + name.length;
//		for(MSField field : fields){
//			size += field.getSize();
//		}
//		for(MSArray array : arrays){
//			size += array.getSize();
//		}
		return size;
	}
	
	public int getBytes(byte[] dest, int pointer){
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, fieldCount);
		for(MSField field : fields){
			pointer = field.getBytes(dest, pointer);
		}
		
		pointer = writeBytes(dest, pointer, stringCount);
		for(MSString string : strings){
			pointer = string.getBytes(dest, pointer);
		}
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for(MSArray array : arrays){
			pointer = array.getBytes(dest, pointer);
		}
		return pointer;
	}
	
	public MSField findField(String name){
		for(MSField field : fields){
			if(field.getName().equals(name))
				return field;
		}
		return null;
	}
	
	public MSArray findArray(String name){
		for(MSArray array : arrays){
			if(array.getName().equals(name))
				return array;
		}
		return null;
	}
	
	public MSString findString(String name){
		for(MSString string : strings){
			if(string.getName().equals(name))
				return string;
		}
		return null;
	}
	
	public static MSObject deserialize(byte[] data, int pointer){
		byte containerType = readByte(data, pointer++);
		assert(containerType == CONTAINER_TYPE);
		
		MSObject result = new MSObject();
		
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		//Early out
		//pointer += result.size - sizeOffset - result.nameLength;
		
		result.fieldCount = readShort(data, pointer);
		pointer += 2;
		
		for(int i = 0; i < result.fieldCount; i++){
			MSField field = MSField.deserialize(data, pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readShort(data, pointer);
		pointer += 2;
		
		for(int i = 0; i < result.stringCount; i++){
			MSString string = MSString.deserialize(data, pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}
		
		result.arrayCount = readShort(data, pointer);
		pointer += 2;
		
		for(int i = 0; i < result.arrayCount; i++){
			MSArray array = MSArray.deserialize(data, pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}
		
		return result;
	}
}
