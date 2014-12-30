package com.eventorio.app.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	static final String TAG = "DBAdapter";

	static String DATABASE_NAME;
	static final String DATABASE_TABLE = "eventos";
	static final int DATABASE_VERSION = 1;
	
	static final String key_id = "_id";
	public static final String Id_Evento="Id_Evento";
	
	public static final String NomEvento = "NomEvento";
	public static final String Fecha = "Fecha";
	public static final String Lugar = "Lugar";

	static final String DATABASE_CREATE = "CREATE TABLE  "+DATABASE_TABLE
			+" (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ NomEvento+"  VARCHAR(100), "
			+ Fecha+"  DATETIME, "
			+ Lugar+"  VARCHAR(100))";
	
	

	final Context context;

	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		
		DATABASE_NAME=ctx.getPackageName();
		
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS solicitudes");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
			DBHelper.close();
		}
	}

	// ---insert a contact into the database---

	// ---deletes a particular contact---
	public boolean deleteSolicitud(String rowId) {
		return db.delete(DATABASE_TABLE,key_id + "=" + rowId, null) > 0;
	}

	// ---retrieves all the solicitudes---
	public Cursor getAllSolicitudes(String orderby) {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { key_id, NomEvento, Fecha, Lugar }, null, null, null, null,orderby);
		

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	// ---retrieves a particular contact---
	public Cursor getSolicitud(String rowCodigo, String rowCodeEntidad)
			throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { key_id, NomEvento, Fecha, Lugar }, null, null, null, null,null);
		

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a contact---
	public boolean updateSolicitud() {
		
		/*Cursor c = getSolicitud(codigo, codigoEntidad);
		
		String iddato=c.getString(c.getColumnIndex("_id"));
		
		c.close();
		
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(key_entidad, entidad);
		initialValues.put(key_primernombre, primernombre);
		initialValues.put(key_segundonombre, segundonombre);
		initialValues.put(key_primerapellido, primerapellido);
		initialValues.put(key_segundoapellido, segundoapellido);
		initialValues.put(key_email, email);
		initialValues.put(key_tipo_solicitud, tipo_solicitud);
		initialValues.put(key_descripcion, descripcion);
		initialValues.put(key_codigo, codigo);
		initialValues.put(key_webservice, webservice);
		//initialValues.put(key_iddato, "NULL"); // SE SUPONE QUE LO CREA EN LA
											// TABLA
		initialValues.put(key_estado, estado);
		initialValues.put(key_vulnerable, vulnerable);
		initialValues.put(key_tipo_documento, tipo_documento);
		initialValues.put(key_documento, documento);
		initialValues.put(key_departamento, departamento);
		initialValues.put(key_municipio, municipio);
		initialValues.put(key_direccion, direccion);
		initialValues.put(key_telefono, telefono);
		initialValues.put(key_celular, celular);
		initialValues.put(key_asunto, asunto);
		initialValues.put(key_adjunto, adjunto);
		initialValues.put(key_medio_recepcion, medio_recepcion);
		initialValues.put(key_medio_respuesta, medio_respuesta);
		initialValues.put(key_respuesta, respuesta);
		initialValues.put(key_anonimo, anonimo);
		initialValues.put(key_simcard, simcard);
		initialValues.put(key_imei, imei);
		initialValues.put(key_hecho, hecho);
		initialValues.put(key_nombre_tipo_identificacion,nombre_tipo_identificacion);
		initialValues.put(key_nombre_tipo_solicitud, nombre_tipo_solicitud);
		initialValues.put(key_nombre_vulnerable, nombre_vulnerable);
		initialValues.put(key_nombre_departamento, nombre_departamento);
		initialValues.put(key_nombre_municipio, nombre_municipio);
		initialValues.put(key_codigo_pais, codigo_pais);
		initialValues.put(key_nombre_pais, nombre_pais);
		initialValues.put(key_codigoAsuntoSolicitud, codigoAsuntoSolicitud);
		initialValues.put(key_codigoTipoCanalSolicitud,codigoTipoCanalSolicitud);
		initialValues.put(key_codigoTipoCanalRespuesta,codigoTipoCanalRespuesta);
		initialValues.put(key_codigoEntidad, codigoEntidad);
		initialValues.put(key_siglaEntidad, siglaEntidad);
		initialValues.put(key_numeroTelefonicoDispositivo,numeroTelefonicoDispositivo);
		initialValues.put(key_fecha, fecha);
		initialValues.put(key_fecha_modificacion, fecha_modificacion);
		initialValues.put(key_llave, llave);
		Log.d(DBAdapter.TAG+" updateSolicitud", initialValues.toString());
		*/
		
		return false;//db.update(DATABASE_TABLE, initialValues, key_iddato + "=" + iddato, null) > 0;
	}

	public long insertSolicitud(int id, String nomEvento, String fecha, String lugar) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(key_id, id);
		initialValues.put(Fecha, fecha);
		initialValues.put(NomEvento, nomEvento);
		initialValues.put(Lugar, lugar);
		Log.d(DBAdapter.TAG+" insertSolicitud", initialValues.toString());
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
}
