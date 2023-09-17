package com.tp1.casino

import android.app.Activity
import android.content.SharedPreferences
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : Activity(), View.OnClickListener {
    private lateinit var btnConnexion: Button
    lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnConnexion = findViewById(R.id.btn_connexion)

        btnConnexion.setOnClickListener(this)

        prefs = getSharedPreferences("MonFichierDeSauvegarde", MODE_PRIVATE)
    }

    override fun onClick(v: View) {
        val intent: Intent
        when (v.id){
            R.id.btn_connexion -> {
                val nom = findViewById<EditText>(R.id.nom)
                try {
                    val editor = prefs.edit()
                    if(!prefs.contains(nom.text.toString()) && nom.text.toString().isNotEmpty()) {
                        editor.putInt(nom.text.toString(), 15)
                    }
                    editor.putString("session", nom.text.toString())
                    editor.apply()
                } catch (e: Exception) {
                    Log.d("validation", "nom vide")
                }
                intent = Intent(this@MainActivity, AccueilActivity::class.java)
                startActivity(intent)
            }
        }
    }
}