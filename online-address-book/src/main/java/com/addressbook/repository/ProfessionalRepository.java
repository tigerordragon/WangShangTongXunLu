package com.addressbook.repository;

import com.addressbook.entity.Professional;

import java.util.Collection;
import java.util.Optional;

/** 定义专业信息的查询和保存操作。 */
public interface ProfessionalRepository {
    /** 保存专业记录。 */
    void save(Professional professional);

    /** 按 ID 查询专业。 */
    Optional<Professional> findById(Long id);

    /** 按名称查询专业。 */
    Optional<Professional> findByName(String name);

    /** 查询所有专业。 */
    Collection<Professional> findAll();

    /** 删除专业记录。 */
    void deleteById(Long id);

    /** 生成新的专业 ID。 */
    Long nextId();
}
