package com.blockbrain.propmgmnt.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.blockbrain.propmgmnt.domain.Apartment;
import com.blockbrain.propmgmnt.repository.ApartmentRepository;
import com.blockbrain.propmgmnt.web.rest.errors.BadRequestAlertException;
import com.blockbrain.propmgmnt.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Apartment.
 */
@RestController
@RequestMapping("/api")
public class ApartmentResource {

    private final Logger log = LoggerFactory.getLogger(ApartmentResource.class);

    private static final String ENTITY_NAME = "apartment";

    private final ApartmentRepository apartmentRepository;

    public ApartmentResource(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    /**
     * POST  /apartments : Create a new apartment.
     *
     * @param apartment the apartment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apartment, or with status 400 (Bad Request) if the apartment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/apartments")
    @Timed
    public ResponseEntity<Apartment> createApartment(@Valid @RequestBody Apartment apartment) throws URISyntaxException {
        log.debug("REST request to save Apartment : {}", apartment);
        if (apartment.getId() != null) {
            throw new BadRequestAlertException("A new apartment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apartment result = apartmentRepository.save(apartment);
        return ResponseEntity.created(new URI("/api/apartments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apartments : Updates an existing apartment.
     *
     * @param apartment the apartment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apartment,
     * or with status 400 (Bad Request) if the apartment is not valid,
     * or with status 500 (Internal Server Error) if the apartment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/apartments")
    @Timed
    public ResponseEntity<Apartment> updateApartment(@Valid @RequestBody Apartment apartment) throws URISyntaxException {
        log.debug("REST request to update Apartment : {}", apartment);
        if (apartment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Apartment result = apartmentRepository.save(apartment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, apartment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apartments : get all the apartments.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of apartments in body
     */
    @GetMapping("/apartments")
    @Timed
    public List<Apartment> getAllApartments(@RequestParam(required = false) String filter) {
        if ("agreement-is-null".equals(filter)) {
            log.debug("REST request to get all Apartments where agreement is null");
            return StreamSupport
                .stream(apartmentRepository.findAll().spliterator(), false)
                .filter(apartment -> apartment.getAgreement() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Apartments");
        return apartmentRepository.findAll();
    }

    /**
     * GET  /apartments/:id : get the "id" apartment.
     *
     * @param id the id of the apartment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apartment, or with status 404 (Not Found)
     */
    @GetMapping("/apartments/{id}")
    @Timed
    public ResponseEntity<Apartment> getApartment(@PathVariable Long id) {
        log.debug("REST request to get Apartment : {}", id);
        Optional<Apartment> apartment = apartmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apartment);
    }

    /**
     * DELETE  /apartments/:id : delete the "id" apartment.
     *
     * @param id the id of the apartment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/apartments/{id}")
    @Timed
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        log.debug("REST request to delete Apartment : {}", id);

        apartmentRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
