import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class StringTest {
	
	@Test
	public void testReversedString() throws IOException{
		assertEquals("1009", reverseString("9001"));
	}
	
	public static String reverseString(String value) {
		StringBuffer buffer = new StringBuffer(value);
		return buffer.reverse().toString();
	}
}