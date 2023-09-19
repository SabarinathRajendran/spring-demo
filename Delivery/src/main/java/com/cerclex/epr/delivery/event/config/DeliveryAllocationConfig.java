package com.cerclex.epr.delivery.event.config;

import com.cerclex.epr.delivery.event.service.DeliveryAllocationServiceImpl;
import com.cerclex.epr.enums.InvoiceRequestType;
import com.cerclex.epr.events.model.delivery.DeliveryAllocationEvent;
import com.cerclex.epr.events.model.invoice.InvoiceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class DeliveryAllocationConfig {

    @Autowired
    DeliveryAllocationServiceImpl deliveryAllocationService;

    @Bean
    public Function<Flux<InvoiceEvent>, Flux<DeliveryAllocationEvent>> allocationProcess() {
        return flux -> flux.flatMap(this::processAllocation);
    }

    private Mono<DeliveryAllocationEvent> processAllocation(InvoiceEvent event){
        if(event.getStatus().equals(InvoiceRequestType.ALLOCATE)){
            return Mono.fromSupplier(() -> this.deliveryAllocationService.createAllocation(event));
        }else{
            return Mono.fromRunnable(() -> this.deliveryAllocationService.cancelAllocation(event));
        }
    }
}
