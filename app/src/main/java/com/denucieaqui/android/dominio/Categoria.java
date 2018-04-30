package com.denucieaqui.android.dominio;

import java.io.Serializable;

/**
 * Created by Gabriele on 12/07/2017.
 */

public class Categoria implements Serializable {

    private String label;
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
