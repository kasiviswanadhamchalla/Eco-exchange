package com.industry_connect.marketplace_service.dto;

public class CertificateDto {
    private String certificateName;
    private String certificateUrl;

    public CertificateDto() {}

    public CertificateDto(String certificateName, String certificateUrl) {
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
