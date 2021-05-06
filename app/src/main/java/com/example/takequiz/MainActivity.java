package com.example.takequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView questions;
    TextInputLayout answers;
    Button btn;
    private String URL1 = "https://teamtntfcc.sytes.net/quiz/consult.php";
    RequestQueue requestQueue;
    ArrayList<String> preguntas = new ArrayList<String>();
    ArrayList<String> respuestas = new ArrayList<String>();
    int count = 0;
    int correctas = 0 ;
    int incorrectas = 0 ;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn =findViewById(R.id.sendAnswer);
        questions = findViewById(R.id.question);
        answers = findViewById(R.id.answer);
        requestQueue = Volley.newRequestQueue(this);
        questions.setText("");
        answers.getEditText().setEnabled(false);
        JsonRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL1,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("datos");

                            for(int i = 0 ; i < jsonArray.length();i++)
                            {
                                JSONObject questionss = jsonArray.getJSONObject(i);

                                preguntas.add( questionss.getString("question"));
                                respuestas.add(questionss.getString("answer"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }

    public void matchs(View v)
    {

        if(flag == true)
        {
            btn.setText("Enviar Respuesta");
            questions.append(preguntas.get(count));
            answers.getEditText().setEnabled(true);
            flag = false;
        }else
        {
            String texto = answers.getEditText().getText().toString().trim();
            if(!texto.isEmpty())
            {
                if(respuestas.get(count).equals(texto))
                {
                    correctas++;
                    count++;
                    answers.getEditText().setText("");
                    answers.getEditText().setFocusable(true);
                    Toast.makeText(this,"Respuesta Correcta, preguntas: "+Integer.toString(count)+"/"+Integer.toString(preguntas.size())
                            , Toast.LENGTH_SHORT).show();

                }else
                {
                    incorrectas++;
                    count++;
                    answers.getEditText().setText("");
                    answers.getEditText().setFocusable(true);
                    Toast.makeText(this,"Respuesta Incorrecta, preguntas: "+Integer.toString(count)+"/"+Integer.toString(preguntas.size())
                            , Toast.LENGTH_SHORT).show();
                }
                if(count < preguntas.size())
                {
                    questions.setText("");
                    questions.append(preguntas.get(count));
                }else
                {
                    questions.setText("");
                    btn.setEnabled(false);
                    answers.getEditText().setEnabled(false);
                }
            }else
            {
                questions.setError("El campo no puede estra vacio");
            }
        }

    }
}