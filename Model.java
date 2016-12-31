package edu.UTEP.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Model extends SQLiteOpenHelper {

	String tag = "ModelTest";

	final static String T_Discussion = "Discussion";
	final static String C_Id = "C_Id";
	final static String C_Msgrecu = "C_Msgrecu";
	final static String C_Msgenvoyer = "C_Msgenvoyer";


	
	public Model(Context context, String name, CursorFactory factory,
				 int version) {
		super(context, name, factory, version);
		Log.i(tag,"Constructeur");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(tag,"Creation");
		db.execSQL("CREATE TABLE "+ T_Discussion +" ("+
				C_Id+		" INTEGER PRIMARY KEY AUTOINCREMENT," +
				C_Msgrecu +		" VARCHAR(255), " +
				C_Msgenvoyer +	" VARCHAR(255)) ");
		Log.i(tag,"Fin Creation");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(tag,"Upgrade");
		db.execSQL("DROP TABLE IF EXISTS " + T_Discussion);
		onCreate(db);
		Log.i(tag,"Fin Upgrade");
	}

	public List<String> allTableNames() {
		List<String> result = new ArrayList<String>() ;
		String selectQuery = "select name from sqlite_master where type = 'table'" ;
		Cursor c = this.getReadableDatabase().rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				String n =  c.getString(c.getColumnIndex("name"));
				result.add(n);
			} while (c.moveToNext());
		}
		return result;
	}

	public void save(Messagerie p){
		ContentValues v  = new ContentValues();
		v.put(C_Msgrecu, p.getMsgrecu());
		v.put(C_Msgenvoyer, p.getMsgenvoyer());
		Long id  = p.getId();
		if (id == null){
			// creation
			id = this.getWritableDatabase().insert(T_Discussion, null, v);
			p.setId(id);
		}
		else{
			// mise a jour
			this.getWritableDatabase().update(
					T_Discussion,
					v, 
					C_Id +" = ?", 
					new String[]{String.valueOf(id)});
		}
	}
	
	
	public Cursor getAllPersonneAsCursor(){
		return this.getReadableDatabase().rawQuery("select * from "+ T_Discussion, null);
	}
	
	public void deleteAllPersonne(){
		this.getWritableDatabase().delete(T_Discussion, null, null);
	}
	
	public void deletePersonne(Messagerie p){
		this.getWritableDatabase().delete(T_Discussion, C_Id +" = ?", new String[]{String.valueOf(p.getId())});
	}
	
	public Messagerie getById(Long id){
		Cursor c =
				this.getReadableDatabase().query(
						T_Discussion,
						null, 
						C_Id +" = ?", 
						new String[]{String.valueOf(id)},
						null, null, null, null);
		if (c.moveToFirst()){
			Messagerie p = convert(c);
			return p;
		}
		return null;
	}
	
	private Messagerie convert(Cursor c){
		Messagerie p = new Messagerie();
		Long id =  c.getLong(c.getColumnIndex(C_Id));
		p.setId(id);
		p.setMsgrecu(c.getString(c.getColumnIndex(C_Msgrecu)));
		p.setMsgenvoyer(c.getString(c.getColumnIndex(C_Msgenvoyer)));
		return p;
	}
	
	public List<Messagerie> getAllPersonne(){
		List<Messagerie> result = new ArrayList<Messagerie>() ;
		Cursor c = getAllPersonneAsCursor();
		if (c.moveToFirst()) {
			do {
				Messagerie p = convert(c);
				Log.i(tag,"Messagerie recuperee de la BD " + p);
				result.add(p);
			} while (c.moveToNext());
		}
		return result;
	}

}
