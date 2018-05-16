package com.tu.manager.dao2;

import com.tu.manager.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao2 extends JpaRepository<Admin,Integer>{
}
