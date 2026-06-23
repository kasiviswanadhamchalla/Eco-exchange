package com.industry_connect.marketplace_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_org_id", nullable = false)
    private Long sellerOrgId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private MaterialCategory category;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double quantity;

    @Column(name = "available_quantity", nullable = false)
    private Double availableQuantity;

    @Column(nullable = false)
    private String unit;

    @Column(name = "price_per_unit", nullable = false)
    private Double pricePerUnit;

    private String location;

    @Column(nullable = false)
    private String status; // DRAFT, ACTIVE, SOLD, DELETED, INACTIVE

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ListingImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QualityCertificate> certificates = new ArrayList<>();

    public Listing() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerOrgId() {
        return sellerOrgId;
    }

    public void setSellerOrgId(Long sellerOrgId) {
        this.sellerOrgId = sellerOrgId;
    }

    public MaterialCategory getCategory() {
        return category;
    }

    public void setCategory(MaterialCategory category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ListingImage> getImages() {
        return images;
    }

    public void setImages(List<ListingImage> images) {
        this.images = images;
    }

    public List<QualityCertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<QualityCertificate> certificates) {
        this.certificates = certificates;
    }

    // Helper methods to maintain bidirectional relationship
    public void addImage(ListingImage image) {
        images.add(image);
        image.setListing(this);
    }

    public void removeImage(ListingImage image) {
        images.remove(image);
        image.setListing(null);
    }

    public void addCertificate(QualityCertificate certificate) {
        certificates.add(certificate);
        certificate.setListing(this);
    }

    public void removeCertificate(QualityCertificate certificate) {
        certificates.remove(certificate);
        certificate.setListing(null);
    }
}
