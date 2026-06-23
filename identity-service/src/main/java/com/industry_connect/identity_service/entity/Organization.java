package com.industry_connect.identity_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "industry_type")
    private String industryType;

    @Column(name = "gst_number", unique = true)
    private String gstNumber;

    @Column(name = "verification_status")
    private String verificationStatus;

    private String city;
    private String state;
    private String country;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Organization() {}

    public Organization(String name, String industryType, String gstNumber, String verificationStatus,
                      String city, String state, String country) {
        this.name = name;
        this.industryType = industryType;
        this.gstNumber = gstNumber;
        this.verificationStatus = verificationStatus;
        this.city = city;
        this.state = state;
        this.country = country;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIndustryType() { return industryType; }
    public void setIndustryType(String industryType) { this.industryType = industryType; }
    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
