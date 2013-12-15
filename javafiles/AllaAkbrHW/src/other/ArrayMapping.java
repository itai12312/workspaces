
public class ArrayMapping extends Mapping {

	private long[] array;
	private long visibleItems;

	public ArrayMapping(Domain dom) {
		super(dom);
	}
	
	public void setArray(long[] arr) {
		array = arr;
		size = arr.length;
		visibleItems = size;
	}
	
	public void setArray(long[] arr, long lim) {
		array = arr;
		size = arr.length;
		visibleItems = lim;
	}

	public long get(long item) {
		if (item >= visibleItems)
			return -1;
		return array[(int)item];
	}

}
