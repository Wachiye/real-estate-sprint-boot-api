package com.egerton.realeaste.dao;

import com.egerton.realeaste.models.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo  extends JpaRepository<Image, Long>{
    Optional<Image> findByName( String name);
}
