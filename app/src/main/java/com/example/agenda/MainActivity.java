package com.example.agenda;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText name, phone;

    Button save, search, close;

    CxMsg msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);

        save = findViewById(R.id.save);
        search = findViewById(R.id.search);
        close = findViewById(R.id.close);

        BancoDados.abrirBanco(this);
        BancoDados.abrirOuCriarTabela(this);
        BancoDados.fecharDB();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void inserirRegistro(View v){
        String nameText = name.getText().toString();
        String phoneText = phone.getText().toString();

        if(nameText.equals("") || phoneText.equals("")){
            CxMsg.mostrar("Campos não podem estar vazios!", this);
            return;
        }

        BancoDados.inserirRegistro(nameText, phoneText, this);

        name.setText("");
        phone.setText("");
    }


    public void telaConsulta(View v){
        Intent telaConsulta = new Intent(this, TelaConsulta.class);
        startActivity(telaConsulta);
    }

    public void fecharTela(View v){
        Intent tela = new Intent(this, MainActivity.class);
        startActivity(tela);
    }

}