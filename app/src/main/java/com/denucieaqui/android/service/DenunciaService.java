package com.denucieaqui.android.service;

import com.denucieaqui.android.dominio.Arquivo;
import com.denucieaqui.android.dominio.Denuncia;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Gabriele on 04/07/2017.
 */

public class DenunciaService {
    private static String DENUNCIA_URL = "denuncia/";
    private static String ARQUIVO_URL = "arquivo/";

    public static boolean insertDenuncia(Denuncia denuncia) {
        Gson gson = new Gson();
        String json = gson.toJson(denuncia);

        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
//        RequestBody formBody = new FormBody.Builder()
//                .add("denunciaObject", json)
//                .build();

        Response response = null;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ConfiguracaoUtil.URL_ACESSO + DENUNCIA_URL)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Accept", "application/json")
                .build();

        try {
            response = client.newCall(request).execute();

            if (response.networkResponse().code() == HttpURLConnection.HTTP_OK) {

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean insertArquivo(Arquivo arquivo) {
        Gson gson = new Gson();
        String json = gson.toJson(arquivo);

        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        Response response = null;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ConfiguracaoUtil.URL_ACESSO + ARQUIVO_URL)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Accept", "application/json")
                .build();

        try {
            response = client.newCall(request).execute();

            if (response.networkResponse().code() == HttpURLConnection.HTTP_OK) {

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
