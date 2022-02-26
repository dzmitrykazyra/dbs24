/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.notifier.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dbs24.app.notifier.entity.MessageDetails;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class MessageDto {

    @EqualsAndHashCode.Include
    private Integer messageId;
    private Long createDate;
    private Long actualDateFrom;
    private Long actualDateTo;
    private Boolean isActual;
    private Boolean isMultiplyMessage;
    private MessageDetails messageDetails;
    private String msgAddress;
    private String packagesList;
    private String packagesMinVersion;
    private String packagesMaxVersion;
}
