package com.denucieaqui.android.gui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.denucieaqui.android.R;
import com.denucieaqui.android.dominio.Denuncia;
import com.denucieaqui.android.negocio.DenunciaNegocio;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class DenunciaEnderecoActivity extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private AlertDialog alerta;
    private LatLng localizacaoAtual;
    private Toolbar toolbar;
    private boolean mPermissionDenied = false;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia_endereco);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Util.buildToolbarHomeButton(DenunciaEnderecoActivity.this, toolbar);

        //setando o mapa no fragment da tela
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

    }

    //Quando o mapa tiver pronto, isso vai acontecer
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        verificarGPS();
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            //Toast.makeText(this, "Conceda a permissão ao app", Toast.LENGTH_LONG).show();
            Log.e("Erro", "Localização do usuário desativada.");
        }
        setarLocalizacao();
    }

    private void verificarGPS() {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Verifica se o GPS está ativado
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Caso não esteja ativo abre um novo diálogo com as configurações para ativar
        if (!enabled) {
            chamaTelaDialogAtivarLocalizacao();
        } else {
            verificarPermissaoLocalizacao();
        }
    }

    //Cria um listener para tratar um evento disparado toda as vezes que a localização é alterada
    private void verificarPermissaoLocalizacao() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } //else if (map != null) {}
    }


    public void setarLocalizacao() {
        // Access to the location has been granted to the app.
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                map.clear();
                localizacaoAtual = new LatLng(location.getLatitude(), location.getLongitude());
                getCityByLocation(location);
                //receberLatLongParaNomeEndereco(localizacaoAtual);
                map.addMarker(new MarkerOptions().position(localizacaoAtual).anchor(0.5f, 1.0f).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_app)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacaoAtual, 15.0f));
            }
        });
    }


    public void chamaTelaDialogAtivarLocalizacao() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Localização desativada");
        builder.setMessage("Por favor, ative sua localização. Deseja ativá-la?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                ;
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chamaTelaDialogAtivarLocalizacao();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "Botão de localização clicado", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void getCityByLocation(Location location) {
        //obtendo coordenadas
        double latPoint = location.getLatitude();
        double lngPoint = location.getLongitude();
        latitude = String.valueOf( location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        //Toast.makeText(this, (int) latPoint, Toast.LENGTH_LONG).show();

        //Classe que fornece a localização da cidade
        Geocoder geocoder = new Geocoder(this.getApplicationContext());
        List myLocation = null;
        //List<Address> locationName = null;

        try {
            //Obtendo os dados do endereço
            myLocation = geocoder.getFromLocation(latPoint, lngPoint, 1);

            /*EditText txtEndereco = (EditText) findViewById(R.id.textoEndereco);
            txtEndereco.getText();
            locationName = geocoder.getFromLocationName(txtEndereco.toString(), 1);*/

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (myLocation != null && myLocation.size() > 0) {
            Address a = (Address) myLocation.get(0);
            //Address b = locationName.get(0);
            //Pronto! Você tem o nome da cidade!
            String city = a.getLocality();
            String street = a.getAddressLine(0);

            EditText endereco = (EditText) findViewById(R.id.textoEndereco);
            endereco.setText(street + " , " + city);

            //Toast.makeText(this, city + " , " + street, Toast.LENGTH_LONG).show();
            //Toast.makeText(this, b.getLatitude() + " , " + b.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Log.d("geolocation", "Endereço não localizado");
        }
    }

    public void chamaTelaDenunciaDescricao(View view) {
        Denuncia denuncia = DenunciaNegocio.getDenuncia();
        EditText endereco = (EditText) findViewById(R.id.textoEndereco);
        EditText referencia = (EditText) findViewById(R.id.textoReferencia);
        denuncia.setEndereco(endereco.getText().toString());
        denuncia.setEnderecoReferencia(referencia.getText().toString());
        denuncia.setLatitude(latitude);
        denuncia.setLongitude(longitude);
        DenunciaNegocio.setDenuncia(denuncia);
        Intent intent = new Intent(DenunciaEnderecoActivity.this, DenunciaDescricaoActivity.class);
        this.startActivity(intent);
    }


}
