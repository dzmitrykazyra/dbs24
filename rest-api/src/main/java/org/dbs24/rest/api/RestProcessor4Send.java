/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import java.util.Collection;
import javax.ws.rs.core.Response;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
public abstract class RestProcessor4Send extends ServRestProcessor {

    public RestProcessor4Send() {
        super();
    }

    //==========================================================================
    public <T extends Object> Response postMessages(final Collection<T> msgs, final String path) {

        if (NullSafe.isNull(this.getTargetServer())) {
            this.initRestService();
        }

        return this.postMessages(
                msgs,
                this.getTargetServer(),
                path,
                this.getServerReadTimeout(),
                this.getServerConnectTimeout(),
                HttpConst.HTTP_200_OK);
    }

}
