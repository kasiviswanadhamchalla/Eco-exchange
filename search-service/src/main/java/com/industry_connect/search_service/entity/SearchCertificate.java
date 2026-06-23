package com.industry_connect.search_service.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class SearchCertificate {
    private String certificateName;
    private String certificateUrl;

    public SearchCertificate() {}

    public SearchCertificate(String certificateName, String certificateUrl) {
        this.certificateName = certificateName;
        this.certificateUrl = certificateUrl;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }
}
