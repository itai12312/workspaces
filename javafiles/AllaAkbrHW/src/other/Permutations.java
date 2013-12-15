import java.util.Iterator;


public class Permutations implements Iterable<Mapping>, Domain {

	public Domain domain;
	public Checker[] preCheckers;
	public long[] fixedValues;
	private boolean hasLimiter = false;

	public Permutations(Domain d) {
		domain = d;
		preCheckers = new Checker[(int) d.size()];
		fixedValues = new long[(int) d.size()];
		for (int i = 0;i < d.size();i++)
			fixedValues[i] = -1;
	}

	public Iterator<Mapping> iterator() {
		return new PermutationsIter(this);
	}

	public void addLimiter(PosValueLimiter lim) {
		if (fixedValues[(int)lim.pos-1] != -1)
			throw new RuntimeException("Double limiter for position "+lim.pos);
		fixedValues[(int)lim.pos-1] = lim.value;
		hasLimiter = true;
	}

	public void addPreChecker(Checker checker,int stage) {
		if (preCheckers[stage] != null)
			throw new RuntimeException("Stage "+stage+" already has pre checker.");
		preCheckers[stage] = checker;
	}

	public void addPreChecker(Checker checker, int minStage, int maxStage) {
		for (int i = minStage;i <= maxStage;i++)
			addPreChecker(checker,i);
	}

	public long encode(Mapping perm) {
		if (hasLimiter)
			throw new RuntimeException("Encoding in presence of limiters isn't supported");
		
		long id = 0;
		long numLeft = domain.size();
		long mult = 1;
		long[] elementsLeft = new long[(int) domain.size()];
		for (int i = 0; i < elementsLeft.length; i++) {
			elementsLeft[i] = i;
		}
		for (int i = 0;i < perm.size;i++) {
			long item = perm.get(i);
			int itemNum;
			for (itemNum = 0; itemNum < elementsLeft.length; itemNum++) {
				if (elementsLeft[itemNum] == item)
					break;
			}
			assert (itemNum < numLeft);
			id += itemNum*mult;
			mult*=numLeft;
			numLeft--;
			elementsLeft[itemNum] = elementsLeft[(int) numLeft];
		}
		return id;
	}
	
	public Mapping decode(long id) {
		if (hasLimiter)
			throw new RuntimeException("Decoding in presence of limiters isn't supported");
		
		long[] permArray = new long[(int) domain.size()];
		long base = domain.size();
		long[] elementsLeft = new long[(int) domain.size()];
		for (int i = 0; i < elementsLeft.length; i++) {
			elementsLeft[i] = i;
		}
		for (int i = 0;i < domain.size();i++) {
			int itemNum = (int) (id % base);
			id = id/base;
			permArray[i] = elementsLeft[itemNum];
			base--;
			elementsLeft[itemNum] = elementsLeft[(int) base];
		}
		ArrayMapping perm = new ArrayMapping(domain);
		perm.setArray(permArray);
		return perm;
	}
	
	public long size() {
		long fact = 1;
		for (int i = 1;i <= domain.size();i++)
			fact *= i;
		return fact;
	}

	public String getString(long id) {
		Mapping perm = decode(id);
		String string = "";
		for (int i = 0;i < domain.size();i++) {
			string+=domain.getString(perm.get(i))+" ";
		}
		return string;
	}

}
