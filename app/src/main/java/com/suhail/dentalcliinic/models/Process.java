package com.suhail.dentalcliinic.models;

import java.io.Serializable;

public class Process implements Serializable {
   private String name;
   private String section;

    public Process() {
    }

    public Process(String name, String section) {
        this.name = name;
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
