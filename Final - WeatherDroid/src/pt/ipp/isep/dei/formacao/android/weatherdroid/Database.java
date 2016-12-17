package pt.ipp.isep.dei.formacao.android.weatherdroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database implements IDatabase {

    private static final String DATABASE_NAME = "weatherdroid.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = "WEATHERDROID_DATABASE";

    // instância da base de dados
    private SQLiteDatabase db;

    // contexto da aplicação que está a utilizar a bd
    private final Context context;

    // open/upgrade helper
    private MyDbHelper dbHelper;

    private static Database instance;
    
    public static Database getInstance(Context context) {

        if (instance == null) {
            instance = new Database(context);
        }

        return instance;
    }

    public Database(Context _context) {
        context = _context;
        dbHelper = new MyDbHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public boolean isOpen() {
        return db != null && db.isOpen();
    }

    public Database open() throws SQLException {
        if (db == null)
            db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (db != null)
            db.close();
        db = null;
    }

    public long insertEntry(String table, ContentValues values) {
        return db.insert(table, null, values);
    }

    public boolean removeEntry(String table, String whereClause) {
        return db.delete(table, whereClause, null) > 0;
    }

    public Cursor getAllEntriesRawQuery(String sql) {
        return db.rawQuery(sql, null);
    }

    public Cursor getEntry(String table, String[] fields, String whereClause, String orderBy) {
        return db.query(table, fields, whereClause, null, null, null, orderBy);
    }

    // db helper
    private static class MyDbHelper extends SQLiteOpenHelper {

        private ScriptFileReader myFileReader;
    	private Context context;
    	
        public MyDbHelper(Context context, String name, CursorFactory factory,
                int version) {
            super(context, name, factory, version);
        	this.context = context;
        }

        // invocado quando a base de dados não existe no disco
        // !!! APÓS MODIFICAÇÕES NESTE MÉTODO, É NECESSÁRIO MUDAR O
        // VALOR DE DATABASE_VERSION PARA QUE AS ALTERAÇÕES TENHAM EFEITO
        @Override
        public void onCreate(SQLiteDatabase _db) {
            Log.w(TAG, "Creating Database...");
            executeFile(_db, "tbl_creates");
            init(_db);
        }

        // inicializa a base de dados
        // !!! APÓS MODIFICAÇÕES NESTE MÉTODO, É NECESSÁRIO MUDAR O
        // VALOR DE DATABASE_VERSION PARA QUE AS ALTERAÇÕES TENHAM EFEITO
        protected void init(SQLiteDatabase _db) {
            Log.w(TAG, "Initializing database...");
            executeFile(_db, "tbl_init");
        }

        // elimina a base de dados
        // !!! APÓS MODIFICAÇÕES NESTE MÉTODO, É NECESSÁRIO MUDAR O
        // VALOR DE DATABASE_VERSION PARA QUE AS ALTERAÇÕES TENHAM EFEITO
        protected void dropAll(SQLiteDatabase _db) {
            Log.w(TAG, "Droping Database...");
            executeFile(_db, "tbl_drops");
        }

        // invocado quando existe uma base de dados que não corresponde
        // à versão actual
        // !!! APÓS MODIFICAÇÕES NESTE MÉTODO, É NECESSÁRIO MUDAR O
        // VALOR DE DATABASE_VERSION PARA QUE AS ALTERAÇÕES TENHAM EFEITO
        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
                int _newVersion) {
            Log.w(TAG, "Upgrading from version " + _oldVersion + " to " 
                    + _newVersion + ", which will destroy all old data.");

            dropAll(_db);
            onCreate(_db);
        }
        
        private void executeFile(SQLiteDatabase _db, String fileName){
            myFileReader = new ScriptFileReader(context, context.getResources().getIdentifier(fileName, "raw", context.getPackageName()));
            myFileReader.open();
            String line = myFileReader.nextLine();
            while(line != null){
                _db.execSQL(line);
            	line = myFileReader.nextLine();
            }
            myFileReader.close();
            myFileReader = null;
        }
    }
}