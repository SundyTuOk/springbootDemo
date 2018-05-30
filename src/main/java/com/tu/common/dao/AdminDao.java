package com.tu.common.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AdminDao{

    Map<String,String> find();
}
