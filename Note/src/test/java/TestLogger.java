import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.junit.Test;

public class TestLogger {
	Logger LOG = LogManager.getLogger(TestLogger.class);
	@Test
	public void testLog() {
		LOG.info("Test info");
	}
}