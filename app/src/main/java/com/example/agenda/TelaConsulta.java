package com.example.agenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class TelaConsulta extends AppCompatActivity {

    EditText name, phone;

    Button previous, next, back;

    SQLiteDatabase db = null;

    Cursor cursor;

    List<String[]> lista;

    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_consulta);

        name = findViewById(R.id.nameSearch);
        phone = findViewById(R.id.phoneSearch);

        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);

        buscarDados();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void abrirBanco(){
        try{
            db = openOrCreateDatabase("bancoAgenda", MODE_PRIVATE, null);
        } catch(Exception ex){
            CxMsg.mostrar("Erro ao abrir ou criar banco de dados!", this);
        }
    }

    public void buscarDados(){
        abrirBanco();
        cursor = db.query(
                "contatos",
                new String[]{"id", "nome", "fone"},
                null, null, null, null, null, null
        );

        if(cursor != null && cursor.getCount() > 0){
            lista = new ArrayList<>();
            cursor.moveToFirst();

            do{
                int iIndex = cursor.getColumnIndex("id");
                int nIndex = cursor.getColumnIndex("nome");
                int fIndex = cursor.getColumnIndex("fone");

                if (nIndex >= 0 && fIndex >= 0) {
                    String id = cursor.getString(iIndex);
                    String nome = cursor.getString(nIndex);
                    String fone = cursor.getString(fIndex);
                    lista.add(new String[]{id, nome, fone});
                } else {
                    CxMsg.mostrar("Colunas não encontradas no cursor!", this);
                }

            } while (cursor.moveToNext());

            mostrarDados();
        } else {
            Toast.makeText(this, "Nenhum registro encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrarDados(){

        if(lista != null && currentIndex >= 0 && currentIndex < lista.size()){
            String[] currentData = lista.get(currentIndex);
            name.setText(currentData[1]);
            phone.setText(currentData[2]);
        }

    }

    public void update(View v){
        String nameText = name.getText().toString();
        String phoneText = phone.getText().toString();

        String[] currentData = lista.get(currentIndex);

        String id = currentData[0];

        if(nameText.equals("") || phoneText.equals("")){
            CxMsg.mostrar("Campos não podem estar vazios!", this);
            return;
        }

        BancoDados.atualizarRegistro(id, nameText, phoneText, this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent telaConsulta = new Intent(TelaConsulta.this, TelaConsulta.class);
                startActivity(telaConsulta);
            }
        }, 1500);
    }

    public void delete(View v){
        String[] currentData = lista.get(currentIndex);

        String id = currentData[0];

        if(id.equals("")){
            return;
        }

        showConfirmDialog(id);
    }

    private void showConfirmDialog(String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar");
        builder.setMessage("Você deseja excluir este item?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BancoDados.deletarRegistro(id, TelaConsulta.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent telaConsulta = new Intent(TelaConsulta.this, TelaConsulta.class);
                        startActivity(telaConsulta);
                    }
                }, 1500);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(TelaConsulta.this, "Cancelado!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancelar", null);

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void proximoRegistro(View v){
        if (lista != null && currentIndex < lista.size() - 1) {
            currentIndex++;
            mostrarDados();
        } else {
            CxMsg.mostrar("Não existem mais registros!", this);
        }
    }

    public void registroAnterior(View v){
        if (lista != null && currentIndex > 0) {
            currentIndex--;
            mostrarDados();
        } else {
            CxMsg.mostrar("Não existem mais registros!", this);
        }
    }

    public void fecharTela(View v){
        Intent tela = new Intent(TelaConsulta.this, MainActivity.class);
        startActivity(tela);
    }

}