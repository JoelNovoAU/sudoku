package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class Dificultad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dificultad);

        EditText etNombre = findViewById(R.id.etNombre);
        Button btnFacil = findViewById(R.id.btnFacil);
        Button btnMedio = findViewById(R.id.btnMedio);
        Button btnDificil = findViewById(R.id.btnDificil);

        btnFacil.setOnClickListener(v -> enviarDificultad(1));
        btnMedio.setOnClickListener(v -> enviarDificultad(2));
        btnDificil.setOnClickListener(v -> enviarDificultad(3));
    }

    private void enviarDificultad(int nivel) {
        EditText etNombre = findViewById(R.id.etNombre);
        String nombre = etNombre.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("Introduce tu nombre");
            return;
        }

        Intent intent = new Intent(Dificultad.this, Juego.class);
        intent.putExtra("dificultad", nivel);
        intent.putExtra("nombre", nombre);

        startActivity(intent);
        finish();
    }

}
