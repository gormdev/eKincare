package com.oneclick.ekincare.vo;

/**
 * Created by RaviTejaN on 17-05-2016.
 */
public class SuggestGetSet {

    String id,name,package_type;
    public SuggestGetSet(String id, String name, String package_type){
        this.setId(id);
        this.setName(name);
        this.setPackage_type(package_type);
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }
}
