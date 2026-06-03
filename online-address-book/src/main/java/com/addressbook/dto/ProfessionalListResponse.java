package com.addressbook.dto;

import java.util.List;

/** 表示专业列表响应。 */
public class ProfessionalListResponse {
    private final boolean success;
    private final List<ProfessionalResponse> professionals;

    /** 创建专业列表响应。 */
    public ProfessionalListResponse(boolean success, List<ProfessionalResponse> professionals) {
        this.success = success;
        this.professionals = professionals;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<ProfessionalResponse> getProfessionals() {
        return professionals;
    }
}
