package basic1;
public class segment1test{
	public static void main(String[] args) {

		System.out.println("********** Test Q2 Segment1 - Start **********");
		Segment1 seg0 = null; // (0.0,0.0)---(2.0,0.0)
		Segment1 seg2 = null;
		Segment1 seg3 = null; // seg2 = seg3 = (1.0,4.0)---(4.0,4.0)
		// constructor and toString
		seg0 = new Segment1(0.0, 0.0, 2.0, 0.0);
		if (!seg0.getPoLeft().equals(new Point(0.0, 0.0))) {
			System.out.println("\t ERROR - expected seg0.getPoLeft()=(0.0,0.0) ; actual="
							+ seg0.getPoLeft());
		} else
			System.out.println("\t OK - expected seg0.getPoLeft()=(0.0,0.0) ; actual="
							+ seg0.getPoLeft());

		// second constructor Segment1 (Point left, Point right)
		Point pLeft = new Point(1.0, 4.0);
		Point pRight = new Point(4.0, 8.0);
		seg2 = new Segment1(pLeft, pRight);
		if (!seg2.getPoLeft().equals(pLeft) || !seg2.getPoRight().equals(new Point(4.0, 4.0))) {
			System.out.println("\t ERROR - second Constructor - expected (1.0,4.0)---(4.0,4.0) ; actual="
							+ seg2);
		} else
			System.out.println("\t OK - second Constructor - expected (1.0,4.0)---(4.0,4.0) ; actual="
							+ seg2);

		// copy constructor
		seg3 = new Segment1(seg2);
		if (!seg3.getPoLeft().equals(new Point(1.0, 4.0))|| !seg3.getPoRight().equals(new Point(4.0, 4.0))) {
			System.out.println("\t ERROR - 3rd Constructor - expected (1.0,4.0)---(4.0,4.0) ; actual="
							+ seg3);
		} else
			System.out.println("\t OK - 3rd Constructor - expected (1.0,4.0)---(4.0,4.0) ; actual="
							+ seg3);

		// equals

		if (!seg2.equals(seg3)) {
			System.out.println("\t ERROR - equals - seg2.equals(seg3)? - expected true ; actual="
							+ seg2.equals(seg3));
		} else
			System.out.println("\t OK - equals - seg2.equals(seg3)? - expected true ; actual="
							+ seg2.equals(seg3));

		// sizes
		if (seg0.getLength() != 2.0) {
			System.out.println("\t ERROR - seg0.getLength() - expected 2.0 ; actual="
							+ seg0.getLength());
		} else
			System.out.println("\t OK - seg0.getLength() - expected 2.0 ; actual="
							+ seg0.getLength());

		Segment1 s1 = new Segment1(0.0, 0.0, 2.0, 0.0);
		s1.changeSize(3.0);
		if (s1.getLength() != 5.0) {
			System.out.println("\t ERROR - s1.changeSize() - expected length 5.0 ; actual="
							+ s1.getLength());
		} else
			System.out.println("\t OK - s1.getLength() - expected length 5.0 ; actual="
							+ s1.getLength());

		s1 = new Segment1(0.0, 0.0, 2.0, 0.0);
		Segment1 s2 = new Segment1(0.0, 2.0, 4.0, 2.0); // bigger than s1
		if (s1.isBigger(s2)) {
			System.out.println("\t ERROR - s1.isBigger(s2) - expected false ; actual="
							+ s1.isBigger(s2));
		} else
			System.out.println("\t OK - s1.isBigger(s2) - expected false ; actual="
							+ s1.isBigger(s2));

		// under, above, right & left
		s1 = new Segment1(5.0, 0.0, 10.0, 0.0);
		Segment1 s3 = new Segment1(5.0, 3.0, 10.0, 3.0);
		Segment1 s4 = new Segment1(5.0, -3.0, 10.0, -3.0);
		// under
		if (!s1.isUnder(s3)) {
			System.out.println("\t ERROR - s1.isUnder(s3) - expected true ; actual="
							+ s1.isUnder(s3) + " s1=" + s1 + " s3=" + s3);
		} else
			System.out.println("\t OK - s1.isUnder(s3) - expected true ; actual="
							+ s1.isUnder(s3) + " s1=" + s1 + " s3=" + s3);
		// above
		if (!s1.isAbove(s4)) {
			System.out.println("\t ERROR - s1.isAbove(s4) - expected true ; actual="
							+ s1.isAbove(s4) + " s1=" + s1 + " s4=" + s4);
		} else
			System.out.println("\t OK - s1.isAbove(s4) - expected true ; actual="
							+ s1.isAbove(s4) + " s1=" + s1 + " s4=" + s4);

		Segment1 sMid = new Segment1(5.0, 0.0, 10.0, 0.0);
		Segment1 sLeft = new Segment1(0.0, 3.0, 4.0, 3.0);
		Segment1 sRight = new Segment1(11.0, -3.0, 15.0, -3.0);

		// left
		if (!sMid.isRight(sLeft)) {
			System.out.println("\t ERROR - sMid.isRight(sLeft) - expected true ; actual="
							+ sMid.isRight(sLeft)+ " sMid="+ sMid+ " sLeft="+ sLeft);
		} else
			System.out.println("\t OK - sMid.isRight(sLeft) - expected true ; actual="
							+ sMid.isRight(sLeft)+ " sMid="+ sMid+ " sLeft="+ sLeft);
		// right
		if (!sMid.isLeft(sRight)) {
			System.out.println("\t ERROR - sMid.isLeft(sRight) - expected true ; actual="
							+ sMid.isLeft(sRight)+ " sMid="+ sMid+ " sRight=" + sRight);
		} else
			System.out.println("\t OK - sMid.isLeft(sRight) - expected true ; actual="
							+ sMid.isLeft(sRight)+ " sMid="+ sMid+ " sRight=" + sRight);

		// overlap
		if (sMid.overlap(sMid) != 5.0) {
			System.out.println("\t ERROR - sMid.overlap(sMid) - expected 5.0 ; actual="
							+ sMid.overlap(sMid)+ " sMid="+ sMid+ " sMid="+ sMid);
		} else
			System.out.println("\t OK - sMid.overlap(sMid) - expected 5.0 ; actual="
							+ sMid.overlap(sMid)+ " sMid="+ sMid+ " sMid="+ sMid);

		if (sMid.trapezePerimeter(sMid) != 10.0) {
			System.out.println("\t ERROR - sMid.trapezePerimeter(sMid) - expected 10.0 ; actual="
							+ sMid.trapezePerimeter(sMid)+ " sMid="+ sMid+ " sMid=" + sMid);
		} else
			System.out.println("\t OK - sMid.trapezePerimeter(sMid) - expected 10.0 ; actual="
							+ sMid.trapezePerimeter(sMid)+ " sMid="+ sMid+ " sMid=" + sMid);

		System.out.println("********** Test Q2 Segment1 - Finish **********\n");

	}

}
