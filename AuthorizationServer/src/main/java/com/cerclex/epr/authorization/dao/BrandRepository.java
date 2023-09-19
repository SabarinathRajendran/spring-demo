package com.cerclex.epr.authorization.dao;

import com.cerclex.epr.authorization.dtos.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand,String> {
    Brand findByBrandName(String brandName);
}
