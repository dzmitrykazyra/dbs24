package org.dbs24.tik.assist.entity.dto.tiktok;

import lombok.Builder;
import lombok.Data;
import org.dbs24.tik.assist.entity.domain.TiktokAccount;

@Data
@Builder
public class TiktokExistSubscriptionDto {

    private Integer accountId;
    private String username;
    private Boolean isExistSubscription;

    public static TiktokExistSubscriptionDto of(TiktokAccount tiktokAccount, Boolean isExistSubscription){
        return TiktokExistSubscriptionDto.builder()
                .accountId(tiktokAccount.getAccountId())
                .username(tiktokAccount.getAccountUsername())
                .isExistSubscription(isExistSubscription)
                .build();
    }
}
