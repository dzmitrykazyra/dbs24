package org.dbs24.entity;

import java.time.LocalDateTime;

public interface SubscriptionPhoneActivity {

    Long getActivity_id();
    LocalDateTime getActual_date();
    LocalDateTime getSubscription_actual_date();
    LocalDateTime getContract_actual_date();
    Integer getSubscription_id();
    Boolean getIs_online();
    String getPhone_num();
}
