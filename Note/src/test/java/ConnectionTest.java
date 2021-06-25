import org.junit.Test;

import ua.com.bohdanprie.notes.dao.DaoFactory;

public class ConnectionTest {
	DaoFactory daoFactory = DaoFactory.getInstance();
	@Test
	public void testConnectToDB() {
		daoFactory.getConnection();
	}
}
