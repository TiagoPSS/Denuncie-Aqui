package com.denucieaqui.android.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class Util {

    /**
     * Mensagem exibida em Toast, no canto inferior da tela, por um curto tempo
     *
     * @param activity
     * @param texto
     */
    public static void showMsgToastShort(Activity activity, String texto) {
        Toast.makeText(activity, texto, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensagem exibida em Toast, no canto inferior da tela, por um longo tempo
     *
     * @param activity
     * @param texto
     */
    public static void showMsgToastLong(Activity activity, String texto) {
        Toast.makeText(activity, texto, Toast.LENGTH_LONG).show();
    }

    /**
     * Mensagem em AlertDialog apenas com botão de confirmação
     *
     * @param activity
     * @param titulo
     * @param texto
     */
    public static void showMsgAlertOk(Activity activity, String titulo, String texto) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(texto);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    /**
     * Mensagem em AlertDialog com botões de confirmação e cancelamento
     *
     * @param activity
     * @param titulo
     * @param texto
     */
    public static void showMsgAlertPosNeg(Activity activity, String titulo, String texto){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(texto);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    /**
     * Metodo geral para troca de telas (activities)
     *
     * @param activityAtual
     * @param activitySeguinte
     */
    public static void trocarTela(Activity activityAtual, Class activitySeguinte){
        Intent intent = new Intent(activityAtual, activitySeguinte);
        activityAtual.startActivity(intent);
    }

    /**
     * Método geral para habilitar o botão de voltar na toolbar de cada tela (activity)
     * @param activity
     * @param toolbar
     */
    public static void buildToolbarHomeButton(final AppCompatActivity activity, Toolbar toolbar){
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

}
