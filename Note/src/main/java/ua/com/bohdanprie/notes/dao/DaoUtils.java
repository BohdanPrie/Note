package ua.com.bohdanprie.notes.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DaoUtils {
	private static final Logger LOG = LogManager.getLogger(DaoUtils.class.getName());

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