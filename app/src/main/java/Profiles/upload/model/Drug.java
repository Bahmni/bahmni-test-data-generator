package Profiles.upload.model;

public class Drug {

    private String name;
    private String form;
    private String uuid;

    public Drug(String name, String form, String uuid) {
        this.name = name;
        this.form = form;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public String getForm() {
        return form;
    }

    public String getUuid() {
        return uuid;
    }

}
