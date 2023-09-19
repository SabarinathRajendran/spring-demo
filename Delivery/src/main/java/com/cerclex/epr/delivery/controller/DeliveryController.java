package com.cerclex.epr.delivery.controller;

import com.cerclex.epr.delivery.exception.BrandAlreadyExistsException;
import com.cerclex.epr.delivery.form.*;
import com.cerclex.epr.delivery.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PreAuthorize("hasAuthority('DELIVERY_CREATE')")
    @PostMapping("/create")
    public ResponseEntity<String> createDelivery(@RequestBody @Valid DeliveryCreationForm deliveryCreationForm){
        try{
            return new ResponseEntity<>(deliveryService.createDelivery(deliveryCreationForm), HttpStatus.OK);
        }catch (Exception e){
            log.error("Unable to create delivery",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create the delivery");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_CREATE')")
    @PostMapping("/createSource")
    public ResponseEntity<DeliverySourceDetailsForm> createDeliverySource(@Valid DeliverySourceDetailsForm deliverySourceDetailsForm){
        try{
            return new ResponseEntity<>(deliveryService.createDeliverySource(deliverySourceDetailsForm), HttpStatus.OK);
        }catch (BrandAlreadyExistsException e){
            log.error("Unable to create source",e);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }catch (Exception e){
            log.error("Unable to create source",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create the delivery source");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_READ')")
    @GetMapping("/getSource")
    public ResponseEntity<List<DeliverySourceDetailsForm>> getDeliverySource(){
        try{
            return new ResponseEntity<>(deliveryService.getDeliverySource(),HttpStatus.OK);
        }catch (Exception e){
            log.error("Unable to get Source : ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get delivery source");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_READ')")
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetailsForm> getDeliveryById(@PathVariable("deliveryId") @Length(max = 20) @NotBlank String deliveryId){
        try{
            return new ResponseEntity<>(deliveryService.getDeliveryById(deliveryId), HttpStatus.OK);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get the delivery");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_READ')")
    @GetMapping("/myDeliveries")
    public List<DeliveryDetailsForm> getDeliveryByUser(DeliveryDetailsForm deliveryDetailsForm){
        return null;
    }

    @PreAuthorize("hasAuthority('DELIVERY_READ')")
    @GetMapping("/allDeliveries")
    public ResponseEntity<List<DeliveryDetailsForm>> getAllDeliveries(@RequestParam String year){
        try{
            return new ResponseEntity<>(deliveryService.getAllDeliveries(year), HttpStatus.OK);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get all the delivery");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_UPDATE')")
    @PostMapping("/{deliveryId}/update")
    public ResponseEntity<DeliveryDetailsForm> updateDelivery(@PathVariable("deliveryId") String deliveryId,@RequestBody @Valid DeliveryCreationForm deliveryCreationForm){
        try{
            return new ResponseEntity<>(deliveryService.updateDelivery(deliveryId, deliveryCreationForm), HttpStatus.OK);
        }catch (EntityNotFoundException e){
            log.error("Unable to update ptd of delivery",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to update ptd of delivery",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get all the delivery");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_UPDATE')")
    @PostMapping("/{deliveryId}/updateHash")
    public ResponseEntity<DeliveryDetailsForm> updateDeliveryHash(@PathVariable("deliveryId") String deliveryId,@RequestParam String transcationHash){
        try{
            return new ResponseEntity<>(deliveryService.updateDeliveryHash(deliveryId, transcationHash), HttpStatus.OK);
        }catch (EntityNotFoundException e){
            log.error("Unable to update ptd of delivery",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to update ptd of delivery",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get all the delivery");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_UPDATE')")
    @PostMapping("/{deliveryId}/updatePTDDetails")
    public ResponseEntity<DeliveryDetailsForm> updatePTDDetails(@PathVariable("deliveryId") String deliveryId,@RequestBody @Valid DeliveryDetailsForm deliveryDetailsForm){
        try{
            return new ResponseEntity<>(deliveryService.updatePickupDropDetails(deliveryId, deliveryDetailsForm), HttpStatus.OK);
        }catch (EntityNotFoundException | IllegalArgumentException e){
            log.error("Unable to update ptd of delivery",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to update ptd of delivery",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update ptd of delivery");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_DELETE')")
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<String> deleteDelivery(@PathVariable("deliveryId") String deliveryId){
        try{
            return new ResponseEntity<>(deliveryService.deleteDelivery(deliveryId), HttpStatus.OK);
        }catch (EntityNotFoundException e){
            log.error("Unable to delete delivery",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to delete delivery",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete delivery");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_ALLOCATE')")
    @PostMapping("/{deliveryId}/allocate/{brandId}/{poNumber}")
    public ResponseEntity<String> allocateDelivery(@PathVariable("deliveryId") String deliveryId, @PathVariable("poNumber") String poNumber, @PathVariable("brandId") String brandId, @RequestParam @NotBlank Double quantity){
        try{
            return new ResponseEntity<>(deliveryService.allocateDelivery(deliveryId, poNumber,brandId, quantity), HttpStatus.OK);
        }catch (EntityNotFoundException | IllegalStateException e){
            log.error("Unable to allocate delivery",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to allocate delivery",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to allocate delivery");
        }
    }

    @PreAuthorize("hasAuthority('DELIVERY_ALLOCATE')")
    @Deprecated(forRemoval = true, since = "Allocation moved to target service for easy maintenance")
    @GetMapping("/{deliveryId}/getAllocation")
    public ResponseEntity<List<BrandInvoiceForm>> getAllocationDetails(@PathVariable("deliveryId")String deliveryId){
        try{
            return new ResponseEntity<>(deliveryService.getDeliveryAllocation(deliveryId), HttpStatus.OK);
        }catch (EntityNotFoundException e){
            log.error("Unable to get delivery allocations",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to get delivery allocations",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get delivery allocations");
        }
    }
}
