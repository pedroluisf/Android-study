package pt.ipp.isep.dei.formacao.android.contrylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database {

    public static final String TBL_COUNTRY = "tbl_country";
    public static final String FLD_ID = "id_country";
    public static final String FLD_NAME = "name";
    public static final String FLD_CONTINENT = "continent";

    private static final String DATABASE_NAME = "countrylist.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = "COUNTRYLIST_DATABASE";

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

    public Cursor getEntry(String table, String[] fields, String whereClause) {
        return db.query(table, fields, whereClause, null, null, null, null);
    }

    // db helper
    private static class MyDbHelper extends SQLiteOpenHelper {

        public MyDbHelper(Context context, String name, CursorFactory factory,
                int version) {

            super(context, name, factory, version);
        }

        // invocado quando a base de dados não existe no disco
        // !!! APÓS MODIFICAÇÕES NESTE MÉTODO, É NECESSÁRIO MUDAR O
        // VALOR DE DATABASE_VERSION PARA QUE AS ALTERAÇÕES TENHAM EFEITO
        @Override
        public void onCreate(SQLiteDatabase _db) {
            Log.w(TAG, "Creating Database...");
            _db.execSQL("CREATE TABLE tbl_country (id_country INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(75) NOT NULL, continent VARCHAR(30));");
            init(_db);
        }

        // inicializa a base de dados
        // !!! APÓS MODIFICAÇÕES NESTE MÉTODO, É NECESSÁRIO MUDAR O
        // VALOR DE DATABASE_VERSION PARA QUE AS ALTERAÇÕES TENHAM EFEITO
        protected void init(SQLiteDatabase _db) {
            Log.w(TAG, "Initializing database...");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Portugal', 'Europe');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Spain', 'Europe');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Germany', 'Europe');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Netherlands', 'Europe');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Norway', 'Europe');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Italy', 'Europe');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Greece', 'Europe');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Poland', 'Europe');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('USA', 'America');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Brazil', 'America');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Venezuela', 'America');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Chile', 'America');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Mexico', 'America');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Cuba', 'America');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Argentina', 'America');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Niger', 'Africa');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Angola', 'Africa');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Egypt', 'Africa');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('South Africa', 'Africa');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Mozambique', 'Africa');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Japan', 'Asia');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Indonesia', 'Asia');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('Russia', 'Asia');");
            _db.execSQL("INSERT INTO tbl_country(name, continent) VALUES ('South Korea', 'Asia');");
        }

        // elimina a base de dados
        // !!! APÓS MODIFICAÇÕES NESTE MÉTODO, É NECESSÁRIO MUDAR O
        // VALOR DE DATABASE_VERSION PARA QUE AS ALTERAÇÕES TENHAM EFEITO
        protected void dropAll(SQLiteDatabase _db) {
            Log.w(TAG, "Droping Database...");
            _db.execSQL("DROP TABLE IF EXISTS tbl_country;");
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
    }
}
