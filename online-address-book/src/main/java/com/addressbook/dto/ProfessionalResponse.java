package com.addressbook.dto;

import com.addressbook.entity.Professional;

/** 表示专业响应。 */
public class ProfessionalResponse {
    private final Long id;
    private final String name;

    /** 创建专业响应对象。 */
    public ProfessionalResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /** 由专业对象转换为响应对象。 */
    public static ProfessionalResponse from(Professional professional) {
        return new ProfessionalResponse(professional.getId(), professional.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
