package com.industry_connect.marketplace_service.config;

import com.industry_connect.marketplace_service.entity.MaterialCategory;
import com.industry_connect.marketplace_service.repository.MaterialCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MaterialCategoryRepository categoryRepository;

    public DataInitializer(MaterialCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            List<MaterialCategory> categories = Arrays.asList(
                    new MaterialCategory("Steel Scrap", "Steel scrap materials from industrial waste"),
                    new MaterialCategory("Fly Ash", "Fly ash byproduct from coal combustion"),
                    new MaterialCategory("Plastic Scrap", "Post-industrial and post-consumer plastic scrap"),
                    new MaterialCategory("Textile Waste", "Textile and fabric waste from garment manufacturers"),
                    new MaterialCategory("Wood Waste", "Wood and timber scrap from packaging and construction"),
                    new MaterialCategory("Glass Waste", "Glass cullet and industrial glass waste")
            );
            categoryRepository.saveAll(categories);
        }
    }
}
