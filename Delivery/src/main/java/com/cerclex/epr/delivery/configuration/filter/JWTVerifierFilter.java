package com.cerclex.epr.delivery.configuration.filter;

import com.cerclex.epr.delivery.utils.Utils;
import com.cerclex.epr.util.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

public class JWTVerifierFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = httpServletRequest.getHeader("Authorization");
//        if(!Utils.validString(authHeader) || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//            return;
//        }

        String token = httpServletRequest.getHeader("auth-token");
        if(!Utils.validString(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String publicKey = SecurityConstants.PUB_KEY.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");;

        KeyFactory kf = null;
        RSAPublicKey pubKey = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        Claims user = Jwts.parserBuilder()
                    .setSigningKey(pubKey)
                    .build()
                    .parseClaimsJws(token).getBody();

        logHeaders(httpServletRequest);
        String username=httpServletRequest.getHeader("username");
        Map<String, String> userDetails = new HashMap<>();
        userDetails = (Map<String, String>) user.get("userDetails");
        String authoritiesStr = httpServletRequest.getHeader("authorities");
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        if(Utils.validString(authoritiesStr)) {
            simpleGrantedAuthorities= Arrays.stream(authoritiesStr.split(",")).distinct()
                    .filter(Utils::validString).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        }
        //Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
        Authentication authentication = new EPRAuthenticationToken(username, null, simpleGrantedAuthorities,userDetails,true,username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    private void logHeaders(HttpServletRequest httpServletRequest) {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String header=headerNames.nextElement();
            logger.info(String.format("Header: %s --- Value: %s", header, httpServletRequest.getHeader(header)));

        }
    }
}
