package com.denucieaqui.android.gui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.denucieaqui.android.R;
import com.denucieaqui.android.dominio.Arquivo;
import com.denucieaqui.android.dominio.Denuncia;
import com.denucieaqui.android.negocio.DenunciaNegocio;
import com.denucieaqui.android.service.DenunciaService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class DenunciaDescricaoActivity extends AppCompatActivity {

    private AlertDialog alerta;
    private Toolbar toolbar;
    private ProgressDialog load;

    private LinearLayout preview_upload_images;
    private Bitmap bitmap;
    private ArrayList<Arquivo> midiaArray = new ArrayList<Arquivo>();


    private static final int ID_IMAGEM_GET = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_descricao);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Util.buildToolbarHomeButton(DenunciaDescricaoActivity.this, toolbar);

        preview_upload_images = (LinearLayout) findViewById(R.id.image_previews);

        /*FloatingActionButton fabGaleria = (FloatingActionButton) findViewById(R.id.fabGaleria);
        fabGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i = 0;

        switch (requestCode) {
            case ID_IMAGEM_GET:

                bitmap = ImagePicker.getImageFromResult(this, resultCode, data);

                ImageView imageView = new ImageView(this);
                imageView.setId(i + 1);
                imageView.setPadding(2, 2, 2, 2);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(220, 220);
                imageView.setLayoutParams(params);
                preview_upload_images.addView(imageView);

                //transformar em Base64 para Upload
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    Arquivo arquivo = new Arquivo();
                    arquivo.setNomeArquivo(DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString());
                    arquivo.setDados(byteArrayOutputStream.toByteArray());
                    arquivo.setFormatoArquivo(".JPEG");
//                    String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//                    Log.d("DescricaoActivityBase64", encodedImage);
//                    base64Array.add(encodedImage);
                    midiaArray.add(arquivo);
                } catch (Exception e) {
                    Toast.makeText(this, "Algo de errado aconteceu ao codificar as fotos", Toast.LENGTH_SHORT);
                }


                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void removeALLImages(View view) {
        preview_upload_images.removeAllViewsInLayout();
        midiaArray.clear();
    }

    public void chamaTelaDialogIdentificacao(final View view) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja se identificar?");
        builder.setMessage("Caso deseje se identificar, você receberá um email assim que a denúncia for atendida pelo órgão responsável informando o que foi feito para solucionar o problema.");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chamaTelaDadosPessoais(view);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chamaTelaDialogAgradecer(view);
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    public void chamaTelaDialogAgradecer(View view) {
        EditText descricaoDenuncia = (EditText) findViewById(R.id.textoDescricaoDenuncia);
        if (descricaoDenuncia.getText().toString().isEmpty()) {
            Toast.makeText(DenunciaDescricaoActivity.this, "Preencha a descrição.", Toast.LENGTH_SHORT).show();
        } else {
            Denuncia denuncia = DenunciaNegocio.getDenuncia();
            denuncia.setListaImagens(midiaArray);
            denuncia.setDescricao(descricaoDenuncia.getText().toString());
            DenunciaNegocio.setDenuncia(denuncia);
            new DenunciaPost().execute();
        }
    }

    public void chamaTelaDadosPessoais(View view) {

        EditText descricaoDenuncia = (EditText) findViewById(R.id.textoDescricaoDenuncia);
        if (descricaoDenuncia.getText().toString().isEmpty()) {
            Toast.makeText(DenunciaDescricaoActivity.this, "Preencha a descrição.", Toast.LENGTH_SHORT).show();
        } else {
            Denuncia denuncia = DenunciaNegocio.getDenuncia();
            //denuncia.setListaImagens(midiaArray);
            denuncia.setDescricao(descricaoDenuncia.getText().toString());
            DenunciaNegocio.setDenuncia(denuncia);
            Intent intent = new Intent(DenunciaDescricaoActivity.this, DadosPessoaisActivity.class);
            this.startActivity(intent);
        }
    }


    public void galeria(View view) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(chooseImageIntent, ID_IMAGEM_GET);

        } else {
            Toast.makeText(this, "Autorização de uso da Memória e Câmera necessária", Toast.LENGTH_LONG);
        }

    }

    private class DenunciaPost extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask", "Exibindo ProgressDialog na tela. Thread: " + Thread.currentThread().getName());
            load = ProgressDialog.show(DenunciaDescricaoActivity.this, "Por favor, aguarde ...",
                    "Inserindo Denuncia ...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("AsyncTask", "Requisição ao webservice. Thread: " + Thread.currentThread().getName());
            uploadImagens(midiaArray);
            return DenunciaService.insertDenuncia(DenunciaNegocio.getDenuncia());
            
        }



        @Override
        protected void onPostExecute(Boolean insert) {
            Log.i("AsyncTask", "Tirando ProgressDialog da tela. Thread: " + Thread.currentThread().getName());
            load.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(DenunciaDescricaoActivity.this);
            builder.setTitle("Obrigado!");
            builder.setMessage("Parabéns pela denúncia! Ela será encaminhada aos órgãos responsáveis.");

            builder.setPositiveButton("Valeu!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(DenunciaDescricaoActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
            });

            alerta = builder.create();
            alerta.show();
        }

        private void uploadImagens(ArrayList<Arquivo> arquivos) {
            for(int i = 0; i > arquivos.size() ; i++){
                if (DenunciaService.insertArquivo(arquivos.get(i))){
                    Log.i("UploadFoto","UPLOAD Foto: " + i +"OK");
                }else{
                    Log.i("UploadFoto","UPLOAD Foto: " + i +"NAO foi Possivel Realizar Upload");
                }
            }
        }
    }
}
