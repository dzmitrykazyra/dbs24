/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.application.core.locale.NLS;
import org.dbs24.entity.PackageDetails;

@Data
@EqualsAndHashCode
public class PackageDetailsInfo {
    private String packageName;
    private Long actualDate;
    private String companyName;
    private String appName;
    private String contactInfo;
    
    public void assign(PackageDetails packageDetails) {
        setActualDate(NLS.localDateTime2long(packageDetails.getActualDate()));
        setAppName(packageDetails.getAppName());
        setCompanyName(packageDetails.getCompanyName());
        setContactInfo(packageDetails.getContactInfo());
        setPackageName(packageDetails.getPackageName());
    }
}
