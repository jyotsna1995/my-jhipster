package com.sats.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sats.app.domain.Language;

import com.sats.app.repository.LanguageRepository;
import com.sats.app.web.rest.util.HeaderUtil;
import com.sats.app.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Language.
 */
@RestController
@RequestMapping("/api")
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(LanguageResource.class);

    private static final String ENTITY_NAME = "language";
        
    private final LanguageRepository languageRepository;

    public LanguageResource(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    /**
     * POST  /languages : Create a new language.
     *
     * @param language the language to create
     * @return the ResponseEntity with status 201 (Created) and with body the new language, or with status 400 (Bad Request) if the language has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/languages")
    @Timed
    public ResponseEntity<Language> createLanguage(@RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to save Language : {}", language);
        if (language.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new language cannot already have an ID")).body(null);
        }
        Language result = languageRepository.save(language);
        return ResponseEntity.created(new URI("/api/languages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /languages : Updates an existing language.
     *
     * @param language the language to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated language,
     * or with status 400 (Bad Request) if the language is not valid,
     * or with status 500 (Internal Server Error) if the language couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/languages")
    @Timed
    public ResponseEntity<Language> updateLanguage(@RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to update Language : {}", language);
        if (language.getId() == null) {
            return createLanguage(language);
        }
        Language result = languageRepository.save(language);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, language.getId().toString()))
            .body(result);
    }

    /**
     * GET  /languages : get all the languages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of languages in body
     */
    @GetMapping("/languages")
    @Timed
    public ResponseEntity<List<Language>> getAllLanguages(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Languages");
        Page<Language> page = languageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/languages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /languages/:id : get the "id" language.
     *
     * @param id the id of the language to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the language, or with status 404 (Not Found)
     */
    @GetMapping("/languages/{id}")
    @Timed
    public ResponseEntity<Language> getLanguage(@PathVariable Long id) {
        log.debug("REST request to get Language : {}", id);
        Language language = languageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(language));
    }

    /**
     * DELETE  /languages/:id : delete the "id" language.
     *
     * @param id the id of the language to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/languages/{id}")
    @Timed
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        log.debug("REST request to delete Language : {}", id);
        languageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
