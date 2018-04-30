package com.denucieaqui.android.negocio;

import com.denucieaqui.android.dominio.Denuncia;

/**
 * Created by Gabriele on 04/07/2017.
 */

public class DenunciaNegocio {

    private static Denuncia denuncia = new Denuncia();

    public static Denuncia getDenuncia() {
        return denuncia;
    }

    public static void setDenuncia(Denuncia denuncia) {
        DenunciaNegocio.denuncia = denuncia;
    }

}
