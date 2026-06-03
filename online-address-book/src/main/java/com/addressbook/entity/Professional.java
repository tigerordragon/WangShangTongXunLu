package com.addressbook.entity;

/** 表示系统中的专业信息。 */
public class Professional {
    private final Long id;
    private final String name;

    /** 创建专业对象。 */
    public Professional(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /** 返回专业 ID。 */
    public Long getId() {
        return id;
    }

    /** 返回专业名称。 */
    public String getName() {
        return name;
    }

    /** 返回修改专业名称后的对象。 */
    public Professional withName(String newName) {
        return new Professional(id, newName);
    }
}
