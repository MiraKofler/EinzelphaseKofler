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

    public static TextView textViewCalculate;
    public Button buttonCalculate;


    public static void main(String[] args) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMatrikelnummer = findViewById(R.id.edittext_matrikelnummer);
        buttonSend = (Button) findViewById(R.id.button_send);
        textViewResult = findViewById(R.id.textview_result);
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

        textViewCalculate = findViewById(R.id.textview_Calculate);
        buttonCalculate = (Button) findViewById(R.id.button_calculate);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        if (editTextMatrikelnummer.getText().toString().length() == 0) {
                            editTextMatrikelnummer.setText(0);
                        }

                        int num = Integer.parseInt(editTextMatrikelnummer.getText().toString());
                        int result = toArray(num);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewCalculate.setText(String.valueOf(result));
                            }
                        });

                    }
                }.start();
            }
        });
    }

    private int toArray(int num) {
        //save the number in a Array
        String buf = Integer.toString(num);
        int[] array = new int[buf.length()];
        for (int i = 0; i < buf.length(); i++) {
            array[i] = Character.getNumericValue(buf.charAt(i));
        }

        //sort the array
        array = bubbleSort(array);


        //turns array into an integer
        int result = 0;
        for (int a : array) {
            result = 10 * result + a;
        }

        return result;
    }

    private static int[] bubbleSort(int[] arr) {
        int n = arr.length;
        int temp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (arr[j - 1] > arr[j]) {
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }

            }
        }
        return arr;
    }


}


//therad starten
//thread verbinden mit nachricht
//nachricht darstellen
//entweder in main warten mit getter und setter PROBLEM haben thread für netzwerk methode aber main thread warten wir sthene
//BESSER im thread kann man mit RUNSUITHREAT einen code schreoben der auch auf das user interface zugreifen kann und es ändern darf
//im netzwerk thread verbinden, mit antwort runuithreat(runnable) und darin auf die ui elemente zugreifen und ändern