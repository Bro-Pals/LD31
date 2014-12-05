package horsentp.stp;

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/**
	An object that handles the file input/output of data. Each data file
	that is given contains a list of 
*/
public abstract class DataInputOutput<T> {
	
	private DataFileFormat format;
	
	public DataInputOutput() {
		format = DataFileFormat.createDefaultDataFileFormat();
	}
	
	public DataInputOutput(DataFileFormat format) {
		this.format = format;
	}
	
	public abstract String[] getPropertyList();
	
	/**
		@returns the data skeleton to build off of.
	*/
	protected abstract T createDefaultObject();
	
	/**
		Writes a property/value pair to a text file.
		@param data the object that is being written.
		@param property the property identifier
		@returns the value that pertains to this property identifier: left entirely
		up to the implementation.
	*/
	public abstract String writeProperty(T data, String property);
	
	/**
		Get a property/value pair to convert into a format that this DataInputOutput's
		generated data object can use.
		@param data the object that having data loaded into it.
		@param property the property identifier
		@param value the property's value
	*/
	public abstract void readProperty(T data, String property, String value);
	
	/**
		Read a data file, extracting from it the list of objects
		that it stored.
	*/
	public ArrayList<T> readFile(File file) {
		try {
			ArrayList<T> list = new ArrayList<T>();
			BufferedReader rdr = new BufferedReader(new FileReader(file));
			char c;
			int b;
			String propertyString = null;
			String buffer = "";
			b = rdr.read();
			T current = null;
			while(b!=-1) {
				c = (char)b;
				if (c == format.getDataStartCharacter()) {
					current = createDefaultObject();
					buffer = "";
				} else if (c == format.getPropertyValueSeparator()) {
					propertyString = buffer;
					buffer = "";
				} else if (c == format.getValueEnd()) {
					readProperty(current, propertyString, buffer);
					buffer = "";
				} else if (c == format.getDataEndCharacter()) {
					//Should have finished reading all of the properties
					list.add(current);
					current = null;
					buffer = "";
				} else {
					buffer += c;
				}
				b = rdr.read();
			}
			rdr.close();
			System.out.println("Successfully read data file " + file.toString());
			return list;
		} catch(Exception e) {
			System.err.println("Unable to read data file " + file.toString() + ": " + e.toString());
			return null;
		}
	}
	
	/**
		Writes what is contained in the list of data objects.
	*/
	public void writeFile(File file, ArrayList<T> list) {
		try {
			PrintWriter writer = new PrintWriter(file);
			for (T t : list) {
				writer.print(format.getDataStartCharacter());
				for (String property : getPropertyList()) {
					writer.print(property);
					writer.print(format.getPropertyValueSeparator());
					writer.print(writeProperty(t, property));
					writer.print(format.getValueEnd());
				}
				writer.print(format.getDataEndCharacter());
			}
			writer.flush();
			writer.close();
			System.out.println("Successfully wrote data file " + file.toString());
		} catch(Exception e) {
			System.err.println("Unable to wrote data file " + file.toString() + ": " + e.toString());
		}
	}
}