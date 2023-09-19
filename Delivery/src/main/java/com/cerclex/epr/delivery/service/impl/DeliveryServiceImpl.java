package com.cerclex.epr.delivery.service.impl;

import com.cerclex.epr.delivery.entity.*;
import com.cerclex.epr.delivery.entity.DeliveryRecyclerDetails;
import com.cerclex.epr.delivery.enums.AttachmentTypeEnum;
import com.cerclex.epr.delivery.exception.BrandAlreadyExistsException;
import com.cerclex.epr.delivery.form.*;
import com.cerclex.epr.delivery.repo.*;
import com.cerclex.epr.delivery.service.DeliveryService;
import com.cerclex.epr.delivery.utils.Constants;
import com.cerclex.epr.enums.BrandTypeEnum;
import com.cerclex.epr.enums.DeliveryStatusEnum;
import com.cerclex.epr.util.EPRWebConstants;
import com.cerclex.epr.util.SecurityConstants;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class DeliveryServiceImpl implements DeliveryService {


    private final DeliveryRepository deliveryRepository;
    private final DeliverySourceRepository deliverySourceRepository;
    private final DeliveryAllocationRepository deliveryAllocationRepository;
    private final DeliveryPickupRepository deliveryPickupRepository;
    private final DeliveryTransportRepository deliveryTransportRepository;
    private final DeliveryRecyclerRepository deliveryRecyclerRepository;
    private final DeliveryAttachmentRepository deliveryAttachmentRepository;

    @Autowired
    EntityManager em;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, DeliverySourceRepository deliverySourceRepository, DeliveryAllocationRepository deliveryAllocationRepository, DeliveryPickupRepository deliveryPickupRepository, DeliveryTransportRepository deliveryTransportRepository, DeliveryRecyclerRepository deliveryRecyclerRepository, DeliveryAttachmentRepository deliveryAttachmentRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliverySourceRepository = deliverySourceRepository;
        this.deliveryAllocationRepository = deliveryAllocationRepository;
        this.deliveryPickupRepository = deliveryPickupRepository;
        this.deliveryTransportRepository = deliveryTransportRepository;
        this.deliveryRecyclerRepository = deliveryRecyclerRepository;
        this.deliveryAttachmentRepository = deliveryAttachmentRepository;
    }

    @Override
    public String createDelivery(DeliveryCreationForm deliveryCreationForm) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> userDetails = (Map<String, String>) authentication.getDetails();
        String brandId = userDetails.get(SecurityConstants.BRAND_ID);
        //TODO: Update recycler available capacity
        DeliveryDetails deliveryDetails = new DeliveryDetails();
        if (deliveryCreationForm.getTotalQuantity() != null) deliveryDetails.setTotalQuantity(deliveryCreationForm.getTotalQuantity());
        deliveryDetails.setDeliveryDate(deliveryCreationForm.getDeliveryDate());
        deliveryDetails.setServiceProviderId(Long.parseLong(brandId));
        deliveryDetails.setStateName(deliveryCreationForm.getStateName());
        deliveryDetails.setType(deliveryCreationForm.getType());
        deliveryDetails.setCategory(deliveryCreationForm.getCategory());
        deliveryDetails.setPricePerKg(deliveryCreationForm.getPricePerKg());
        deliveryDetails.setGstRate(deliveryCreationForm.getGstRate());
        deliveryDetails.setTruckNo(deliveryCreationForm.getTruckNo());
        deliveryDetails.setDriverId(deliveryCreationForm.getDriverId());

        //Added Newly for the Support of E-Waste and Recycling type for the reports calculations
        deliveryDetails.setWasteType(deliveryCreationForm.getWasteType());
        deliveryDetails.setRecyclingType(deliveryCreationForm.getRecyclingType());

        deliveryRepository.save(deliveryDetails);
        return deliveryDetails.getId().toString();
    }

    @Override
    public DeliverySourceDetailsForm createDeliverySource(DeliverySourceDetailsForm deliverySourceDetailsForm) throws BrandAlreadyExistsException {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(DeliverySourceDetailsForm.class, DeliverySourceDetails.class).addMappings( s -> {
            s.skip(DeliverySourceDetails::setId);
        });
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> userDetails = (Map<String, String>) authentication.getDetails();
        String brandType = userDetails.get(SecurityConstants.BRAND_TYPE);
        String _brandId = userDetails.get(SecurityConstants.BRAND_ID);
        DeliverySourceDetails deliverySourceDetails = deliverySourceRepository.findByBrandId(deliverySourceDetailsForm.getBrandId());
        if (deliverySourceDetails!= null) throw new BrandAlreadyExistsException("Source with the same brand id exists");
        if (!brandType.equals(BrandTypeEnum.APPLICATION.name()) && !brandType.equals(BrandTypeEnum.SERVICE_PROVIDER.name())){
            throw new IllegalStateException("Creation of Delivery Source not allowed");
        }
        deliverySourceDetails = modelMapper.map(deliverySourceDetailsForm, DeliverySourceDetails.class);
        deliverySourceDetails.setServiceProviderId(Long.parseLong(_brandId));
        deliverySourceRepository.save(deliverySourceDetails);
        return deliverySourceDetailsForm;
    }

    @Override
    public List<DeliverySourceDetailsForm> getDeliverySource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> userDetails = (Map<String, String>) authentication.getDetails();
        String brandType = userDetails.get(SecurityConstants.BRAND_TYPE);
        String _brandId = userDetails.get(SecurityConstants.BRAND_ID);
        if (!brandType.equals(BrandTypeEnum.APPLICATION.name()) && !brandType.equals(BrandTypeEnum.SERVICE_PROVIDER.name())){
            throw new IllegalStateException("fetch of Delivery Source not allowed");
        }
        List<DeliverySourceDetailsForm> deliverySourceDetailsFormList;
        List<DeliverySourceDetails> deliverySourceDetails;
        deliverySourceDetails = deliverySourceRepository.findByServiceProviderId(Long.parseLong(_brandId));
        deliverySourceDetailsFormList = new ModelMapper().map(deliverySourceDetails, new TypeToken<List<DeliverySourceDetailsForm>>(){}.getType());
        return deliverySourceDetailsFormList;
    }

    @Override
    public DeliveryDetailsForm updateDelivery(String deliveryId, DeliveryCreationForm deliveryCreationForm){
        Optional<DeliveryDetails> deliveryDetailsOptional = deliveryRepository.findById(Long.parseLong(deliveryId));
        if (deliveryDetailsOptional.isEmpty()) throw new EntityNotFoundException("Unable to find delivery for the given id");
        DeliveryDetails deliveryDetails = deliveryDetailsOptional.get();
        deliveryDetails = deliveryCreationForm.getDeliveryDetails(deliveryDetails);

        deliveryRepository.save(deliveryDetails);
        return new ModelMapper().map(deliveryDetails,DeliveryDetailsForm.class);
    }

    @Override
    public DeliveryDetailsForm updateDeliveryHash(String deliveryId, String hash) {
        Optional<DeliveryDetails> deliveryDetailsOptional = deliveryRepository.findById(Long.parseLong(deliveryId));
        if (deliveryDetailsOptional.isEmpty()) throw new EntityNotFoundException("Unable to find delivery for the given id");
        DeliveryDetails deliveryDetails = deliveryDetailsOptional.get();
        deliveryDetails.setDeliveryHash(hash);
        deliveryRepository.save(deliveryDetails);
        return new ModelMapper().map(deliveryDetails,DeliveryDetailsForm.class);
    }

    @Override
    public DeliveryDetailsForm updatePickupDropDetails(String deliveryId, DeliveryDetailsForm deliveryDetailsForm){
        Optional<DeliveryDetails> deliveryDetailsOptional = deliveryRepository.findById(Long.valueOf(deliveryId));
        if (deliveryDetailsOptional.isEmpty()) throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
        DeliveryDetails deliveryDetails = deliveryDetailsOptional.get();

        DeliveryStatusEnum deliveryStatus = DeliveryStatusEnum.IN_PROGRESS;

        deliveryDetails.setPickupDetails(updatePickupData(deliveryDetailsForm.getPickupDetails(), deliveryDetails.getPickupDetails()));
        deliveryDetails.setTransportDetails(updateTransportData(deliveryDetailsForm.getTransportDetails(), deliveryDetails.getTransportDetails()));
        deliveryDetails.setRecycleDetails(updateRecyclerData(deliveryDetailsForm.getRecycleDetails(), deliveryDetails.getRecycleDetails()));
        deliveryRepository.save(deliveryDetails);
        return deliveryDetailsForm;
    }

    @Override
    public DeliveryDetailsForm getDeliveryById(String deliveryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> userDetails = (Map<String, String>) authentication.getDetails();
        String brandType = userDetails.get(SecurityConstants.BRAND_TYPE);
        String _brandId = userDetails.get(SecurityConstants.BRAND_ID);
        Optional<DeliveryDetails> deliveryDetails = deliveryRepository.findById(Long.parseLong(deliveryId));

        if (deliveryDetails.isEmpty()) throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
        if (brandType.equals(BrandTypeEnum.SERVICE_PROVIDER.name())){
            if (!deliveryDetails.get().getServiceProviderId().equals(Long.parseLong(_brandId))){
                throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
            }
        } else if (brandType.equals(BrandTypeEnum.RECYCLER.name())){
            if (!deliveryDetails.get().getRecyclerId().equals(Long.parseLong(_brandId))){
                throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
            }
        } else if (brandType.equals(BrandTypeEnum.TRANSPORTER.name())){
            if (!deliveryDetails.get().getTransportId().equals(Long.parseLong(_brandId))){
                throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
            }
        } else if (brandType.equals(BrandTypeEnum.SOURCE.name())){
            if (!deliveryDetails.get().getSourceId().equals(Long.parseLong(_brandId))){
                throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
            }
        }

        return new ModelMapper().map(deliveryDetails,DeliveryDetailsForm.class);
    }

    @Override
    public List<DeliveryDetailsForm> getDeliveryByUser(DeliveryDetailsForm deliveryDetailsForm) {
        return null;
    }

    @Override
    public List<DeliveryDetailsForm> getAllDeliveries(String year) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> userDetails = (Map<String, String>) authentication.getDetails();
        String brandType = userDetails.get(SecurityConstants.BRAND_TYPE);
        String _brandId = userDetails.get(SecurityConstants.BRAND_ID);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DeliveryDetails> cq = cb.createQuery(DeliveryDetails.class);

        Root<DeliveryDetails> trgt = cq.from(DeliveryDetails.class);
        List<Predicate> predicates = new ArrayList<>();

        if (!year.equals(EPRWebConstants.WEB_ALL_CONSTANT)){
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Integer.parseInt(year), Calendar.APRIL, 1);
            Date startDate = calendar.getTime();
            calendar.clear();
            calendar.set(Integer.parseInt(year) + 1, Calendar.MARCH, 31);
            Date endDate = calendar.getTime();
            predicates.add(cb.between(trgt.get("deliveryDate"), startDate,endDate));
        }

        if (brandType.equals(BrandTypeEnum.SERVICE_PROVIDER.name())){
            predicates.add(cb.equal(trgt.get("serviceProviderId"),Long.parseLong(_brandId)));
        } else if (brandType.equals(BrandTypeEnum.RECYCLER.name())){
            predicates.add(cb.equal(trgt.get("recyclerId"),Long.parseLong(_brandId)));

        } else if (brandType.equals(BrandTypeEnum.TRANSPORTER.name())){
            predicates.add(cb.equal(trgt.get("transportId"),Long.parseLong(_brandId)));

        } else if (brandType.equals(BrandTypeEnum.SOURCE.name())){
            predicates.add(cb.equal(trgt.get("sourceId"),Long.parseLong(_brandId)));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<DeliveryDetails> deliveries = em.createQuery(cq).getResultList();
        return new ModelMapper().map(deliveries,new TypeToken<List<DeliveryDetailsForm>>(){}.getType());
    }

    @Override
    public String deleteDelivery(String deliveryId) {
        Optional<DeliveryDetails> deliveryDetailsOptional = deliveryRepository.findById(Long.parseLong(deliveryId));
        if (deliveryDetailsOptional.isEmpty()) throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
        DeliveryDetails deliveryDetails = deliveryDetailsOptional.get();
        deliveryDetails.setActive(false);
        deliveryRepository.save(deliveryDetails);
        return "Delivery Deleted Successfully";
    }

    @Override
    public String allocateDelivery(String deliveryId, String poNumber, String brandId, Double quantity) {
        DeliveryAllocationDetails deliveryAllocationDetail = new DeliveryAllocationDetails();
        boolean allocationExist = false;
        Optional<DeliveryDetails> deliveryDetailsOptional = deliveryRepository.findById(Long.parseLong(deliveryId));
        if (deliveryDetailsOptional.isEmpty()) throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
        DeliveryDetails deliveryDetails = deliveryDetailsOptional.get();
        //TODO: Get the list and mandatory documents and check if all are present and change it to pending, then uncomment the below line
        //if (deliveryDetails.getDeliveryStatus().equals(DeliveryStatusEnum.IN_PROGRESS))throw new IllegalStateException("Incomplete Delivery cannot be allocated, please complete all documents before allocation");
        List<DeliveryAllocationDetails> deliveryAllocationDetails = deliveryDetails.getDeliveryAllocationDetails();
        Double allocatedQuantity = (double) 0;
        if (!deliveryAllocationDetails.isEmpty()){
            for (DeliveryAllocationDetails allocations: deliveryAllocationDetails) {
                if (allocations.getPoNumber().equals(poNumber) && allocations.getBrandId().equals(Long.parseLong(brandId))){
                    allocations.setStatus(DeliveryStatusEnum.PENDING);
                    deliveryAllocationDetail = allocations;
                    allocationExist = true;
                }else allocatedQuantity += allocations.getAllocatedQuantity();
            }
        }
        if ((allocatedQuantity + quantity) > deliveryDetails.getTotalQuantity()) throw new IllegalStateException("Allocation cannot exceed total quantity availability");

        //TODO: check if poNumber exists with the Target system
        deliveryAllocationDetail.setAllocatedQuantity(quantity);
        if (!allocationExist) {
            deliveryAllocationDetail.setBrandId(Long.parseLong(brandId));
            deliveryAllocationDetail.setPoNumber(poNumber);
            deliveryAllocationDetail.setStatus(DeliveryStatusEnum.PENDING);
            deliveryAllocationDetail.setStateName(deliveryDetails.getStateName());
            deliveryAllocationDetail.setPrice(deliveryDetails.getPricePerKg());
            deliveryAllocationDetail.setCategory(deliveryDetails.getCategory());
            deliveryAllocationDetail.setDeliveryDetails(deliveryDetails);
        }
        deliveryAllocationRepository.save(deliveryAllocationDetail);
        return "Delivery Allocated Successfully";
    }

    @Override
    public List<BrandInvoiceForm> getDeliveryAllocation(String deliveryId) {
        Optional<DeliveryDetails> deliveryDetails = deliveryRepository.findById(Long.valueOf(deliveryId));
        List<DeliveryAllocationDetails> deliveryAllocationDetailsList = deliveryAllocationRepository.findByDeliveryDetails(deliveryDetails.get());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(DeliveryAllocationDetails.class, BrandInvoiceForm.class)
                .addMappings(m-> m.map(src-> src.getDeliveryDetails().getId(),BrandInvoiceForm::setDeliveryId));
        List<BrandInvoiceForm> brandInvoiceFormList = new ArrayList<>();
        for (DeliveryAllocationDetails deliveryAllocationDetails : deliveryAllocationDetailsList){
            brandInvoiceFormList.add(modelMapper.map(deliveryAllocationDetails,BrandInvoiceForm.class));
        }
        return brandInvoiceFormList;
    }

    @Override
    public InvoiceDeliveryDetailsForm getDeliveryForInvoice(String deliveryId) {
        Optional<DeliveryDetails> deliveryDetails = deliveryRepository.findById(Long.parseLong(deliveryId));
        if (deliveryDetails.isEmpty()) throw new EntityNotFoundException(Constants.DELIVERY_NOT_FOUND);
        return new ModelMapper().map(deliveryDetails,InvoiceDeliveryDetailsForm.class);
    }

    private DeliveryRecyclerDetails updateRecyclerData(DeliveryRecyclerForm deliveryRecyclerForm, DeliveryRecyclerDetails deliveryRecyclerDetails) {
        if (deliveryRecyclerForm==null || !deliveryRecyclerForm.isUpdated()) return deliveryRecyclerDetails;
        if (deliveryRecyclerDetails == null) deliveryRecyclerDetails = new DeliveryRecyclerDetails();
        deliveryRecyclerDetails.setReportDate(deliveryRecyclerForm.getReportDate());
        deliveryRecyclerDetails.setTotalWeight(deliveryRecyclerForm.getTotalWeight());
        deliveryRecyclerDetails.setTareWeight(deliveryRecyclerForm.getTareWeight());

        // Changed from Source to Static names
/*        DeliverySourceDetails deliverySourceDetails = deliverySourceRepository.findByBrandId(deliveryRecyclerForm.getSourceId().getBrandId());
        if (deliverySourceDetails==null) throw new EntityNotFoundException("Unable to find the given recycler brand");
        if (!deliverySourceDetails.getBrandTypeEnum().equals(BrandTypeEnum.RECYCLER)) throw new IllegalArgumentException("Given brand for recycler is not a recycler");
        deliveryRecyclerDetails.setSourceId(deliverySourceDetails);*/
        //Static
        deliveryRecyclerDetails.setProviderName(deliveryRecyclerForm.getProviderName());
        deliveryRecyclerDetails.setProviderAddress(deliveryRecyclerForm.getProviderAddress());
        deliveryRecyclerDetails.setProviderLocation(deliveryRecyclerForm.getProviderLocation());

        deliveryRecyclerDetails.setDropProof(
                mapAttachmentData(
                        deliveryRecyclerForm.getDropProof(),
                        AttachmentTypeEnum.DROP_PROOF,
                        deliveryRecyclerDetails.getDropProof()
                )
        );
        deliveryRecyclerDetails.setWeighBridgeDetails(
                mapAttachmentData(deliveryRecyclerForm.getWeighBridgeDetails(),
                        AttachmentTypeEnum.WEIGH_BRIDGE,
                        deliveryRecyclerDetails.getWeighBridgeDetails()
                )
        );
        deliveryRecyclerDetails.setRecyclerCertificate(
                mapAttachmentData(
                        deliveryRecyclerForm.getRecyclerCertificate(),
                        AttachmentTypeEnum.RECYCLER_CERTIFICATE,
                        deliveryRecyclerDetails.getRecyclerCertificate()
                )
        );
        deliveryRecyclerDetails.setCoProcessingCertificate(
                mapAttachmentData(
                        deliveryRecyclerForm.getCoProcessingCertificate(),
                        AttachmentTypeEnum.CO_PROCESSING_CERTIFICATE,
                        deliveryRecyclerDetails.getCoProcessingCertificate()
                )
        );
        deliveryRecyclerDetails.setRecyclerAcknowledgement(
                mapAttachmentData(
                        deliveryRecyclerForm.getRecyclerAcknowledgement(),
                        AttachmentTypeEnum.RECYCLER_ACKNOWLEDGEMENT,
                        deliveryRecyclerDetails.getRecyclerAcknowledgement()
                )
        );
        deliveryRecyclerDetails.setOthers1(
                mapAttachmentData(
                        deliveryRecyclerForm.getOthers1(),
                        AttachmentTypeEnum.OTHERS,
                        deliveryRecyclerDetails.getOthers1()
                )
        );
        deliveryRecyclerDetails.setOthers2(
                mapAttachmentData(
                        deliveryRecyclerForm.getOthers2(),
                        AttachmentTypeEnum.OTHERS,
                        deliveryRecyclerDetails.getOthers2()
                )
        );
        return deliveryRecyclerRepository.save(deliveryRecyclerDetails);
    }

    private DeliveryTransportDetails updateTransportData(DeliveryTransportForm transportDetailsForm, DeliveryTransportDetails deliveryTransportDetails) {
        if (transportDetailsForm == null || !transportDetailsForm.isUpdated()) return deliveryTransportDetails;
        if (deliveryTransportDetails == null) deliveryTransportDetails = new DeliveryTransportDetails();
        deliveryTransportDetails.setStartDate(transportDetailsForm.getStartDate());
        deliveryTransportDetails.setEndDate(transportDetailsForm.getEndDate());

/*      DeliverySourceDetails deliverySourceDetails = deliverySourceRepository.findByBrandId(transportDetailsForm.getSourceId().getBrandId());
        if (deliverySourceDetails==null) throw new EntityNotFoundException("Unable to find the given transport brand");
        if (!deliverySourceDetails.getBrandTypeEnum().equals(BrandTypeEnum.TRANSPORTER)) throw new IllegalArgumentException("Given brand for transport is not a transporter");
        deliveryTransportDetails.setSourceId(deliverySourceDetails);*/

        deliveryTransportDetails.setProviderName(transportDetailsForm.getProviderName());
        deliveryTransportDetails.setProviderAddress(transportDetailsForm.getProviderAddress());
        deliveryTransportDetails.setProviderLocation(transportDetailsForm.getProviderLocation());

        deliveryTransportDetails.setDeliveryChallan(
                mapAttachmentData(
                        transportDetailsForm.getDeliveryChallan(),
                        AttachmentTypeEnum.DELIVERY_CHALLAN,
                        deliveryTransportDetails.getDeliveryChallan()
                )
        );
        deliveryTransportDetails.setEwayBill(
                mapAttachmentData(
                        transportDetailsForm.getEwayBill(),
                        AttachmentTypeEnum.EWAY_BILL,
                        deliveryTransportDetails.getEwayBill()
                )
        );
        deliveryTransportDetails.setVehiclePhoto1(
                mapAttachmentData(
                        transportDetailsForm.getVehiclePhoto1(),
                        AttachmentTypeEnum.VEHICLE_PHOTO,
                        deliveryTransportDetails.getVehiclePhoto1()
                )
        );
        deliveryTransportDetails.setVehiclePhoto2(
                mapAttachmentData(
                        transportDetailsForm.getVehiclePhoto2(),
                        AttachmentTypeEnum.VEHICLE_PHOTO,
                        deliveryTransportDetails.getVehiclePhoto2()
                )
        );
        deliveryTransportDetails.setVehiclePhoto3(
                mapAttachmentData(
                        transportDetailsForm.getVehiclePhoto3(),
                        AttachmentTypeEnum.VEHICLE_PHOTO,
                        deliveryTransportDetails.getVehiclePhoto3()
                )
        );
        deliveryTransportDetails.setVehiclePhoto4(
                mapAttachmentData(
                        transportDetailsForm.getVehiclePhoto4(),
                        AttachmentTypeEnum.VEHICLE_PHOTO,
                        deliveryTransportDetails.getVehiclePhoto4()
                )
        );
        deliveryTransportDetails.setOthers1(
                mapAttachmentData(
                        transportDetailsForm.getOthers1(),
                        AttachmentTypeEnum.OTHERS,
                        deliveryTransportDetails.getOthers1()
                )
        );
        deliveryTransportDetails.setOthers2(
                mapAttachmentData(
                        transportDetailsForm.getOthers2(),
                        AttachmentTypeEnum.OTHERS,
                        deliveryTransportDetails.getOthers2()
                )
        );

        return deliveryTransportRepository.save(deliveryTransportDetails);
    }

    private DeliveryPickupDetails updatePickupData(DeliveryPickupForm pickupDetailsForm, DeliveryPickupDetails deliveryPickupDetails) {
        if (pickupDetailsForm==null || !pickupDetailsForm.isUpdated()) return deliveryPickupDetails;
        if (deliveryPickupDetails == null) deliveryPickupDetails = new DeliveryPickupDetails();
        deliveryPickupDetails.setReportedDate(pickupDetailsForm.getReportedDate());

/*        DeliverySourceDetails deliverySourceDetails = deliverySourceRepository.findByBrandId(pickupDetailsForm.getSourceId().getBrandId());
        if (deliverySourceDetails==null) throw new EntityNotFoundException("Unable to find the given source brand");
        if (!deliverySourceDetails.getBrandTypeEnum().equals(BrandTypeEnum.SOURCE)) throw new IllegalArgumentException("Given brand for source is not a source brand");
        deliveryPickupDetails.setSourceId(deliverySourceDetails);*/

        deliveryPickupDetails.setProviderName(pickupDetailsForm.getProviderName());
        deliveryPickupDetails.setProviderAddress(pickupDetailsForm.getProviderAddress());
        deliveryPickupDetails.setProviderLocation(pickupDetailsForm.getProviderLocation());
        deliveryPickupDetails.setTotalWeight(pickupDetailsForm.getTotalWeight());
        deliveryPickupDetails.setTareWeight(pickupDetailsForm.getTareWeight());
        deliveryPickupDetails.setReportedDate(pickupDetailsForm.getReportedDate());

        deliveryPickupDetails.setWeighBridgeDetails(
                mapAttachmentData(
                        pickupDetailsForm.getWeighBridgeDetails(),
                        AttachmentTypeEnum.WEIGH_BRIDGE,
                        deliveryPickupDetails.getWeighBridgeDetails()
                )
        );
        deliveryPickupDetails.setSourceInvoice(
                mapAttachmentData(
                        pickupDetailsForm.getSourceInvoice(),
                        AttachmentTypeEnum.SOURCE_INVOICE,
                        deliveryPickupDetails.getSourceInvoice()
                )
        );
        deliveryPickupDetails.setLorryReceipt(
                mapAttachmentData(
                        pickupDetailsForm.getLorryReceipt(),
                        AttachmentTypeEnum.LORRY_RECEIPT,
                        deliveryPickupDetails.getLorryReceipt()
                )
        );
        deliveryPickupDetails.setOthers1(
                mapAttachmentData(
                        pickupDetailsForm.getOthers1(),
                        AttachmentTypeEnum.OTHERS,
                        deliveryPickupDetails.getOthers1()
                )
        );
        deliveryPickupDetails.setOthers2(
                mapAttachmentData(
                        pickupDetailsForm.getOthers2(),
                        AttachmentTypeEnum.OTHERS,
                        deliveryPickupDetails.getOthers2()
                )
        );
        deliveryPickupDetails.setOthers3(
                mapAttachmentData(
                        pickupDetailsForm.getOthers3(),
                        AttachmentTypeEnum.OTHERS,
                        deliveryPickupDetails.getOthers3()
                )
        );


        return deliveryPickupRepository.save(deliveryPickupDetails);

    }

    public AttachmentDetails mapAttachmentData(AttachmentDetailsForm data, AttachmentTypeEnum type, AttachmentDetails currentAttachmentDetails){
        if (data!=null && data.isModified()){
            if (data.getAttachmentType() != type) throw new IllegalArgumentException("Attachment of type "+data.getAttachmentType()+" cannot be uploaded to "+ type);
            AttachmentDetails attachmentDetails = new AttachmentDetails();
            attachmentDetails.setAttachmentName(data.getAttachmentName());
            attachmentDetails.setAttachmentType(type);
            if (data.getAttachmentCategory()!=null)attachmentDetails.setAttachmentCategory(data.getAttachmentCategory());
            if (data.getAttachmentNo()!=null) attachmentDetails.setAttachmentNo(data.getAttachmentNo());
            attachmentDetails.setContentType(data.getContentType());
            attachmentDetails.setAttachmentDataId(data.getAttachmentDataId());
            attachmentDetails.setUploadDate(data.getUploadDate());
            return deliveryAttachmentRepository.save(attachmentDetails);
        }else return currentAttachmentDetails;
    }

    public DeliveryStatusEnum calculateDeliveryStatus(DeliveryDetails deliveryDetails){
        return DeliveryStatusEnum.PENDING;
    }
}
