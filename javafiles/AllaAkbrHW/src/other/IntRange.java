
public class IntRange implements Domain{

	private int min;
	private int max;

	public IntRange(int a, int b) {
		min = a;
		max = b;
	}

	public long size() {
		return max-min+1;
	}

	public long decode(long idx) {
		return idx+min;
	}

	public String getString(long idx) {
		return String.valueOf(idx+min);
	}

	public long encode(long i) {
		return i-min;
	}

}
