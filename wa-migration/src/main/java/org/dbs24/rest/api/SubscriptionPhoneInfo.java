/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import static org.dbs24.consts.SysConst.DATETIME_MS_FORMAT;
import org.dbs24.entity.SubscriptionPhone;
import org.dbs24.stmt.StmtProcessor;

@Data
@NoArgsConstructor
public class SubscriptionPhoneInfo {

    private Integer id;
    private Long phoneNum;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_MS_FORMAT)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime addTime;
    private Integer isValid;
    private Integer userId;
    private Integer authKeyId;
    private Integer avatarId;
    private byte[] avatar;
    private Integer notify;
    private String assignedName;
    private Integer isRemoved;

    public void assign(SubscriptionPhone subscriptionPhone) {
        this.addTime = subscriptionPhone.getAddTime();
        this.assignedName = subscriptionPhone.getAssignedName();
        this.authKeyId = StmtProcessor.isNull(subscriptionPhone.getAuthKey()) ? null : subscriptionPhone.getAuthKey().getId();
        this.avatar = subscriptionPhone.getAvatar();
        this.avatarId = subscriptionPhone.getAvatarId();
        this.id = subscriptionPhone.getId();
        this.isRemoved = subscriptionPhone.getIsRemoved();
        this.isValid = subscriptionPhone.getIsValid();
        this.notify = subscriptionPhone.getNotify();
        this.phoneNum = subscriptionPhone.getPhoneNum();
        this.userId = subscriptionPhone.getAppUser().getId();
    }
}
