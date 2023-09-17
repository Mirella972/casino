package com.tp1.casino

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class RouletteActivity : AppCompatActivity(),View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonPair: RadioButton
    private lateinit var radioButtonImpair: RadioButton
    private lateinit var mise: EditText
    private lateinit var btnJouer: Button
    lateinit var prefs: SharedPreferences
    private lateinit var nbreMiser: EditText
    var solde: Int = 0
    var session: String = ""
    var gain: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roulette)
        bindViews()
    }

    private fun bindViews() {
        // Initialisation des bouttons
        nbreMiser = findViewById(R.id.editTextNumeroMiser)
        radioGroup = findViewById(R.id.radioGroup)
        radioButtonPair = findViewById(R.id.radioButtonPair)
        radioButtonImpair = findViewById(R.id.radioButtonImpair)
        btnJouer = findViewById(R.id.btn_jouer)
        mise = findViewById(R.id.editTextNumberMise)

        // Définition des écoutes des bouttons
        radioGroup.setOnCheckedChangeListener(this)
        btnJouer.setOnClickListener(this)

        // Récupération du SharedPreferences
        prefs = getSharedPreferences("MonFichierDeSauvegarde", MODE_PRIVATE)
        session = prefs.getString("session", "Rien").toString()
        solde = prefs.getInt(session, 0)
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.radioButtonImpair -> if (radioButtonImpair!!.isChecked){
                nbreMiser!!.setText("")
                nbreMiser.isEnabled = false
            }
            R.id.radioButtonPair -> if (radioButtonPair!!.isChecked){
                nbreMiser!!.setText("")
                nbreMiser.isEnabled = false
            }
        }
    }

    override fun onClick(v: View){
        val intent: Intent
        var victoire = false
        when (v.id){
            R.id.btn_jouer -> {
                /*
                * Gestion du jeu
                * une mise est obligatoire
                * le nombre miser <= 36
                * la mise <= solde
                * */
                if (!nbreMiser.text.isNotEmpty() && !mise.text.isNotEmpty() && !radioButtonImpair!!.isChecked && !radioButtonPair!!.isChecked){
                    Snackbar.make(v, "La mise est obligatoire. Faites vos jeux !", Snackbar.LENGTH_LONG).show()
                }else if (nbreMiser.text.isNotEmpty() && nbreMiser.text.toString().toInt() > 36){
                    nbreMiser.text.clear()
                    Snackbar.make(v, "Le numéro misé doit être entre 0 et 36 inclus.", Snackbar.LENGTH_LONG).show()
                }else if(mise.text.isNotEmpty() && mise.text.toString().toInt() > solde){
                    mise.text.clear()
                    Snackbar.make(v, "Votre mise ne peux pas être supérieur à votre solde de $solde.", Snackbar.LENGTH_LONG).show()
                } else {
                    val nombreAleatoire = Random.nextInt(37)
                    Log.d("nombre aleatoire", "$nombreAleatoire")
                    if(nombreAleatoire % 2 == 0) {
                        if (radioButtonPair!!.isChecked) {
                            gain = mise.text.toString().toInt() * 2
                            victoire = true
                        }
                    } else {
                        if (radioButtonImpair!!.isChecked){
                            gain = mise.text.toString().toInt() * 2
                            victoire = true
                        }
                    }
                    if (nbreMiser.text.isNotEmpty() && nbreMiser.text.toString().toInt() == nombreAleatoire){
                        gain = mise.text.toString().toInt() * 36
                        victoire = true
                    }
                    if(victoire){
                        solde += gain
                    } else {
                        solde -= mise.text.toString().toInt()
                    }
                    // Mettre à jour le SharedPreferences
                    val editor = prefs.edit()
                    editor.putInt(session, solde)
                    editor.apply()
                    Log.d("nouveau solde", "$solde")
                    // Retour à l'accueil si le solde est nul
                    if (solde <= 0){
                        Snackbar.make(v, "Votre solde est insuffisant.", 10).show()
                        intent = Intent(this@RouletteActivity, AccueilActivity::class.java)
                        startActivity(intent)
                    }
                    Log.d("nombre miser", "$nbreMiser")
                    Log.d("gain", "$gain")
                    Log.d("solde", "$solde")
                    // Affichage du résultat et du nouveau solde, vider les différents champs.
                    Snackbar.make(v, "Le numéro sortant est le : $nombreAleatoire. Votre solde est de $solde.", Snackbar.LENGTH_LONG).show()
                    radioButtonPair.isChecked = false
                    radioButtonImpair.isChecked = false
                    nbreMiser.isEnabled = true
                    nbreMiser.text.clear()
                    mise.text.clear()
                }
            }
        }
    }
}