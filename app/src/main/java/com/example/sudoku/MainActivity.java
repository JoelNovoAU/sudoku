package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.inicio);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnNuevaPartida = findViewById(R.id.btnNuevaPartida);
        Button btnPuntuaciones = findViewById(R.id.btnPuntuaciones);
        Button btnSalir = findViewById(R.id.btnSalir);


        btnNuevaPartida.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Dificultad.class);
            startActivity(intent);
        });

        btnPuntuaciones.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PuntuacionesActivity.class);
            startActivity(intent);
        });

        btnSalir.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Salir")
                    .setMessage("¿Estás seguro de que quieres salir?")
                    .setPositiveButton("Sí", (dialog, which) -> finishAffinity())
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
