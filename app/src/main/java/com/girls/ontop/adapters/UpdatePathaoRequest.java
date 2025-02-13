package com.girls.ontop.adapters;

public class UpdatePathaoRequest {
    private int id;
    private String pathaocity;
    private String pathaoarea;
    private String pathaozone;

    public UpdatePathaoRequest(int id, String pathaocity, String pathaoarea, String pathaozone) {
        this.id = id;
        this.pathaocity = pathaocity;
        this.pathaoarea = pathaoarea;
        this.pathaozone = pathaozone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPathaocity() {
        return pathaocity;
    }

    public void setPathaocity(String pathaocity) {
        this.pathaocity = pathaocity;
    }

    public String getPathaoarea() {
        return pathaoarea;
    }

    public void setPathaoarea(String pathaoarea) {
        this.pathaoarea = pathaoarea;
    }

    public String getPathaozone() {
        return pathaozone;
    }

    public void setPathaozone(String pathaozone) {
        this.pathaozone = pathaozone;
    }
}
