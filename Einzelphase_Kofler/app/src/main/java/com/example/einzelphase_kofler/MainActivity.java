package com.example.einzelphase_kofler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public EditText editTextMatrikelnummer;
    public static TextView textViewResult;
    public Button buttonSend;


    public static void main(String[] args) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMatrikelnummer = findViewById(R.id.edittext_matrikelnummer);
        buttonSend = (Button) findViewById(R.id.button_send);
        textViewResult = findViewById(R.id.textview_result);
        //textViewResult.setText(mod);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        String sentence = editTextMatrikelnummer.getText().toString();
                        String modifiedSentence;

                        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                        try {
                            Socket clientSocket = new Socket("se2-isys.aau.at", 53212);

                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                            BufferedReader inFromServer = new BufferedReader
                                    (new InputStreamReader(clientSocket.getInputStream()));

                            //sentence = editTextMatrikelnummer.getText().toString();

                            outToServer.writeBytes(sentence + '\n');

                            modifiedSentence = inFromServer.readLine();

                            //System.out.println("FROM SERVER: " + modifiedSentence);
                            //RUNSUITHREAT einen code schreoben der auch auf das user interface zugreifen kann und es ändern darf
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textViewResult.setText(modifiedSentence);
                                }
                            });

                            clientSocket.close();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }


}


//therad starten
//thread verbinden mit nachricht
//nachricht darstellen
//entweder in main warten mit getter und setter PROBLEM haben thread für netzwerk methode aber main thread warten wir sthene
//BESSER im thread kann man mit RUNSUITHREAT einen code schreoben der auch auf das user interface zugreifen kann und es ändern darf
//im netzwerk thread verbinden, mit antwort runuithreat(runnable) und darin auf die ui elemente zugreifen und ändern