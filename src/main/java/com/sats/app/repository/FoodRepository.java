package com.sats.app.repository;

import com.sats.app.domain.Food;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Food entity.
 */
@SuppressWarnings("unused")
public interface FoodRepository extends JpaRepository<Food,Long> {

}
