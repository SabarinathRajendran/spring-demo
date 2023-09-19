package com.cerclex.epr.apiGateway.service;

import com.cerclex.epr.apiGateway.dao.AuthenticationServiceRepository;
import com.cerclex.epr.apiGateway.model.ConnValidationResponse;
import com.cerclex.epr.apiGateway.utils.Utilities;
import com.cerclex.epr.util.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ValidateTokenService {

//    @Autowired
//    private AuthenticationServiceProxy authenticationServiceProxy;

    @Autowired
    private AuthenticationServiceRepository authenticationServiceRepository;


    public ConnValidationResponse validateAuthenticationToken(String bearerToken) {

        if(Utilities.isTokenValid(bearerToken)) {
            String token = bearerToken.replace(SecurityConstants.PREFIX, "");

            try {
//                ResponseEntity<ConnValidationResponse> responseEntity = authenticationServiceProxy.validateConnection(token);
//                if(responseEntity.getStatusCode().is2xxSuccessful()) {
//                    return responseEntity.getBody();
//                } else if(responseEntity.getStatusCode().is4xxClientError()) {
//                    int errorCode = responseEntity.getStatusCode().value();
//                    if(errorCode==401) {
//                        throw new BusinessException("", HttpStatus.UNAUTHORIZED, "Unauthorized");
//                    } else if(errorCode==403) {
//                        throw new BusinessException("", HttpStatus.FORBIDDEN, "Forbidden");
//                    }
//                }

                return authenticationServiceRepository.validateConnection(bearerToken);

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

    }

}
