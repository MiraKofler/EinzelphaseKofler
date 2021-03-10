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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMatrikelnummer = findViewById(R.id.edittext_matrikelnummer);
        buttonSend = findViewById(R.id.button_send);
        textViewResult = findViewById(R.id.textview_result);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerButtonSend();

            }
        });

        textViewCalculate = findViewById(R.id.textview_Calculate);
        buttonCalculate = (Button) findViewById(R.id.button_calculate);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerButtonCalculate();

            }
        });
    }

    private void listenerButtonSend(){
        new Thread() {
            public void run() {
                String sentence = editTextMatrikelnummer.getText().toString();
                try {
                    Socket clientSocket = new Socket("se2-isys.aau.at", 53212);

                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                    BufferedReader inFromServer = new BufferedReader
                            (new InputStreamReader(clientSocket.getInputStream()));

                    outToServer.writeBytes(sentence + '\n');

                    String modifiedSentence = inFromServer.readLine();

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

    private void listenerButtonCalculate() throws NumberFormatException{
        try {
            new Thread() {
                public void run() {
                    if (editTextMatrikelnummer.getText().toString().length() == 0) {
                        editTextMatrikelnummer.setText(-1);
                    }

                    int num = Integer.parseInt(editTextMatrikelnummer.getText().toString());
                    int result = toArray(num);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result == -1) {
                                textViewCalculate.setText(-1);
                            } else {
                                textViewCalculate.setText(String.valueOf(result));
                            }
                        }
                    });

                }
            }.start();
        }catch(NumberFormatException e){

        }
    }

    private int toArray(int num) {
        //save the number in an Array
        String buf = Integer.toString(num);
        int[] array = new int[buf.length()];
        for (int i = 0; i < buf.length(); i++) {
            array[i] = Character.getNumericValue(buf.charAt(i));
        }
        array = bubbleSort(array);
        int result = finalProduct(array);

        return result;
    }

    private int finalProduct(int[] array){
        //turns array into an integer
        int result = 0;
        int primenum = 0;
        for (int a : array) {
            if(a == 2 || a == 3 || a == 5 || a == 7){
                primenum = 10 * result + a;
            }else {
                result = 10 * result + a;
            }
        }

        if(primenum > 10000000)     return -1;
        else                        return result;
    }

    private static int[] bubbleSort(int[] arr) {
        int n = arr.length;
        int temp;
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