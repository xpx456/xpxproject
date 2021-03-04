package com.restaurant.model.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class VeinDynamic {
    @Id(autoincrement = true)
    private Long id;
    private String fingerIndex;
    private String feat;
    private String userId;
    private Integer index;
    private String groupId;
    private String createTime;
    @Generated(hash = 354286578)
    public VeinDynamic(Long id, String fingerIndex, String feat, String userId,
            Integer index, String groupId, String createTime) {
        this.id = id;
        this.fingerIndex = fingerIndex;
        this.feat = feat;
        this.userId = userId;
        this.index = index;
        this.groupId = groupId;
        this.createTime = createTime;
    }
    @Generated(hash = 1295945375)
    public VeinDynamic() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFingerIndex() {
        return this.fingerIndex;
    }
    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }
    public String getFeat() {
        return this.feat;
    }
    public void setFeat(String feat) {
        this.feat = feat;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Integer getIndex() {
        return this.index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
}
