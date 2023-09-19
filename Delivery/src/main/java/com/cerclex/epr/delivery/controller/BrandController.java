package com.cerclex.epr.delivery.controller;

import com.cerclex.epr.delivery.form.BrandInvoiceDetailedForm;
import com.cerclex.epr.delivery.form.BrandInvoiceForm;
import com.cerclex.epr.delivery.service.BrandInvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@Deprecated(forRemoval = true, since = "Has been moved to the Target Service")
@RequestMapping("/delivery/brandInvoice")
public class BrandController {

    private final BrandInvoiceService brandInvoiceService;

    public BrandController(BrandInvoiceService brandInvoiceService) {
        this.brandInvoiceService = brandInvoiceService;
    }

    //@GetMapping("/{invoiceId}")
    public ResponseEntity<BrandInvoiceDetailedForm> getBrandInvoiceById(@PathVariable("invoiceId") @NotBlank @Length(max = 15) String invoiceId){
        try{
            return new ResponseEntity<>(brandInvoiceService.getBrandInvoiceById(invoiceId), HttpStatus.OK);
        }catch (EntityNotFoundException | IllegalStateException e){
            log.error("Unable to get Invoice",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to get Invoice",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get Invoice");
        }
    }

    //@GetMapping("/brand/{brandId}")
    public ResponseEntity<List<BrandInvoiceForm>> getInvoiceForBrand(@PathVariable("brandId") @NotBlank @Length(max = 15) String brandId){
        try{
            return new ResponseEntity<>(brandInvoiceService.getInvoiceForBrand(brandId), HttpStatus.OK);
        }catch (EntityNotFoundException | IllegalStateException e){
            log.error("Unable to get Invoices for brand",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to get Invoices for brand",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get Invoices for brand");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<BrandInvoiceForm>> getAllInvoice(){
        try{
            return new ResponseEntity<>(brandInvoiceService.getAllInvoice(), HttpStatus.OK);
        }catch (EntityNotFoundException | IllegalStateException e){
            log.error("Unable to get all Invoice",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to get all Invoice",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get all Invoice");
        }
    }

    @PostMapping("/{invoiceId}/accept")
    public ResponseEntity<String> acceptInvoice(@PathVariable("invoiceId") @NotBlank @Length(max = 15) String invoiceId){
        try{
            return new ResponseEntity<>(brandInvoiceService.acceptInvoice(invoiceId), HttpStatus.OK);
        }catch (EntityNotFoundException | IllegalStateException e){
            log.error("Unable to accept Invoice",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to accept accept",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to accept Invoice");
        }
    }

    @PostMapping("/{invoiceId}/reject")
    public ResponseEntity<String> rejectInvoice(@PathVariable("invoiceId") @NotBlank @Length(max = 15) String invoiceId,@RequestParam @NotBlank @Max(250) String rejectMessage){
        try{
            return new ResponseEntity<>(brandInvoiceService.rejectInvoice(invoiceId, rejectMessage), HttpStatus.OK);
        }catch (EntityNotFoundException | IllegalStateException e){
            log.error("Unable to reject Invoice",e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            log.error("Unable to reject Invoice",e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to reject Invoice");
        }
    }
}
