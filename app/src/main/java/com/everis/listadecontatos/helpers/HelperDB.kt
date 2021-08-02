package com.everis.listadecontatos.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO

class HelperDB(
    context: Context?
) : SQLiteOpenHelper(context,DATABASE_NAME,null, VERSION) {

    companion object{
        private  val DATABASE_NAME :String = "contato.db"
        private val VERSION: Int = 1
    }

    val TABLENAME = "contato"
    val COLUMNS_ID = "id INTEGER NOT NULL"
    val COLUMNS_NOME = "nome TEXT NOT NULL"
    val COLUMNS_TELEFONE = "telefone TEXT NOT NULL"
    val DROP_TABLE = "DROP TABLE IF EXISTS  $TABLENAME"
    val CREATE_TABLE = "CREATE TABLE $TABLENAME(" +
            "$COLUMNS_ID PRIMARY KEY," +
            "$COLUMNS_NOME," +
            "$COLUMNS_TELEFONE)"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion){
            db?.execSQL(DROP_TABLE)
            onCreate(db)
        }
    }

    fun buscarContatos(busca: String? = null, isBuscaPorId: Boolean = false): List<ContatosVO>{
        val db = readableDatabase?: return mutableListOf()
        var contatos = mutableListOf<ContatosVO>()
        var where: String? = null
        var args: Array<String> = arrayOf()
        if(isBuscaPorId){
             where = "id = ?"
             args = arrayOf("$busca")
        }else{
             where = "nome LIKE ?"
             args = arrayOf("%$busca%")

        }
        var cursor = db.query(TABLENAME,null, where, args, null, null, null )
        while(cursor.moveToNext()){
            var contato = ContatosVO(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("nome")),
                cursor.getString(cursor.getColumnIndex("telefone"))
            )
            contatos.add(contato)
        }
        db.close()
        return contatos
    }
    fun salvarContato(contato: ContatosVO){
        val db = writableDatabase?: return
        var query = "INSERT INTO $TABLENAME (nome, telefone) VALUES (?,?)"
        var arr = arrayOf(contato.nome, contato.telefone)
        db.execSQL(query, arr)
        db.close()

    }
    fun deleteContato(id: String){
        val db = writableDatabase?: return
        var where = "id = ?"
        var args = arrayOf(id)
        db.delete(TABLENAME,where, args)
        db.close()
    }
    fun updateContato(contato: ContatosVO){
        val db = writableDatabase?: return
        var where = "id = ?"
        var args = arrayOf(contato.id.toString())
        val content = ContentValues()
        content.put("nome", contato.nome)
        content.put("telefone", contato.telefone)
        db.update(TABLENAME, content ,where, args)
    }
}