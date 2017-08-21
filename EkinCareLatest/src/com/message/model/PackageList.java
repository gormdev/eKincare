package com.message.model;

import com.oneclick.ekincare.vo.PackageDataSet;

import java.util.List;

/**
 * Created by Ajay on 24-12-2016.
 */

public class PackageList {
    private List<PackageItem> packages;

    public List<PackageItem> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageItem> packages) {
        this.packages = packages;
    }
}
