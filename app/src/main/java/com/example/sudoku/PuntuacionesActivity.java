package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PuntuacionesActivity extends AppCompatActivity {

    RecyclerView recycler;
    Spinner spinnerDificultad;

    ArrayList<Puntuaciones> listaCompleta = new ArrayList<>();
    ArrayList<Puntuaciones> listaFiltrada = new ArrayList<>();

    PuntuacionesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuaciones);

        recycler = findViewById(R.id.recyclerPuntuaciones);
        spinnerDificultad = findViewById(R.id.spinnerDificultad);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PuntuacionesAdapter(listaFiltrada);
        recycler.setAdapter(adapter);

        configurarSpinner();
        cargarPuntuaciones();
    }

    private void configurarSpinner() {
        String[] opciones = {"Todas", "Fácil", "Medio", "Difícil"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDificultad.setAdapter(spinnerAdapter);

        spinnerDificultad.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                filtrarPorDificultad(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void filtrarPorDificultad(int opcion) {
        listaFiltrada.clear();

        for (Puntuaciones p : listaCompleta) {
            if (opcion == 0 || p.getDificultad() == opcion) {
                listaFiltrada.add(p);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void cargarPuntuaciones() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:3000/verpuntuaciones");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    json.append(line);
                }

                JSONArray arr = new JSONArray(json.toString());

                listaCompleta.clear();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);

                    String nombre = o.getString("nombre");
                    int minutos = o.getInt("minutos");
                    int segundos = o.getInt("segundos");
                    int dificultad = o.getInt("dificultad");

                    listaCompleta.add(new Puntuaciones(nombre, minutos, segundos, dificultad));
                }

                runOnUiThread(() -> filtrarPorDificultad(spinnerDificultad.getSelectedItemPosition()));

            } catch (Exception e) {
                Log.e("API", "Error cargando puntuaciones", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "Error al cargar puntuaciones", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}
