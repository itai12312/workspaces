package basic1;
public class Mmn13_Point_Tester{

	public static void main(String[] args) {
		System.out.println("********** Test Q1 Point - Start **********");
		Point p0 = null; // p0=(0,0)
		Point p1 = null; // p1=p2=(3.3,4.4)
		Point p2 = null; // p3=(1.1, 1.1)
		Point p4 = null; // p4=(3.3, 5.2)
		Point p5 = null; // p5=(4.1, 4.4)

		p0 = new Point(0.0, 0.0); // (0, 0)
		p1 = new Point(3.3, 4.4);
		p2 = new Point(p1);
		if (p2.getX() != 3.3 || p2.getY() != 4.4) {
			System.out.println("\t ERROR - copy constructor (other p): expected(3.3, 4.4) ; actual=" + p2);
		} else
			System.out.println("\t OK - copy constructor (other p): expected(3.3, 4.4) ; actual=" + p2);

		p4 = new Point(3.3, 5.2);
		p5 = new Point(4.1, 4.4);

		// check equals
		if (!p1.equals(p2)) { // expect true
			System.out.println("\t ERROR - p1.equals(p2) expected true ; actual="+ p1.equals(p2) + ";  p1=" + p1 + " p2=" + p2);
		} else 
			System.out.println("\t OK - p1.equals(p2) expected true ; actual="+ p1.equals(p2) + ";  p1=" + p1 + " p2=" + p2);

		// check above and under p1 & p5 same high, p4 above
		if (!p4.isAbove(p1)) { // expect true
			System.out.println("\t ERROR - p4.isAbove(p1) expected true ; actual="
							+ p4.isAbove(p1) + ";  p1=" + p1 + " p4=" + p4);
		} else 
			System.out.println("\t OK - p4.isAbove(p1) expected true ; actual="
					+ p4.isAbove(p1) + ";  p1=" + p1 + " p4=" + p4);

		// under
		if (!p1.isUnder(p4)) { // expect true
			System.out.println("\t ERROR - p4.isUnder(p1) expected true ; actual="
							+ p1.isUnder(p4) + ";  p1=" + p1 + " p4=" + p4);
		} else 
			System.out.println("\t OK - p1.isUnder(p4) expected true ; actual="
					+ p1.isUnder(p4) + ";  p1=" + p1 + " p4=" + p4);

		// check left and right p1 & p4 same, p5 right
		if (!p1.isLeft(p5)) { // expect true
			System.out.println("\t ERROR - p1.isLeft(p5) expected true ; actual="
							+ p1.isLeft(p5) + ";  p1=" + p1 + " p5=" + p5);
		} else
			System.out.println("\t OK - p1.isLeft(p5) expected true ; actual="
					+ p1.isLeft(p5) + ";  p1=" + p1 + " p5=" + p5);

		// check move
		p0.move(2.1, -1.2); // p0 was (0, 0) expected (2.1, -1.2)
		if (p0.getX() != 2.1 || p0.getY() != -1.2) {
			System.out.println("\t ERROR - p0.move(2.1, -1.2) expected p0(2.1, -1.2); actual="+ p0);
		} else
			System.out.println("\t OK - p0.move(2.1, -1.2) expected p0(2.1, -1.2); actual=" + p0);

		// distance
		p0 = new Point(0.0, 0.0);
		p1 = new Point(0.0, 3.0);
		p2 = new Point(4.0, 3.0);

		if (p0.distance(p1) != 3) {
			System.out.println("\t ERROR - p0.distance(p1) expected 3 ; actual="
							+ p0.distance(p1) + " p0=" + p0 + "; p1=" + p1);
		} else
			System.out.println("\t OK - p0.distance(p1) expected 3 ; actual="
					+ p0.distance(p1) + "p0=" + p0 + "; p1=" + p1);
		// check distance
		System.out.println("********** Test Q1 Point - Finish **********\n");
	}


}