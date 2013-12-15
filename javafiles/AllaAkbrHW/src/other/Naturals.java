import java.util.Iterator;


public class Naturals implements Iterable<Long>{

	public long max;
	public long inc;
	public long rem;

	public Naturals(long i) {
		max = i;
		inc = 1;
		rem = 0;
	}

	public Iterator<Long> iterator() {
		return new NaturalsIter(this);
	}

	public void addLimiter(ModuloLimiter lim) {
		if (inc != 1)
			throw new RuntimeException("Only one ModuloLimiter is allowed");
		inc = lim.mod;
		rem = lim.rem;
	}

}
