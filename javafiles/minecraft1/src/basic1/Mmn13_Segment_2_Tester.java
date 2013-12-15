package basic1;
public class Mmn13_Segment_2_Tester {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("********** Test Q3 Segment2 - Start **********");
		Segment2 seg0 = null, // (0.0,0.0)---(2.0,0.0)
		seg1 = null, // (0.0,2.0)---(2.0,2.0)
		seg2 = null, seg3 = null; // seg2 = seg3 = (1.0,4.0)---(4.0,4.0)
		// constructor and toString
		Point p = new Point(3.0, 2.0);
		Segment2 seg22 = new Segment2(p, 4.0);
		Point pLeft = new Point(1.0, 4.0);
		Point pRight = new Point(4.0, 8.0);
		seg2 = new Segment2(pLeft, pRight);
		// copy constructor
		seg3 = new Segment2(seg2);

		p.setX(1.0);
		if (!seg22.toString().equalsIgnoreCase("(1.0,2.0)---(5.0,2.0)")) {
			System.out.println("\t ERROR - seg22.toString() expected (1.0,2.0)---(5.0,2.0) ; actual="
							+ seg22);
		} else
			System.out.println("\t OK - seg22.toString() expected (1.0,2.0)---(5.0,2.0) ; actual="
							+ seg22);

		seg0 = new Segment2(0.0, 0.0, 2.0, 0.0);
		if (!seg0.getPoLeft().equals(new Point(0.0, 0.0))) {
			System.out.println("\t ERROR - expected seg0.getPoLeft()=(0.0,0.0) ; actual="
							+ seg0.getPoLeft());
		} else
			System.out.println("\t OK - expected seg0.getPoLeft()=(0.0,0.0) ; actual="
							+ seg0.getPoLeft());

		if (!seg0.getPoRight().equals(new Point(2.0, 0.0))) {
			System.out.println("\t ERROR - expected seg0.getPoRight()=(2.0,0.0) ; actual="
							+ seg0.getPoRight());
		} else
			System.out.println("\t OK - expected seg0.getPoRight()=(2.0,0.0) ; actual="
							+ seg0.getPoRight());
		// toString
		if (!seg0.toString().equalsIgnoreCase("(0.0,0.0)---(2.0,0.0)")) {
			System.out.println("\t ERROR - expected seg0.toString()=(0.0,0.0)---(2.0,0.0) ; actual="
							+ seg0);
		} else
			System.out.println("\t OK -  expected seg0.toString()=(0.0,0.0)---(2.0,0.0) ; actual="
							+ seg0);
		// check if fix right point y
		seg1 = new Segment2(0.0, 2.0, 2.0, 5.0);
		if (seg1.getPoRight().getY() != 2.0) {
			System.out.println("\t ERROR - fix right point y - expected seg1.getPoRight().getY()=2.0 ; actual="
							+ seg1.getPoRight().getY());
		} else
			System.out.println("\t OK - fix right point y - expected seg1.getPoRight().getY()=2.0 ; actual="
							+ seg1.getPoRight().getY());

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

		Segment2 s1 = new Segment2(0.0, 0.0, 2.0, 0.0);
		s1.changeSize(3.0);
		if (s1.getLength() != 5.0) {
			System.out.println("\t ERROR - s1.changeSize() - expected length 5.0 ; actual="
							+ s1.getLength());
		} else
			System.out.println("\t OK - s1.getLength() - expected length 5.0 ; actual="
							+ s1.getLength());

		// under, above, right & left
		s1 = new Segment2(5.0, 0.0, 10.0, 0.0);
		Segment2 s3 = new Segment2(5.0, 3.0, 10.0, 3.0);
		Segment2 s4 = new Segment2(5.0, -3.0, 10.0, -3.0);
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

		Segment2 sMid = new Segment2(5.0, 0.0, 10.0, 0.0);
		Segment2 sLeft = new Segment2(0.0, 3.0, 4.0, 3.0);
		Segment2 sRight = new Segment2(11.0, -3.0, 15.0, -3.0);

		//
		if (!sMid.isRight(sLeft)) {
			System.out.println("\t ERROR - sMid.isRight(sLeft) - expected true ; actual="
							+ sMid.isRight(sLeft)+ " sMid="+ sMid+ " sLeft="+ sLeft);
		} else
			System.out
					.println("\t OK - sMid.isRight(sLeft) - expected true ; actual="
							+ sMid.isRight(sLeft)+ " sMid="+ sMid+ " sLeft="+ sLeft);

		if (!sMid.isLeft(sRight)) {
			System.out
					.println("\t ERROR - sMid.isLeft(sRight) - expected true ; actual="
							+ sMid.isLeft(sRight)+ " sMid="+ sMid+ " sRight=" + sRight);
		} else
			System.out
					.println("\t OK - sMid.isLeft(sRight) - expected true ; actual="
							+ sMid.isLeft(sRight)+ " sMid="+ sMid+ " sRight=" + sRight);
		// overlap
		if (sMid.overlap(sMid) != 5.0) {
			System.out
					.println("\t ERROR - sMid.overlap(sMid) - expected 5.0 ; actual="
							+ sMid.overlap(sMid)+ " sMid="+ sMid+ " sMid="+ sMid);
		} else
			System.out
					.println("\t OK - sMid.overlap(sMid) - expected 5.0 ; actual="
							+ sMid.overlap(sMid)+ " sMid="+ sMid+ " sMid="+ sMid);

		// trapez
		if (sMid.trapezePerimeter(sMid) != 10.0) {
			System.out
					.println("\t ERROR - sMid.trapezePerimeter(sMid) - expected 10.0 ; actual="
							+ sMid.trapezePerimeter(sMid)+ " sMid="+ sMid+ " sMid=" + sMid);
		} else
			System.out
					.println("\t OK - sMid.trapezePerimeter(sMid) - expected 10.0 ; actual="
							+ sMid.trapezePerimeter(sMid)+ " sMid="+ sMid+ " sMid=" + sMid);
		System.out.println("********** Test Q3 Segment2 - Finish **********\n");

	}

}
