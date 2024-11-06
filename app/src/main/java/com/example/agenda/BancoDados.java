package com.example.agenda;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContextWrapper;
import android.widget.Toast;

public class BancoDados {

    static SQLiteDatabase db = null;

    public static void abrirBanco(Activity act){
        try{
            ContextWrapper cw = new ContextWrapper(act);
            db = cw.openOrCreateDatabase("bancoAgenda", MODE_PRIVATE, null);
        } catch(Exception ex){
            CxMsg.mostrar("Erro ao abrir ou criar banco de dados!", act);
        }
    }

    public static void fecharDB(){
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public static void abrirOuCriarTabela(Activity act){
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS contatos (id INTEGER PRIMARY KEY, nome TEXT, fone TEXT);");
        } catch(Exception ex){
            CxMsg.mostrar("Erro ao criar tabela!", act);
        }
    }

    public static void inserirRegistro(String nome, String fone, Activity act){
        abrirBanco(act);

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("fone", fone);

        try {
            long result = db.insert("contatos", null, values);
            if (result == -1) {
                CxMsg.mostrar("Erro ao inserir registro!", act);
            } else {
                CxMsg.mostrar("Registro inserido com sucesso!", act);
            }
        } catch (Exception ex) {
            CxMsg.mostrar("Erro ao inserir registro, tente novamente!", act);
        } finally {
            fecharDB();
        }
    }

    public static void atualizarRegistro(String id, String nome, String fone, Activity act){
        abrirBanco(act);

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("fone", fone);

        String whereClause = "id = ?";
        String[] whereArgs = new String[]{id};

        try{
            int result = db.update("contatos", values, whereClause, whereArgs);
            if(result > 0){
                Toast.makeText(act, "Registro atualizado!", Toast.LENGTH_SHORT).show();
            } else {
                CxMsg.mostrar("Nenhum registro foi atualizado!", act);
            }
        } catch(Exception ex){
            CxMsg.mostrar("Erro ao atualizar registro!", act);
        } finally {
            fecharDB();
        }
    }

    public static void deletarRegistro(String id, Activity act){
        abrirBanco(act);

        String whereClause = "id = ?";
        String[] whereArgs = new String[]{id};

        try{
            int result = db.delete("contatos", whereClause, whereArgs);
            if(result > 0){
                Toast.makeText(act, "Registro deletado!", Toast.LENGTH_SHORT).show();
            } else {
                CxMsg.mostrar("Nenhum registro foi deletado!", act);
            }
        } catch(Exception ex){
            CxMsg.mostrar("Erro ao deletar registro!", act);
        } finally {
            fecharDB();
        }
    }

}
