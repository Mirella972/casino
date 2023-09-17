package com.tp1.casino

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class BanqueActivity : Activity(), View.OnClickListener {
    private lateinit var btnValidationBanque: Button
    lateinit var prefs: SharedPreferences
    var solde: Int = 0
    var session: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banque)

        // Initialisation du boutton
        btnValidationBanque = findViewById(R.id.btn_validation_banque)

        // Définition de l'écoute du boutton
        btnValidationBanque.setOnClickListener(this)

        // Récupération du SharedPreferences
        prefs = getSharedPreferences("MonFichierDeSauvegarde", MODE_PRIVATE)
        session = prefs.getString("session", "Rien").toString()
        solde = prefs.getInt(session, 0)
    }

    override fun onClick(v: View) {
        val intent: Intent
        when (v.id){
            R.id.btn_validation_banque -> {
                val editor = prefs.edit()
                val montant = findViewById<EditText>(R.id.edit_montant).text.toString()
                val intMontant = Integer.valueOf(montant)
                solde += intMontant
                editor.putInt(session, solde)
                editor.apply()
                intent = Intent(this@BanqueActivity, AccueilActivity::class.java)
                startActivity(intent)
            }
        }
    }
}