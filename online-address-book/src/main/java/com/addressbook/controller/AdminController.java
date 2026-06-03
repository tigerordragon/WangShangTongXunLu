package com.addressbook.controller;

import com.addressbook.dto.AdminStudentListResponse;
import com.addressbook.dto.AdminStudentResponse;
import com.addressbook.dto.MessageResponse;
import com.addressbook.dto.ProfessionalListResponse;
import com.addressbook.dto.ProfessionalRequest;
import com.addressbook.service.AdminStudentService;
import com.addressbook.service.ProfessionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 处理管理员账户和专业维护接口。 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminStudentService adminStudentService;
    private final ProfessionalService professionalService;

    /** 创建管理员控制器。 */
    public AdminController(AdminStudentService adminStudentService, ProfessionalService professionalService) {
        this.adminStudentService = adminStudentService;
        this.professionalService = professionalService;
    }

    /** 查询待删除账户。 */
    @GetMapping("/students/pending")
    public AdminStudentListResponse pendingStudents() {
        return new AdminStudentListResponse(true, adminStudentService.listPendingAccounts());
    }

    /** 查询已通过审核账户。 */
    @GetMapping("/students/approved")
    public AdminStudentListResponse approvedStudents() {
        return new AdminStudentListResponse(true, adminStudentService.listApprovedAccounts());
    }

    /** 查询已禁用账户。 */
    @GetMapping("/students/disabled")
    public AdminStudentListResponse disabledStudents() {
        return new AdminStudentListResponse(true, adminStudentService.listDisabledAccounts());
    }

    /** 删除未通过审核账户。 */
    @DeleteMapping("/students/{id}")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable Long id) {
        if (!adminStudentService.deleteUnapprovedAccount(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "仅允许删除未通过审核的账户"));
        }
        return ResponseEntity.ok(new MessageResponse(true, "删除成功"));
    }

    /** 禁用已通过审核账户。 */
    @PostMapping("/students/{id}/disable")
    public ResponseEntity<MessageResponse> disableStudent(@PathVariable Long id) {
        if (!adminStudentService.disableApprovedAccount(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "仅允许禁用已通过审核的账户"));
        }
        return ResponseEntity.ok(new MessageResponse(true, "禁用成功"));
    }

    /** 启用已禁用账户。 */
    @PostMapping("/students/{id}/enable")
    public ResponseEntity<MessageResponse> enableStudent(@PathVariable Long id) {
        if (!adminStudentService.enableDisabledAccount(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "仅允许启用已禁用的账户"));
        }
        return ResponseEntity.ok(new MessageResponse(true, "启用成功"));
    }

    /** 查询专业列表。 */
    @GetMapping("/professionals")
    public ProfessionalListResponse listProfessionals() {
        return new ProfessionalListResponse(true, professionalService.listAll());
    }

    /** 新增专业。 */
    @PostMapping("/professionals")
    public ResponseEntity<MessageResponse> createProfessional(@RequestBody ProfessionalRequest request) {
        if (!professionalService.create(request.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "专业名称不能为空或重复"));
        }
        return ResponseEntity.ok(new MessageResponse(true, "新增成功"));
    }

    /** 修改专业。 */
    @PutMapping("/professionals/{id}")
    public ResponseEntity<MessageResponse> updateProfessional(@PathVariable Long id, @RequestBody ProfessionalRequest request) {
        if (!professionalService.update(id, request.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "专业无法修改"));
        }
        return ResponseEntity.ok(new MessageResponse(true, "修改成功"));
    }

    /** 删除专业。 */
    @DeleteMapping("/professionals/{id}")
    public ResponseEntity<MessageResponse> deleteProfessional(@PathVariable Long id) {
        if (!professionalService.delete(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "专业正在被学生使用或不存在"));
        }
        return ResponseEntity.ok(new MessageResponse(true, "删除成功"));
    }
}
