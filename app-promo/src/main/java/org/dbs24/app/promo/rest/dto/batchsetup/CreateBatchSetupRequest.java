/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.rest.dto.batchsetup;

import lombok.Data;
import org.dbs24.rest.api.action.SimpleActionInfo;
import org.dbs24.spring.core.api.PostRequestBody;

@Data
public class CreateBatchSetupRequest implements PostRequestBody {

    private SimpleActionInfo entityAction;
    private BatchSetupInfo entityInfo;

}
