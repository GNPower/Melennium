package serialization.containers;

import static serialization.SerializationUtils.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import serialization.ContainerType;

public class MSDatabase extends Container{

	public static final byte[] HEADER = "MSDB".getBytes();
	public static final short VERSION = 0x0001;
	
	public static final byte CONTAINER_TYPE = ContainerType.DATABASE; //DataStorageType (field, array, object)
	
	private short objectCount;
	public List<MSObject> objects = new ArrayList<MSObject>();
	
	public MSDatabase(String name){
		setName(name);
		size += HEADER.length + 2 + 1 + 2;
	}
	
	private MSDatabase(){		
	}
	
	public void addObject(MSObject object){
		objects.add(object);
		size += object.getSize();
		
		objectCount = (short) objects.size();
	}
	
	public void clearDatabase(){
		objects.clear();
		objectCount = (short) 0;
	}
	
	public int getSize(){
		return size;
	}
	
	public int getBytes(byte[] dest, int pointer){
		pointer = writeBytes(dest, pointer, HEADER);
		pointer = writeBytes(dest, pointer, VERSION);
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);		
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, objectCount);
		for(MSObject object : objects){
			pointer = object.getBytes(dest, pointer);
		}

		return pointer;
	}
	
	public static MSDatabase deserialize(byte[] data){
		int pointer = 0;
		
		String header = readString(data, pointer, HEADER.length);
		assert(header.equals(HEADER));
		pointer += HEADER.length;		
		
		if(readShort(data, pointer) != VERSION){
			System.err.println("Invalid MS version!");
			return null;
		}
		pointer += 2;
		
		byte containerType = readByte(data, pointer++);
		assert(containerType == CONTAINER_TYPE);
		
		MSDatabase result = new MSDatabase();
		
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		result.objectCount = readShort(data, pointer);
		pointer += 2;
		
		for(int i = 0; i < result.objectCount; i++){		
			MSObject object = MSObject.deserialize(data, pointer);
			result.objects.add(object);
			pointer += object.getSize();
		}
		
		return result;
	}
	
	public MSObject findObject(String name){
		for(MSObject object : objects){
			if(object.getName().equals(name))
				return object;
		}
		return null;
	}
	
	public static MSDatabase deserializeFromFile(String path) {
		byte[] buffer = null;
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
			buffer = new byte[stream.available()];
			stream.read(buffer);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deserialize(buffer);
	}
	
	public void serializeToFile(String path){
		byte[] data = new byte[getSize()];
		getBytes(data, 0);
		
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
			stream.write(data);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
