
public class FindFirst {

	public static Object f(Iterable<? extends Object> sols, Checker check) {
		for (Object sol : sols) {
			if (check.check(sol))
				return sol;
		}
		return null;
	}

}
