/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.tik.assist.entity.dto.action;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.OrderAction;

@Data
public class ActionTask {

    @JsonProperty("task_id")    
    private Long actionId;
    @JsonProperty("contract_type")    
    private Integer contractTypeId;
    @JsonProperty("agent_id")    
    private Integer agentId;/*
    @JsonProperty("tiktok_uri")    
    private String tikUri;*/
    @JsonProperty("aweme_id")
    private String awemeId;
    @JsonProperty("sec_user_id")
    private String secUserId;/*
    @JsonProperty("comment_id")
    private String cid;*/

    public static ActionTask toActionTask(OrderAction orderAction) {

        return StmtProcessor.create(
                ActionTask.class,
                actionTask -> {
                    actionTask.setActionId(Long.valueOf(orderAction.getOrderActionId()));
                    actionTask.setContractTypeId(orderAction.getUserOrder().getActionType().getActionTypeId());
                    actionTask.setAgentId(orderAction.getBot().getBotId());/*
                    actionTask.setTikUri(orderAction.getUserOrder().getTiktokUri());*/
                    actionTask.setAwemeId(orderAction.getUserOrder().getAwemeId());
                    actionTask.setSecUserId(orderAction.getUserOrder().getTiktokAccount().getSecUserId());/*
                    actionTask.setCid(orderAction.getUserOrder().getCid());*/
        });
    }
}
