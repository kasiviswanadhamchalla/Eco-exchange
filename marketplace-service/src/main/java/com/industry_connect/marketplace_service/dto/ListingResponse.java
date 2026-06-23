package com.industry_connect.marketplace_service.dto;

import com.industry_connect.marketplace_service.entity.Listing;
import com.industry_connect.marketplace_service.entity.ListingImage;
import com.industry_connect.marketplace_service.entity.QualityCertificate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ListingResponse {
    private Long id;
    private Long sellerOrgId;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String description;
    private Double quantity;
    private Double availableQuantity;
    private String unit;
    private Double pricePerUnit;
    private String location;
    private String status;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private List<CertificateDto> certificates;

    public ListingResponse() {}

    public ListingResponse(Listing listing) {
        this.id = listing.getId();
        this.sellerOrgId = listing.getSellerOrgId();
        if (listing.getCategory() != null) {
            this.categoryId = listing.getCategory().getId();
            this.categoryName = listing.getCategory().getName();
        }
        this.title = listing.getTitle();
        this.description = listing.getDescription();
        this.quantity = listing.getQuantity();
        this.availableQuantity = listing.getAvailableQuantity();
        this.unit = listing.getUnit();
        this.pricePerUnit = listing.getPricePerUnit();
        this.location = listing.getLocation();
        this.status = listing.getStatus();
        this.createdAt = listing.getCreatedAt();
        
        if (listing.getImages() != null) {
            this.imageUrls = listing.getImages().stream()
                    .map(ListingImage::getImageUrl)
                    .collect(Collectors.toList());
        }
        
        if (listing.getCertificates() != null) {
            this.certificates = listing.getCertificates().stream()
                    .map(c -> new CertificateDto(c.getCertificateName(), c.getCertificateUrl()))
                    .collect(Collectors.toList());
        }
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<CertificateDto> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateDto> certificates) {
        this.certificates = certificates;
    }
}
