package com.addressbook.repository.mybatis;

import com.addressbook.entity.Professional;
import com.addressbook.mapper.ProfessionalMapper;
import com.addressbook.repository.ProfessionalRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/** 基于 MyBatis 的专业仓储实现。 */
@Repository
public class MyBatisProfessionalRepository implements ProfessionalRepository {
    private final ProfessionalMapper professionalMapper;

    public MyBatisProfessionalRepository(ProfessionalMapper professionalMapper) {
        this.professionalMapper = professionalMapper;
    }

    @Override
    public void save(Professional professional) {
        if (professionalMapper.update(professional) == 0) {
            professionalMapper.insert(professional);
        }
    }

    @Override
    public Optional<Professional> findById(Long id) {
        return Optional.ofNullable(professionalMapper.selectById(id));
    }

    @Override
    public Optional<Professional> findByName(String name) {
        return Optional.ofNullable(professionalMapper.selectByName(name));
    }

    @Override
    public Collection<Professional> findAll() {
        return professionalMapper.selectAll();
    }

    @Override
    public void deleteById(Long id) {
        professionalMapper.deleteById(id);
    }

    @Override
    public Long nextId() {
        return professionalMapper.selectMaxId() + 1L;
    }
}
