package com.cerclex.epr.authorization.dao;

import com.cerclex.epr.authorization.dtos.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {
}
