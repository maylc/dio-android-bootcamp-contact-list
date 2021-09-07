package io.github.maylcf.contactslist.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.github.maylcf.contactslist.model.Contact

class HelperDB(
    context: Context
) : SQLiteOpenHelper(context, dbName, null, version) {

    companion object {
        private const val dbName = "contato.db"
        private const val version = 2
    }

    val TABLE_NAME = "contato"
    val COLUMNS_ID = "id"
    val COLUMNS_NOME = "nome"
    val COLUMNS_TELEFONE = "telefone"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_NOME TEXT NOT NULL," +
            "$COLUMNS_TELEFONE TEXT NOT NULL," +
            "" +
            "PRIMARY KEY($COLUMNS_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun searchContact(search: String, isSearchingById: Boolean = false): List<Contact> {
        val db = readableDatabase ?: return mutableListOf()
        val list = mutableListOf<Contact>()
        var where: String? = null
        var args: Array<String> = arrayOf()
        if (isSearchingById) {
            where = "$COLUMNS_ID = ?"
            args = arrayOf("$search")
        } else {
            where = "$COLUMNS_NOME LIKE ?"
            args = arrayOf("%$search%")
        }
        val cursor = db.query(TABLE_NAME, null, where, args, null, null, null)
        if (cursor == null) {
            db.close()
            return mutableListOf()
        }
        while (cursor.moveToNext()) {
            val contato = Contact(
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_TELEFONE))
            )
            list.add(contato)
        }
        db.close()
        return list
    }

    fun saveContact(contact: Contact) {
        val db = writableDatabase ?: return
        val content = ContentValues()
        content.put(COLUMNS_NOME, contact.name)
        content.put(COLUMNS_TELEFONE, contact.phone)
        db.insert(TABLE_NAME, null, content)
        db.close()
    }

    fun deleteContact(id: Int) {
        val db = writableDatabase ?: return
        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMNS_ID = ?"
        val arg = arrayOf("$id")
        db.execSQL(sql, arg)
        db.close()
    }

    fun updateContact(contact: Contact) {
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_NOME = ?, $COLUMNS_TELEFONE = ? WHERE $COLUMNS_ID = ?"
        val arg = arrayOf(contact.name, contact.phone, contact.id)
        db.execSQL(sql, arg)
        db.close()
    }
}