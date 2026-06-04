package com.addressbook.repository;

import com.addressbook.entity.Professional;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** 使用内存保存专业信息，供单元测试使用。 */
public class InMemoryProfessionalRepository implements ProfessionalRepository {
    private final Map<Long, Professional> professionalsById = new ConcurrentHashMap<Long, Professional>();
    private final Map<String, Professional> professionalsByName = new ConcurrentHashMap<String, Professional>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    /** 保存专业记录。 */
    @Override
    public void save(Professional professional) {
        Professional existing = professionalsById.get(professional.getId());
        if (existing != null && !existing.getName().equals(professional.getName())) {
            professionalsByName.remove(existing.getName());
        }
        professionalsById.put(professional.getId(), professional);
        professionalsByName.put(professional.getName(), professional);
        if (professional.getId() >= idGenerator.get()) {
            idGenerator.set(professional.getId() + 1);
        }
    }

    /** 按 ID 查询专业。 */
    @Override
    public Optional<Professional> findById(Long id) {
        return Optional.ofNullable(professionalsById.get(id));
    }

    /** 按名称查询专业。 */
    @Override
    public Optional<Professional> findByName(String name) {
        return Optional.ofNullable(professionalsByName.get(name));
    }

    /** 查询所有专业。 */
    @Override
    public Collection<Professional> findAll() {
        return professionalsById.values();
    }

    /** 删除专业记录。 */
    @Override
    public void deleteById(Long id) {
        Professional removed = professionalsById.remove(id);
        if (removed != null) {
            professionalsByName.remove(removed.getName());
        }
    }

    /** 生成新的专业 ID。 */
    @Override
    public Long nextId() {
        return idGenerator.getAndIncrement();
    }
}
