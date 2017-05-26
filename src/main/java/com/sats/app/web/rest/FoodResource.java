package com.sats.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sats.app.domain.Food;

import com.sats.app.repository.FoodRepository;
import com.sats.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Food.
 */
@RestController
@RequestMapping("/api")
public class FoodResource {

    private final Logger log = LoggerFactory.getLogger(FoodResource.class);

    private static final String ENTITY_NAME = "food";
        
    private final FoodRepository foodRepository;

    public FoodResource(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    /**
     * POST  /foods : Create a new food.
     *
     * @param food the food to create
     * @return the ResponseEntity with status 201 (Created) and with body the new food, or with status 400 (Bad Request) if the food has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/foods")
    @Timed
    public ResponseEntity<Food> createFood(@RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to save Food : {}", food);
        if (food.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new food cannot already have an ID")).body(null);
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.created(new URI("/api/foods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /foods : Updates an existing food.
     *
     * @param food the food to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated food,
     * or with status 400 (Bad Request) if the food is not valid,
     * or with status 500 (Internal Server Error) if the food couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/foods")
    @Timed
    public ResponseEntity<Food> updateFood(@RequestBody Food food) throws URISyntaxException {
        log.debug("REST request to update Food : {}", food);
        if (food.getId() == null) {
            return createFood(food);
        }
        Food result = foodRepository.save(food);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, food.getId().toString()))
            .body(result);
    }

    /**
     * GET  /foods : get all the foods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of foods in body
     */
    @GetMapping("/foods")
    @Timed
    public List<Food> getAllFoods() {
        log.debug("REST request to get all Foods");
        List<Food> foods = foodRepository.findAll();
        return foods;
    }

    /**
     * GET  /foods/:id : get the "id" food.
     *
     * @param id the id of the food to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the food, or with status 404 (Not Found)
     */
    @GetMapping("/foods/{id}")
    @Timed
    public ResponseEntity<Food> getFood(@PathVariable Long id) {
        log.debug("REST request to get Food : {}", id);
        Food food = foodRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(food));
    }

    /**
     * DELETE  /foods/:id : delete the "id" food.
     *
     * @param id the id of the food to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/foods/{id}")
    @Timed
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        log.debug("REST request to delete Food : {}", id);
        foodRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
