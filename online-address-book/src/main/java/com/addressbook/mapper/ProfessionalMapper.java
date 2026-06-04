package com.addressbook.mapper;

import com.addressbook.entity.Professional;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 专业表 MyBatis Mapper。 */
public interface ProfessionalMapper {
    int insert(Professional professional);

    int update(Professional professional);

    Professional selectById(@Param("id") Long id);

    Professional selectByName(@Param("name") String name);

    List<Professional> selectAll();

    int deleteById(@Param("id") Long id);

    Long selectMaxId();
}
