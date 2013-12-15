import java.util.Iterator;

/**
 * The identity permutation is output last.
 */
public class PermutationsIter implements Iterator<Mapping> {

	private Domain domain;
	private int[] counters;
	private long[] curPerm;
	private long domainSize;
	private ArrayMapping mapping;
	private long[] mobileValues;
	private long[] fixedValues;
	private long[] fixedPositions;
	private Checker[] preCheckers;
	private long countersChecked;
	private boolean started;

	public PermutationsIter(Permutations p) {
		domain = p.domain;
		preCheckers = p.preCheckers;
		countersChecked = 0;
		mapping = new ArrayMapping(domain);
		domainSize = domain.size();
		curPerm = new long[(int)domainSize];
		initLimiterTables(p);
		counters = new int[mobileValues.length];
		for (int i = 1;i < counters.length;i++)
			counters[i] = 0;
		started = false;
	}

	private void initLimiterTables(Permutations p) {
		
		long[] positions = new long[(int) domainSize];
		for (int i = 0; i < positions.length; i++) {
			positions[i] = -1;
		}
		
		int numFixed;
		int numMobile = (int) domainSize;
		for (int i = 0; i < p.fixedValues.length; i++) {
			if (p.fixedValues[i] != -1) {
				numMobile--;
				positions[(int) p.fixedValues[i]] = i;
			}
		}
		mobileValues = new long[numMobile];
		fixedValues = new long[(int) (domainSize-numMobile)];
		fixedPositions = new long[(int) (domainSize-numMobile)];
		
		numMobile = 0;
		numFixed = 0;
		for (int i = 0; i < p.fixedValues.length; i++) {
			if (positions[i] == -1) {
				mobileValues[numMobile] = i;
				numMobile++;
			} else {
				fixedValues[numFixed] = i;
				fixedPositions[numFixed] = positions[i];
				numFixed++;
			}
		}
	}

	public boolean hasNext() {
		if (!started)
			return true;
		//have we reached the identity permutation?
		for (int i = 0;i < counters.length;i++) {
			if (counters[i] != 0)
				return true;
		}
		return false;
	}

	public Mapping next() {
		long i;
		int counter = counters.length-1;
		started = true;
		//find first item that's incorrect
		for (i = countersChecked+1;i < counters.length-1;i++) {
			Checker preChecker = preCheckers[(int)i];
			if (preChecker != null) {
				mapping.setArray(curPerm,i);
				if (!preChecker.check(mapping)) {
					counter = (int) (i-1);
					break;
				}
			}
		}

		
		//last counter is always 0 and is ignored
		//increment at the "bad" counter (counter LEN-1 if
		//all are ok);
		for (;counter >= 0;counter--) {
			if (counters[counter] < counters.length-counter-1) {
				counters[counter]++;
				long itemsReady = counter+1;
				Checker preChecker = preCheckers[(int)itemsReady];
				if (preChecker != null) {
					computePerm();
					mapping.setArray(curPerm,itemsReady);
					if (!preChecker.check(mapping)) {
						counter--;   //reverse incremeting at loop end
						continue;  //try again this counter
					}
				}
				countersChecked = itemsReady;
				break;
			}
			counters[counter] = 0;
		}
		
		computePerm();
		mapping.setArray(curPerm);
		return mapping;
	}

	/**
	 * @param stage number of items to place.
	 */
	private void computePerm() {
		long tmp;
		int i;
		for (i = 0; i < mobileValues.length; i++)
			curPerm[i] = mobileValues[i];
		
		//permute mobile values
		for (i = 0; i < mobileValues.length; i++) {
			tmp = curPerm[i];
			curPerm[i] = curPerm[i+counters[i]];
			curPerm[i+counters[i]] = tmp;
		}
		
		//place fixed values
		int permLen = mobileValues.length;
		for (i = 0; i < fixedValues.length; i++) {
			tmp = curPerm[(int) fixedPositions[i]];
			curPerm[(int) fixedPositions[i]] = fixedValues[i];
			curPerm[permLen] = tmp;
			permLen++;
		}
	}

	public void remove() {
	}

}
