
public abstract class Mapping {
	protected long size;
	Domain domain;
	abstract long get(long item);

	
	public Mapping(Domain dom) {
		domain = dom;
	}
	
	public long getSize() {
		return size;
	}
	
	public String toString() {
		String retval = "";
		for (long i = 0;i < size;i++) {
			retval = retval+" "+domain.getString(get(i));
		}
		return retval;
	}


	public long[] asArray() {
		long[] array = new long[(int) getSize()];
		for (int i = 0;i < getSize();i++)
			array[i] = get(i);
		return array;
	}
}
