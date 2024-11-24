package com.example.produit

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Product(val id: Int = 0, val name: String, val price: String, val description: String)

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "products.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "products"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_PRICE = "price"
        const val COL_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT,
                $COL_PRICE TEXT,
                $COL_DESCRIPTION TEXT
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, product.name)
            put(COL_PRICE, product.price)
            put(COL_DESCRIPTION, product.description)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, product.name)
            put(COL_PRICE, product.price)
            put(COL_DESCRIPTION, product.description)
        }
        db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(product.id.toString()))
        db.close()
    }

    fun deleteProduct(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getProducts(): List<Product> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val products = mutableListOf<Product>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME))
                val price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION))
                products.add(Product(id, name, price, description))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return products
    }
}
