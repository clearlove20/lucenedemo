package com.lucene.dao;

import com.lucene.pojo.Poetry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

@Mapper
public interface PoetryDAO {

    public List<Poetry> findAll();
}
