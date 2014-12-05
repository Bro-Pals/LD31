package horsentp.stp;

/**
	An object defining the properties of the {@link horsentp.stp.DataInputOutput DataInputOutput} input/output files.
	{@link createDefaultDataFileFormat() createDefaultDataFileFormat} should work for most cases; if it does
	not work for your case, create a DataFile format object such that the characters used
	are all different from each other, and none of the specified characters are used inside
	of the data that your Data objects read and write.
*/
public class DataFileFormat {
	/**
		The character that ends a Data object string.
	*/
	private char dataEndCharacter;
	/**
		The character that starts a Data object string.
	*/
	private char dataStartCharacter;
	/**
		The character that separates the property from the value.
	*/
	private char propertyValueSeparator;
	/**
		The character that ends the value string of a property.
	*/
	private char valueEnd;
	
	public DataFileFormat(char dataEndCharacter, char dataStartCharacter, char propertyValueSeparator, char valueEnd) {
		this.dataEndCharacter = dataEndCharacter;
		this.dataStartCharacter = dataStartCharacter;
		this.propertyValueSeparator = propertyValueSeparator;
		this.valueEnd = valueEnd;
	}
	
	public static DataFileFormat createDefaultDataFileFormat() {
		return new DataFileFormat('@', '#', '&', '~');
	}
	
	public char getDataEndCharacter() { return dataEndCharacter; }
	public char getDataStartCharacter() { return dataStartCharacter; }
	public char getPropertyValueSeparator() { return propertyValueSeparator; }
	public char getValueEnd() { return valueEnd; }
}