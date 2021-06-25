import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class MainTest {

	@Test
	public void test() {
		try (FileOutputStream file = new FileOutputStream("asdfsaf")) {
			try {
				throw new NullPointerException();
			} catch (NullPointerException e) {
				System.out.println("inner exception");
			}
		} catch (FileNotFoundException e) {
			System.out.println("out exception");
		} catch (IOException e) {

		} finally {
			System.out.println("finally");
		}
	}
}
