package com.sakhatech.parkme.model;

import java.io.Serializable;

/**
 * Created by Bheema.
 * Company Techjini
 */
public class MetaData implements Serializable {
    private  String action;
    private ApplicationUpdate version;

    public ApplicationUpdate getVersion() {
        return version;
    }

    public void setVersion(ApplicationUpdate version) {
        this.version = version;
    }
}
