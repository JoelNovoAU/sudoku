package com.example.sudoku;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Juego extends AppCompatActivity {

    private static final int CELLS = 9;
    private static final int CELL_SIZE_DP = 30;

    private int[][] puzzle;
    private Chronometer cronometro;
    private GridLayout grid;
    private int dificultad = 1;

    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);

        dificultad = getIntent().getIntExtra("dificultad", 1);
        nombre = getIntent().getStringExtra("nombre");

        cronometro = findViewById(R.id.cronometro);
        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.start();

        grid = findViewById(R.id.gridSudoku);
        generarNuevoSudoku();

        Button btn = findViewById(R.id.btnNuevaPartida);
        btn.setOnClickListener(v -> {
            long tiempoMilis = SystemClock.elapsedRealtime() - cronometro.getBase();
            new Thread(() -> enviarPuntuacion(tiempoMilis, dificultad)).start();
        });
    }

    private void generarNuevoSudoku() {
        int empties = obtenerEmptiesPorDificultad();
        Generador generator = new Generador();
        puzzle = generator.generatePuzzle(empties);
        cargarTablero();
    }

    private int obtenerEmptiesPorDificultad() {
        switch (dificultad) {
            case 1:
                return 30;
            case 2:
                return 40;
            case 3:
                return 50;
            default:
                return 30;
        }
    }

    private void cargarTablero() {
        grid.removeAllViews();
        grid.setColumnCount(CELLS);
        grid.setRowCount(CELLS);

        float scale = getResources().getDisplayMetrics().density;
        int cellPx = (int) (CELL_SIZE_DP * scale + 0.5f);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = puzzle[r][c];
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cellPx * 2;
                params.height = cellPx * 2;
                params.setMargins(4, 4, 4, 4);

                if (val != 0) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(params);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tv.setTypeface(Typeface.DEFAULT_BOLD);
                    tv.setBackgroundResource(R.drawable.celda);
                    tv.setText(String.valueOf(val));
                    tv.setTextColor(getResources().getColor(R.color.whote));
                    tv.setId(1000 + r * 9 + c);
                    grid.addView(tv);
                } else {
                    EditText et = new EditText(this);
                    et.setLayoutParams(params);
                    et.setGravity(Gravity.CENTER);
                    et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    et.setTypeface(Typeface.DEFAULT_BOLD);
                    et.setBackgroundResource(R.drawable.celda);
                    et.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

                    et.setFilters(new InputFilter[]{
                            new InputFilter.LengthFilter(1),
                            (source, start, end, dest, dstart, dend) -> {
                                if (source.length() == 0) return null;
                                if (source.toString().matches("[1-9]")) return source;
                                return "";
                            }
                    });
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);

                    int finalR = r;
                    int finalC = c;
                    et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String input = s.toString();
                            if (input.isEmpty()) {
                                et.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                                return;
                            }

                            int num = Integer.parseInt(input);
                            int[][] currentBoard = new int[9][9];

                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < 9; j++) {
                                    TextView cell = grid.findViewById(1000 + i * 9 + j);
                                    if (cell instanceof EditText) {
                                        String text = ((EditText) cell).getText().toString();
                                        currentBoard[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
                                    } else {
                                        currentBoard[i][j] = puzzle[i][j];
                                    }
                                }
                            }

                            currentBoard[finalR][finalC] = 0;

                            if (dificultad == 3) {
                                et.setTextColor(getResources().getColor(android.R.color.white));
                                return;
                            }

                            if (!isMoveValid(currentBoard, finalR, finalC, num)) {
                                et.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            } else {
                                et.setTextColor(getResources().getColor(android.R.color.white));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    et.setId(1000 + r * 9 + c);
                    grid.addView(et);
                }
            }
        }
    }

    private boolean isMoveValid(int[][] board, int r, int c, int num) {
        for (int i = 0; i < 9; i++) if (board[r][i] == num) return false;
        for (int i = 0; i < 9; i++) if (board[i][c] == num) return false;
        int sr = (r / 3) * 3;
        int sc = (c / 3) * 3;
        for (int i = sr; i < sr + 3; i++)
            for (int j = sc; j < sc + 3; j++)
                if (board[i][j] == num) return false;
        return true;
    }

    private void enviarPuntuacion(long tiempoMillis, int dificultad) {

        int totalSegundos = (int) (tiempoMillis / 1000);
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;

        new Thread(() -> {
            try {
                Log.d("Juego", "Intentando conectar a API...");
                URL url = new URL("http://10.0.2.2:3000/puntuacion");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                String json = "{ \"nombre\": \"" + nombre + "\"" +
                        ", \"minutos\": " + minutos +
                        ", \"segundos\": " + segundos +
                        ", \"dificultad\": " + dificultad + " }";


                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.flush();
                os.close();

                int code = conn.getResponseCode();
                Log.d("Juego", "Código de respuesta: " + code);

                InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);
                reader.close();
                conn.disconnect();

                Log.d("Juego", "RESPUESTA API: " + response.toString());

                runOnUiThread(() -> {
                    if (code >= 200 && code < 300) {
                        Toast.makeText(Juego.this, "Puntuación enviada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Juego.this, "Error al enviar puntuación", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(Juego.this, MainActivity.class));
                });

            } catch (Exception e) {
                Log.e("Juego", "Error al enviar puntuación", e);
                runOnUiThread(() ->
                        Toast.makeText(Juego.this, "Error al enviar puntuación", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

}
