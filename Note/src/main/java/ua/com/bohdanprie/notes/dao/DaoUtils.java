package ua.com.bohdanprie.notes.dao;

public final class DaoUtils {
	
	public static String buildInsertValuesQuery(int numberRows, int numberElements) {
		StringBuffer SQL = new StringBuffer();

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
		return SQL.toString();
	}
}
