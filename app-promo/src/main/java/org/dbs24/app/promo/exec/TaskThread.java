/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.app.promo.exec;

import org.dbs24.application.core.thread.AbstractThread;

public class TaskThread extends AbstractThread {

    public TaskThread(Runnable target) {
        super(target);
    }

}
