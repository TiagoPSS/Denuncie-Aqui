package com.denucieaqui.android.gui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.denucieaqui.android.R;
import com.denucieaqui.android.dominio.Categoria;
import com.denucieaqui.android.dominio.Denuncia;
import com.denucieaqui.android.dominio.Pessoa;
import com.denucieaqui.android.negocio.DenunciaNegocio;
import com.denucieaqui.android.service.CategoriaListService;
import com.denucieaqui.android.service.DenunciaService;

import java.util.ArrayList;
import java.util.List;


public class DadosPessoaisActivity extends AppCompatActivity {

    private AlertDialog alerta;
    private Toolbar toolbar;
    private Pessoa pessoa;
    private ProgressDialog load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_pessoais);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Util.buildToolbarHomeButton(DadosPessoaisActivity.this, toolbar);
    }

    @Override
    protected void onResume() {
        if (getIntent().getBooleanExtra("EXIT", false)) {
            Intent intent = new Intent(DadosPessoaisActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        super.onResume();
    }

    public void chamaDialogAgradecerIdentificacao(final View view) {
        EditText nome = (EditText) findViewById(R.id.textoNome);
        EditText telefone1 = (EditText) findViewById(R.id.textoTelefone1);
        EditText telefone2 = (EditText) findViewById(R.id.textoTelefone2);
        EditText email = (EditText) findViewById(R.id.textoEmail);


        if (nome.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                || (telefone1.getText().toString().isEmpty() && telefone2.getText().toString().isEmpty())) {
            Toast.makeText(DadosPessoaisActivity.this, "Preencha o nome, email e ao menos um dos telefones.", Toast.LENGTH_SHORT).show();
        } else {
            Denuncia denuncia = DenunciaNegocio.getDenuncia();
            denuncia.setNomeDenunciante(nome.getText().toString().trim());
            denuncia.setTelefonePrincipal(telefone1.getText().toString().trim());
            denuncia.setTelefoneAuxiliar(telefone2.getText().toString().trim());
            denuncia.setEmailDenunciante(email.getText().toString().trim());
            DenunciaNegocio.setDenuncia(denuncia);
            new DenunciaPost().execute();
        }

    }

    private class DenunciaPost extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask", "Exibindo ProgressDialog na tela. Thread: " + Thread.currentThread().getName());
            load = ProgressDialog.show(DadosPessoaisActivity.this, "Por favor, aguarde ...",
                    "Inserindo Denuncia ...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("AsyncTask", "Requisição ao webservice. Thread: " + Thread.currentThread().getName());
            return DenunciaService.insertDenuncia(DenunciaNegocio.getDenuncia());
        }

        @Override
        protected void onPostExecute(Boolean insert) {
            Log.i("AsyncTask", "Tirando ProgressDialog da tela. Thread: " + Thread.currentThread().getName());
            load.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(DadosPessoaisActivity.this);
            builder.setTitle("Obrigado!");
            builder.setMessage("Parabéns pela denúncia! Fique atento ao email, pois assim que houver alguma resposta, nós te avisaremos. Deseja fazer outra denúncia?");

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(DadosPessoaisActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DadosPessoaisActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
            });

            alerta = builder.create();
            alerta.show();
        }
    }


}
