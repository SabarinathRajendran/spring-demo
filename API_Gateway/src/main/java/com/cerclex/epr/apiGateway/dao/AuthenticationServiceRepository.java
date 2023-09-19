package com.cerclex.epr.apiGateway.dao;

import com.cerclex.epr.apiGateway.model.ConnValidationResponse;
import com.cerclex.epr.util.SecurityConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class AuthenticationServiceRepository {

	@GetMapping(value = "/api/v1/validateConnection", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ConnValidationResponse validateConnection(String authToken) {

		try {

			WebClient client = WebClient.builder().baseUrl("lb://authentication-service").build();
			ConnValidationResponse response = client.get().uri("/api/v1/validateConnection")
					.header(SecurityConstants.HEADER, authToken)
					.retrieve().toEntity(ConnValidationResponse.class).flux().blockFirst().getBody();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//throw new EPRGatewayException("Gateway Error", HttpStatus.BAD_GATEWAY);
		throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gateway Error");
	}

}
