package com.denucieaqui.android.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Gabriele on 04/07/2017.
 */

public class Denuncia {
    private long id;
    private String tipoDenuncia;
    private String categoriaDenuncia;
    private String descricao;
    private String latitude;
    private String longitude;
    private String endereco;
    private String enderecoReferencia;
    private String nomeDenunciante;
    private String telefonePrincipal;
    private String telefoneAuxiliar;
    private String emailDenunciante;
    private Date dataCriacao;
    private List<Arquivo> listaImagens;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNomeDenunciante() {
        return nomeDenunciante;
    }

    public void setNomeDenunciante(String nomeDenunciante) {
        this.nomeDenunciante = nomeDenunciante;
    }

    public String getEmailDenunciante() {
        return emailDenunciante;
    }

    public void setEmailDenunciante(String emailDenunciante) {
        this.emailDenunciante = emailDenunciante;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getTipoDenuncia() {
        return tipoDenuncia;
    }

    public void setTipoDenuncia(String tipoDenuncia) {
        this.tipoDenuncia = tipoDenuncia;
    }
    public String getCategoriaDenuncia() {
        return categoriaDenuncia;
    }

    public void setCategoriaDenuncia(String categoriaDenuncia) {
        this.categoriaDenuncia = categoriaDenuncia;
    }

    public List<Arquivo> getListaImagens() {
        return listaImagens;
    }

    public void setListaImagens(List<Arquivo> listaImagens) {
        this.listaImagens = listaImagens;
    }

    public String getEnderecoReferencia() {
        return enderecoReferencia;
    }

    public void setEnderecoReferencia(String enderecoReferencia) {
        this.enderecoReferencia = enderecoReferencia;
    }

    public String getTelefonePrincipal() {
        return telefonePrincipal;
    }

    public void setTelefonePrincipal(String telefonePrincipal) {
        this.telefonePrincipal = telefonePrincipal;
    }

    public String getTelefoneAuxiliar() {
        return telefoneAuxiliar;
    }

    public void setTelefoneAuxiliar(String telefoneAuxiliar) {
        this.telefoneAuxiliar = telefoneAuxiliar;
    }
}
