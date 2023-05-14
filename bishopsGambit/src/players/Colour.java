package players;

public enum Colour {
	WHITE, BLACK;

	@Override
	public String toString() {
		return name().charAt(0) + name().substring(1).toLowerCase();
	}
}
