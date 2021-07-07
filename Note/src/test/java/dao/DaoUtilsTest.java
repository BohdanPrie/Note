package dao;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ua.com.bohdanprie.notes.dao.DaoUtils;

public class DaoUtilsTest {
	@Test
	public void buildInsertQuery() {
		StringBuffer buffer = new StringBuffer("INSERT INTO VALUES ");
		DaoUtils.buildInsertValuesQuery(0, 0, buffer);
		assertEquals("INSERT INTO VALUES ()", buffer.toString());
		
		buffer = new StringBuffer("INSERT INTO VALUES ");
		DaoUtils.buildInsertValuesQuery(2, 5, buffer);
		assertEquals("INSERT INTO VALUES (?,?,?,?,?), (?,?,?,?,?)", buffer.toString());
		
		buffer = new StringBuffer("INSERT INTO VALUES ");
		DaoUtils.buildInsertValuesQuery(2, 0, buffer);
		assertEquals("INSERT INTO VALUES (), ()", buffer.toString());
	}
}
