package data_mining.p1.converters;

public class NearestImageDataConverter {

	public boolean convertFromCharacterValue(char ch) {
		return ch == '1';
	}

	public char convertToCharacterValue(boolean value) {
		return value ? '1' : '0';
	}

}
