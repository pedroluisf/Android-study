package pt.ipp.isep.dei.formacao.android.weatherdroid;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

public interface IDatabase {

	public boolean isOpen();

	public Database open() throws SQLException;

	public void close();

	public long insertEntry(String table, ContentValues values);

	public boolean removeEntry(String table, String whereClause);

	public Cursor getAllEntriesRawQuery(String sql);

	public Cursor getEntry(String table, String[] fields, String whereClause, String orderBy);

}