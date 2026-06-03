package com.addressbook.service;

import com.addressbook.dto.ProfessionalRequest;
import com.addressbook.dto.ProfessionalResponse;
import com.addressbook.entity.Professional;
import com.addressbook.repository.ProfessionalRepository;
import com.addressbook.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** 处理专业信息的增删改查。 */
@Service
public class ProfessionalService {
    private final ProfessionalRepository professionalRepository;
    private final StudentRepository studentRepository;

    /** 创建专业服务。 */
    public ProfessionalService(ProfessionalRepository professionalRepository, StudentRepository studentRepository) {
        this.professionalRepository = professionalRepository;
        this.studentRepository = studentRepository;
    }

    /** 查询所有专业。 */
    public List<ProfessionalResponse> listAll() {
        return professionalRepository.findAll().stream()
                .map(ProfessionalResponse::from)
                .collect(Collectors.toList());
    }

    /** 新增专业。 */
    public boolean create(String name) {
        String normalizedName = normalize(name);
        if (normalizedName == null || professionalRepository.findByName(normalizedName).isPresent()) {
            return false;
        }
        professionalRepository.save(new Professional(professionalRepository.nextId(), normalizedName));
        return true;
    }

    /** 修改专业名称。 */
    public boolean update(Long id, String newName) {
        Professional professional = professionalRepository.findById(id).orElse(null);
        String normalizedName = normalize(newName);
        if (professional == null || normalizedName == null) {
            return false;
        }
        if (!Objects.equals(professional.getName(), normalizedName) && professionalRepository.findByName(normalizedName).isPresent()) {
            return false;
        }
        professionalRepository.save(professional.withName(normalizedName));
        return true;
    }

    /** 删除专业。 */
    public boolean delete(Long id) {
        Professional professional = professionalRepository.findById(id).orElse(null);
        if (professional == null) {
            return false;
        }
        boolean hasRelatedStudent = studentRepository.findAll().stream()
                .anyMatch(student -> Objects.equals(normalize(student.getMajor()), normalize(professional.getName())));
        if (hasRelatedStudent) {
            return false;
        }
        professionalRepository.deleteById(id);
        return true;
    }

    /** 判断专业名称是否可用。 */
    public boolean existsByName(String name) {
        String normalizedName = normalize(name);
        return normalizedName != null && professionalRepository.findByName(normalizedName).isPresent();
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
