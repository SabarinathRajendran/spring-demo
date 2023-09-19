package com.cerclex.epr.authorization.services.redis;


import com.cerclex.epr.authorization.dao.redis.TokensRedisRepository;
import com.cerclex.epr.authorization.model.redis.TokensEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokensRedisService {


    @Autowired
    private TokensRedisRepository tokensRedisRepository;

    public TokensEntity save(TokensEntity entity) {
        return tokensRedisRepository.save(entity);
    }


    public Optional<TokensEntity> findById(String id) {
        return tokensRedisRepository.findById(id);
    }

    public Iterable<TokensEntity> findAll() {
        return tokensRedisRepository.findAll();
    }


}
