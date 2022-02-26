package org.dbs24.tik.assist.entity.dto.subscription;

import lombok.Data;

@Data
public class ByTemplateUserSubscriptionDto extends AbstractUserSubscriptionDto {

    private Integer subscriptionTemplateId;

}
