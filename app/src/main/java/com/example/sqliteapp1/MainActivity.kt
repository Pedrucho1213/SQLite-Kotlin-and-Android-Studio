package com.example.sqliteapp1

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = findViewById<EditText>(R.id.editName)
        val data = findViewById<EditText>(R.id.editData)
        val savebtn = findViewById<Button>(R.id.btnSave)
        val deletebtn = findViewById<Button>(R.id.btnDelete)
        val getbtn = findViewById<Button>(R.id.btnGet)
        val updatebtn = findViewById<Button>(R.id.btnUpdate)


        // Insert data
        savebtn.setOnClickListener {
            val admin = DataBase(this, "prueba", null, 1)
            val db = admin.writableDatabase
            val record = ContentValues()
            record.put("nombre", name.text.toString())
            record.put("descripccion", data.text.toString())
            db.insert("personas", null, record)
            db.close()
            name.text = null
            data.text = null
            record.clear()
            Toast.makeText(this, "Se cargaron los datos correctamente", Toast.LENGTH_SHORT).show()
        }

        // Get data
        getbtn.setOnClickListener {
            val admin = DataBase(this, "prueba", null, 1)
            val db = admin.writableDatabase
            if (db == null) {
                Toast.makeText(this, "No db", Toast.LENGTH_SHORT).show()
            } else {
                val sql = "select descripccion from personas where nombre = '${name.text}'"
                val row = db.rawQuery(sql, null)
                row.moveToNext()
                if (row.moveToFirst()) {
                    val dsc = row.getString(row.getColumnIndex("descripccion"))
                    data.setText(dsc)
                } else {
                    Toast.makeText(this, "No existe registros", Toast.LENGTH_SHORT).show()
                    db.close()
                    row.close()
                }
            }
        }

        // Update data
        updatebtn.setOnClickListener{
            val admin = DataBase(this, "prueba", null, 1)
            val db = admin.writableDatabase
            val record = ContentValues()
            record.put("descripccion", data.text.toString())
            val cant = db.update("personas", record, "nombre =?", arrayOf(name.text.toString()))
            db.close()
            name.text = null
            data.text = null
            Toast.makeText(this, "$cant", Toast.LENGTH_SHORT).show()
            if (cant == 1){
                Toast.makeText(this, "Se actualizó el registro con exito", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "Error en el update", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete data
        deletebtn.setOnClickListener{
            val admin = DataBase(this, "prueba", null, 1)
            val db = admin.writableDatabase
            if (db == null){
                Toast.makeText(this,"No existe DB", Toast.LENGTH_SHORT).show()
            }else{
                val cnt = db.delete("personas", "nombre =?", arrayOf(name.text.toString()))
                db.close()
                name.text = null
                data.text = null
                if (cnt == 1){
                    Toast.makeText(this, "Se borro el registro con exito", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "No existe el registro", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}