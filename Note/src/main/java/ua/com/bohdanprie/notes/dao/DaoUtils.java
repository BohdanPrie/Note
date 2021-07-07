package ua.com.bohdanprie.notes.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Class provide methods to work with DAO 
 * @author bohda
 *
 */
public final class DaoUtils {
	private static final Logger LOG = LogManager.getLogger(DaoUtils.class.getName());

	/**
	 * Method build InsertQuery, based on number of inserted rows and number of inserted elements in each row.
	 * <br>Do not insert values, but build String Query
	 * @param numberRows
	 * @param numberElements
	 * @param SQL
	 */
	public static void buildInsertValuesQuery(int numberRows, int numberElements, StringBuffer SQL) {
		LOG.trace("Building query to insert user's data");
		if(SQL == null) {
			LOG.debug("Buffer is null");
			return;
		}
		if(numberRows == 0) {
			SQL.append("()");
		}
		for (int values = 0; values < numberRows; values++) {
			SQL.append("(");
			for(int elems = 0; elems < numberElements; elems++) {
				SQL.append("?");
				if(elems != numberElements - 1) {
					SQL.append(",");
				}
			}
			SQL.append(")");
			if(values != numberRows - 1) {
				SQL.append(", ");
			}
		}	
		LOG.info("Query to insert user's data is built");
	}
}