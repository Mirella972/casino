package com.tp1.casino

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.content.SharedPreferences
import android.util.Log
import android.widget.Button
import android.widget.TextView

class AccueilActivity : Activity(), View.OnClickListener {
    private lateinit var btnRoulette: Button
    private lateinit var btnBanque: Button
    lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        // Initialisation des boutons
        btnRoulette = findViewById(R.id.btn_roulette)
        btnBanque = findViewById(R.id.btn_banque)

        // Définition des écoutes des bouttons
        btnRoulette.setOnClickListener(this)
        btnBanque.setOnClickListener(this)

        // Récupération du SharedPreferences
        prefs = getSharedPreferences("MonFichierDeSauvegarde", MODE_PRIVATE)
        var utilisateur = prefs.getString("session", "Rien").toString()
        var solde = prefs.getInt(utilisateur, 0)

        // Affichage du nom utilisateur et de son solde
        var afficherSolde = findViewById<TextView>(R.id.textView_solde)
        afficherSolde.text = "$utilisateur - solde : $solde"

        Log.d("session", "donnee : $utilisateur, $solde")

        // Désactivation du menu roulette si le solde est nul
        if (solde == 0){
            btnRoulette.setEnabled(false)
        }
    }

    override fun onClick(v: View) {
        val intent: Intent
        when (v.id){
            R.id.btn_roulette -> {
                intent = Intent(this@AccueilActivity, RouletteActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_banque -> {
                intent = Intent(this@AccueilActivity, BanqueActivity::class.java)
                startActivity(intent)
            }
        }
    }
}