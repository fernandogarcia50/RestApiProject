package com.example.restapiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pokemon= findViewById<EditText>(R.id.et_pokemon_to_search_for)
        val btnSearch=findViewById<Button>(R.id.btn_search)

        queue=Volley.newRequestQueue(this)


        btnSearch.setOnClickListener {
            getPokemon(pokemon.text.toString())
            pokemon.text.clear()
        }
    }

    fun getPokemon(pokemonName: String){
        val url= "https://pokeapi.co/api/v2/pokemon/${pokemonName.lowercase()}"
        val pokemonInfo = findViewById<TextView>(R.id.tv_pokemon_info)
        val jsonRequest = JsonObjectRequest(url,
            Response.Listener<JSONObject> { response->
                val id = response.getInt("id")
                val peso = response.getInt("weight")
                val tipo = response.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name")
                val nombre = response.getString("name")
                val hp = response.getJSONArray("stats").getJSONObject(0).getInt("base_stat")
                val ataqye = response.getJSONArray("stats").getJSONObject(1).getInt("base_stat")
                val defensa = response.getJSONArray("stats").getJSONObject(2).getInt("base_stat")
                val velocidad = response.getJSONArray("stats").getJSONObject(5).getInt("base_stat")
                val infoString = "Nombre: $nombre # Id: $id #Tipo:  $tipo # Peso: $peso # Hp: $hp #Ataque: $ataqye # Defensa: $defensa# Velocidad: $velocidad"
                    pokemonInfo.setText(infoString)
                  Log.d("JsonResponse", "response: $infoString")
            },
            Response.ErrorListener { errorMessage ->
                pokemonInfo.setText("Error 404 pokemon no encontrado")
                Log.d("JsonResponse", "error: $errorMessage")

            })
            queue.add(jsonRequest)
    }

    override fun onStop() {
        super.onStop()
        queue.cancelAll("stopped")
    }
}