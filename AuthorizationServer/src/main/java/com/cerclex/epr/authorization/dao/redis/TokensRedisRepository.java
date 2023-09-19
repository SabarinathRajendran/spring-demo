package com.cerclex.epr.authorization.dao.redis;

import com.cerclex.epr.authorization.model.redis.TokensEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensRedisRepository extends CrudRepository<TokensEntity, String> {
}
