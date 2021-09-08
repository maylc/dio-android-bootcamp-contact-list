package io.github.maylcf.contactslist.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.github.maylcf.contactslist.model.Contact

class HelperDB(context: Context) : SQLiteOpenHelper(context, dbName, null, version) {

    companion object {
        private const val dbName = "contato.db"
        private const val version = 2
    }

    private val tableName = "contato"
    private val columnId = "id"
    private val columnName = "nome"
    private val columnPhone = "telefone"

    private val dropTableSql = "DROP TABLE IF EXISTS $tableName"
    private val createTableSql = "CREATE TABLE $tableName (" +
            "$columnId INTEGER NOT NULL," +
            "$columnName TEXT NOT NULL," +
            "$columnPhone TEXT NOT NULL," +
            "PRIMARY KEY($columnId AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL(dropTableSql)
        }
        onCreate(db)
    }

    fun searchContact(search: String, isSearchingById: Boolean = false): List<Contact> {
        val db = readableDatabase ?: return mutableListOf()
        val list = mutableListOf<Contact>()
        val where: String?
        val args: Array<String>

        if (isSearchingById) {
            where = "$columnId = ?"
            args = arrayOf(search)
        } else {
            where = "$columnName LIKE ?"
            args = arrayOf("%$search%")
        }

        val cursor = db.query(tableName, null, where, args, null, null, null)

        if (cursor == null) {
            db.close()
            return mutableListOf()
        }

        while (cursor.moveToNext()) {
            val contact = Contact(
                cursor.getInt(cursor.getColumnIndex(columnId)),
                cursor.getString(cursor.getColumnIndex(columnName)),
                cursor.getString(cursor.getColumnIndex(columnPhone))
            )
            list.add(contact)
        }

        db.close()
        return list
    }

    fun saveContact(contact: Contact) {
        val db = writableDatabase ?: return
        val content = ContentValues()
        content.put(columnName, contact.name)
        content.put(columnPhone, contact.phone)
        db.insert(tableName, null, content)
        db.close()
    }

    fun deleteContact(id: Int) {
        val db = writableDatabase ?: return
        val sql = "DELETE FROM $tableName WHERE $columnId = ?"
        val arg = arrayOf("$id")
        db.execSQL(sql, arg)
        db.close()
    }

    fun updateContact(contact: Contact) {
        val db = writableDatabase ?: return
        val sql = "UPDATE $tableName SET $columnName = ?, $columnPhone = ? WHERE $columnId = ?"
        val arg = arrayOf(contact.name, contact.phone, contact.id)
        db.execSQL(sql, arg)
        db.close()
    }
}