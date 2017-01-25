package com.sakhatech.parkme.model;

import java.io.Serializable;

/**
 * Created by Bheema.
 */
public class ApplicationUpdate implements Serializable{
    public int minVersion;
    public int currentVersion;
    public String newChanges;

}
