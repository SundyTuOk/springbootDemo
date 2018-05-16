package com.tu.manager.dao;

import com.tu.manager.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends JpaRepository<Admin,Integer>{
}
