package test;

import serialization.Type;
import serialization.containers.MSArray;
import serialization.containers.MSDatabase;
import serialization.containers.MSField;
import serialization.containers.MSObject;
import serialization.containers.MSString;

public class Test {

	public static void main(String[] args) {
		
		MSDatabase database = new MSDatabase("name");
		MSObject object = new MSObject("object1");
		object.addString(MSString.Create("string01", "testData"));
		object.addArray(MSArray.Float("positions", new float[] {5.0f, 0.0f, -5.0f}));		
		database.addObject(object);		
		dump(database);
	}
	
	private static void dump(MSDatabase database){
		System.out.println("--------------------------------");		
		System.out.println("           MSDatabase           ");
		System.out.println("--------------------------------");
		System.out.println("Name: " + database.getName());
		System.out.println("Size: " + database.getSize());
		System.out.println("Object Count: " + database.objects.size());
		for(MSObject object : database.objects){
			System.out.println("\tObject:");
			System.out.println("\tName: " + object.getName());
			System.out.println("\tSize: " + object.getSize());
			System.out.println("\tField Count: " + object.fields.size());
			for(MSField field : object.fields){
				System.out.println("\t\tField:");
				System.out.println("\t\t\tName: " + field.getName());
				System.out.println("\t\t\tSize: " + field.getSize());
				System.out.println("\t\t\tField Count: " + object.fields.size());
				String data = "";
				switch(field.type){
				case Type.BYTE:
					data += field.getByte();
					break;
				case Type.SHORT:
					data += field.getShort();
					break;
				case Type.CHAR:
					data += field.getChar();
					break;
				case Type.INTEGER:
					data += field.getInt();
					break;
				case Type.LONG:
					data += field.getLong();
					break;
				case Type.FLOAT:
					data += field.getFloat();
					break;
				case Type.DOUBLE:
					data += field.getDouble();
					break;
				case Type.BOOLEAN:
					data += field.getBoolean();
					break;
				}
				System.out.println("\t\t\tData: " + data);
			}
			System.out.println();
			for(MSArray array : object.arrays){
				System.out.println("\t\tArray:");
				System.out.println("\t\t\tName: " + array.getName());
				System.out.println("\t\t\tSize: " + array.getSize());
				System.out.println("\t\t\tData: " + array.getString());
			}
			for(MSString string : object.strings) {
				System.out.println("\t\tString:");
				System.out.println("\t\t\tName: " + string.getName());
				System.out.println("\t\t\tData: " + string.getString());
			}
			System.out.println();
		}
		System.out.println("--------------------------------");
	}

}
