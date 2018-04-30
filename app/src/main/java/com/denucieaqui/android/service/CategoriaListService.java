package com.denucieaqui.android.service;

import com.denucieaqui.android.dominio.Categoria;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Gabriele on 12/07/2017.
 */

public class CategoriaListService {

    private static String CATEGORIA_URL = "denuncia/list/categoria";

    public static List<Categoria> carregarCategoria() {
        Response response = null;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ConfiguracaoUtil.URL_ACESSO + CATEGORIA_URL)
                .build();

        try {
            response = client.newCall(request).execute();

            if (response.networkResponse().code() == HttpURLConnection.HTTP_OK) {
                String json = response.body().string();

                Gson gson = new Gson();

                //Log.i("WS", json);

                TypeToken<List<Categoria>> token = new TypeToken<List<Categoria>>(){};
                List<Categoria> result = gson.fromJson(json, token.getType());

                if (result != null && result.size() > 0) {
                    return result;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
