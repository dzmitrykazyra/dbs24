package org.dbs24.tik.assist.entity.dto.subscription;

import lombok.Data;
import org.dbs24.tik.assist.entity.dto.subscription.UserSubscriptionPlanStatisticsDto;

import java.util.List;

@Data
public class UserSubscriptionPlanStatisticsDtoList {

    private List<UserSubscriptionPlanStatisticsDto> statisticsDtoList;
}
