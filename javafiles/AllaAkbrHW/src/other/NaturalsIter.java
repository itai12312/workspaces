import java.util.Iterator;


public class NaturalsIter implements Iterator<Long> {

	private long max;
	private long lastReturned;
	private long inc;

	public NaturalsIter(Naturals nats) {
		max = nats.max;
		lastReturned = nats.rem-nats.inc;
		inc = nats.inc;
	}

	public boolean hasNext() {
		return (lastReturned <= max-inc);
	}

	public Long next() {
		lastReturned+=inc;
		return lastReturned;
	}

	public void remove() {
	}

}
