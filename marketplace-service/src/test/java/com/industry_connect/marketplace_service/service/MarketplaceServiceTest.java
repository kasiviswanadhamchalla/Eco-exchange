package com.industry_connect.marketplace_service.service;

import com.industry_connect.marketplace_service.dto.*;
import com.industry_connect.marketplace_service.entity.*;
import com.industry_connect.marketplace_service.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketplaceServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private MaterialCategoryRepository categoryRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private KafkaEventPublisher eventPublisher;

    @InjectMocks
    private MarketplaceService marketplaceService;

    private MaterialCategory steelCategory;
    private Listing activeListing;

    @BeforeEach
    void setUp() {
        steelCategory = new MaterialCategory("Steel Scrap", "Steel scrap");
        steelCategory.setId(1L);

        activeListing = new Listing();
        activeListing.setId(10L);
        activeListing.setSellerOrgId(100L);
        activeListing.setCategory(steelCategory);
        activeListing.setTitle("Steel Pipes");
        activeListing.setQuantity(20.0);
        activeListing.setAvailableQuantity(20.0);
        activeListing.setUnit("TONS");
        activeListing.setPricePerUnit(500.0);
        activeListing.setStatus("ACTIVE");
    }

    @Test
    void testCreateListing() {
        ListingRequest request = new ListingRequest();
        request.setCategoryId(1L);
        request.setTitle("Steel Pipes");
        request.setQuantity(20.0);
        request.setUnit("TONS");
        request.setPricePerUnit(500.0);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(steelCategory));
        when(listingRepository.save(any(Listing.class))).thenAnswer(invocation -> {
            Listing l = invocation.getArgument(0);
            l.setId(10L);
            return l;
        });

        ListingResponse response = marketplaceService.createListing(request, 100L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("DRAFT", response.getStatus());
        assertEquals(100L, response.getSellerOrgId());
        verify(listingRepository, times(1)).save(any(Listing.class));
    }

    @Test
    void testPlaceOffer() {
        OfferRequest request = new OfferRequest();
        request.setQuantity(5.0);
        request.setOfferedPrice(450.0);

        when(listingRepository.findById(10L)).thenReturn(Optional.of(activeListing));
        when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> {
            Offer o = invocation.getArgument(0);
            o.setId(200L);
            return o;
        });

        OfferResponse response = marketplaceService.placeOffer(10L, request, 200L);

        assertNotNull(response);
        assertEquals(200L, response.getId());
        assertEquals(10L, response.getListingId());
        assertEquals(200L, response.getBuyerOrgId());
        assertEquals("PENDING", response.getStatus());
        verify(eventPublisher, times(1)).publishOfferPlaced(eq(200L), eq(10L), eq(200L), eq(450.0));
    }

    @Test
    void testAcceptOffer() {
        Offer offer = new Offer();
        offer.setId(200L);
        offer.setListing(activeListing);
        offer.setBuyerOrgId(200L);
        offer.setQuantity(5.0);
        offer.setOfferedPrice(450.0);
        offer.setStatus("PENDING");

        when(offerRepository.findById(200L)).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(listingRepository.save(any(Listing.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(offerRepository.findByListingId(10L)).thenReturn(new ArrayList<>());

        OfferResponse response = marketplaceService.acceptOffer(200L, 100L);

        assertNotNull(response);
        assertEquals("ACCEPTED", response.getStatus());
        assertEquals(15.0, activeListing.getAvailableQuantity());
        verify(eventPublisher, times(1)).publishOfferAccepted(eq(200L), eq(10L));
    }
}
