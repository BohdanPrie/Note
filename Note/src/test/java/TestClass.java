import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestClass {
	public static boolean isPalindrom(String checkedValue) {
		for (int i = 0; i < checkedValue.length() / 2; i++) {
			if(checkedValue.charAt(i) != checkedValue.charAt(checkedValue.length() - 1 - i)) {
				return false;
			}
		}
		return true;
	}
	
	@Test
	public void checkPalindrom() {
		assertEquals(true, isPalindrom("1001"));
		assertEquals(false, isPalindrom("777925498"));
		assertEquals(true, isPalindrom("tenet"));
		assertEquals(true, isPalindrom("лёшанаполкеклопанашёл"));
	}
}
