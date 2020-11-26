// Started out as constants, but some fields ended more dynamic ;-)
public class Constants {
	public final static int maxGoalsPrMatch = 10;
	public final static int maxExtraTimePrMatch = 10;
	public final static String dkDateFormat = "d. MMM YYYY";
	private static int modus = 16;  // Predefined modus for margin space etc.
	public static int getModus() { return modus; }

	private static int mDIFrameWidth = 70 * modus;
	public static int getMDIFrameWidth() { return mDIFrameWidth;};

	private static int mDIFrameHigth = 55 * modus;
	public static int getMDIFrameHigth() {return mDIFrameHigth;}
	
	private static int stdTableWidth = 32 * modus;
	public static int getStdTableWidth() {return stdTableWidth;}	

	private static int mDIChildWidth = mDIFrameWidth - 14 * modus;
	public static int getMDIChildWidth() {return mDIChildWidth;}	

	private static int mDIChildHigth = mDIFrameHigth - 11 * modus;
	public static int getMDIChildHigth() {return mDIChildHigth;}
	
	public final static int pointForWin = 3;
	public final static int pointForDraw = 1;
	public static void recalcDimensions(int newModus) {
		modus = newModus;
		mDIFrameWidth = 70 * modus;
		mDIFrameHigth = 55 * modus;
		stdTableWidth = 32 * modus;	
		mDIChildWidth = mDIFrameWidth - 14 * modus;
		mDIChildHigth = mDIFrameHigth - 11 * modus;
	};
}
