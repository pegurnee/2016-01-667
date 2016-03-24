
public enum FileType {
	CLASSIFIED("classified"),
	TESTING("test"),
	TRAINING("train"),
	VALIDATION("validate");

	private final String name;

	private FileType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
