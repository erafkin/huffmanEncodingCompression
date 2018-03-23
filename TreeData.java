
public class TreeData {
	public char c;
	public int x;
	public TreeData(char c, int x) {
		this.c = c;
		this.x = x;

	}
	public int getInt() {
		return this.x;
	}
	public char getChar() {
		return this.c;
	}
	public void setInt(int x) {
		this.x = x;
	}
	public void setChar(char c) {
		this.c = c;
	}
	@Override
	public String toString() {
		return "character: " + this.c + ", frequency: "+ this.x;
	}

}
