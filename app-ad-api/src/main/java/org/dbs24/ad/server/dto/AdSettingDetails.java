package org.dbs24.ad.server.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@EqualsAndHashCode
@Data
@Embeddable
public class AdSettingDetails {

    @Column(name = "banner_ads")
    private Boolean bannerAds;

    @Column(name = "desktop_ads")
    private Boolean desktopAds;

    @Column(name = "desktop_first_view_delay")
    private Integer desktopFirstViewDelay;

    @Column(name = "desktop_other_view_delay")
    private Integer desktopOtherViewDelay;

    @Column(name = "desktop_min_ads_pause")
    private Integer desktopMinAdsPause;

    @Column(name = "desktop_action_id")
    private Integer desktopActionId;
}
