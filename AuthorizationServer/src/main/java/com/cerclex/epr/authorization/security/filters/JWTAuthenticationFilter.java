package com.cerclex.epr.authorization.security.filters;

import com.cerclex.epr.authorization.dtos.ApplicationUsers;
import com.cerclex.epr.authorization.dtos.User;
import com.cerclex.epr.util.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cerclex.epr.authorization.model.ConnValidationResponse;
import com.cerclex.epr.authorization.model.JwtAuthenticationModel;
import com.cerclex.epr.authorization.model.redis.TokensEntity;
import com.cerclex.epr.authorization.services.redis.TokensRedisService;
import com.cerclex.epr.authorization.utils.Utilities;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private ObjectMapper mapper=new ObjectMapper();

    private final TokensRedisService tokensRedisService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            JwtAuthenticationModel authModel = mapper.readValue(request.getInputStream(), JwtAuthenticationModel.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(authModel.getUsername(), authModel.getPassword());
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String privateKey = SecurityConstants.KEY.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");

        KeyFactory kf = null;
        PrivateKey privKey = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            // error : java.security.spec.InvalidKeySpecException: java.security.InvalidKeyException: IOException : algid parse error, not a sequence
            // for algorithm based error view this answer : https://stackoverflow.com/questions/6559272/algid-parse-error-not-a-sequence
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            privKey = kf.generatePrivate(keySpecPKCS8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        ApplicationUsers applicationUsers = (ApplicationUsers) authResult.getPrincipal();

        User user = applicationUsers.getUserDetails();

        Map<String, String> userDetails = new HashMap<>();

        userDetails.put("brandId", user.getBrand().getBrandId().toString());
        userDetails.put("brandName",user.getBrand().getBrandName());
        userDetails.put("brandType", user.getBrand().getBrandType().name());
        userDetails.put("userType",user.getUserType().name());

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
//                .claim("principal", authResult.getPrincipal())
                .claim("userDetails",userDetails)
                .setIssuedAt(new Date())
                .setIssuer(SecurityConstants.ISSUER)
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.UTC)))
                .signWith(privKey, SignatureAlgorithm.RS512)
                .compact();

        log.info(token);
        TokensEntity tokensEntity = TokensEntity.builder().id(Utilities.generateUuid()).authenticationToken(token)
                        .username(authResult.getName())
                        .createdBy("SYSTEM").createdOn(LocalDateTime.now())
                        .modifiedBy("SYSTEM").modifiedOn(LocalDateTime.now())
                        .build();
        tokensEntity = tokensRedisService.save(tokensEntity);
        response.addHeader(SecurityConstants.HEADER, String.format("Bearer %s", tokensEntity.getId()));
        response.addHeader("Expiration", String.valueOf(2*24*60*60));

        ConnValidationResponse respModel = ConnValidationResponse.builder().status(HttpStatus.OK.name()).token(String.format("Bearer %s", tokensEntity.getId())).methodType(HttpMethod.GET.name()).isAuthenticated(true).build();
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(respModel));
    }
}
