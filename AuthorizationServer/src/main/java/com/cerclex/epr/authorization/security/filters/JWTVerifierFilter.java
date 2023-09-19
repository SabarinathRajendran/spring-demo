package com.cerclex.epr.authorization.security.filters;

import com.cerclex.epr.authorization.model.redis.TokensEntity;
import com.cerclex.epr.authorization.services.redis.TokensRedisService;
import com.cerclex.epr.authorization.utils.Utilities;
import com.cerclex.epr.util.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JWTVerifierFilter extends OncePerRequestFilter {

    private final TokensRedisService tokensRedisService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = httpServletRequest.getHeader(SecurityConstants.HEADER);
        if(!(Utilities.validString(bearerToken) && bearerToken.startsWith(SecurityConstants.PREFIX))) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String authToken = bearerToken.replace(SecurityConstants.PREFIX, "");

        Optional<TokensEntity> tokensEntity = tokensRedisService.findById(authToken);

        if(!tokensEntity.isPresent()) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = tokensEntity.get().getAuthenticationToken();

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
        Jws<Claims> authClaim = Jwts.parserBuilder()
                .setSigningKey(pubKey)
                .build()
                .parseClaimsJws(token);

        String username = authClaim.getBody().getSubject();

        List<Map<String, String>> authorities = (List<Map<String, String>>) authClaim.getBody().get("authorities");
        Map<String, String> userDetails = (Map<String,String>) authClaim.getBody().get("userDetails");
        List<GrantedAuthority> grantedAuthorities = authorities.stream().map(map -> new SimpleGrantedAuthority(map.get("authority")))
                .collect(Collectors.toList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        httpServletRequest.setAttribute("username", username);
        httpServletRequest.setAttribute("authorities", grantedAuthorities);
        httpServletRequest.setAttribute("userDetails", userDetails);
        httpServletRequest.setAttribute("jwt", token);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
