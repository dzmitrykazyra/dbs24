/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import java.time.LocalDateTime;
import org.dbs24.application.core.nullsafe.NullSafe;

public final class SysEnvFuncs {

    //==========================================================================
    public static final String getSysEnvFuns() {

        return System.getenv()
                .keySet()
                .stream()
                .sorted()
                .reduce("System env \n ",
                        (x, y) -> x.concat(" ").concat(String.format("%40s = '%s'\n",
                                y, NullSafe.getStringObjValue(System.getenv(y)))));
    }
    //==========================================================================

    public static final String getSysProperties() {

        return System.getProperties()
                .keySet()
                .stream()
                .map(x -> (String) x)
                .sorted()
                .reduce("sysProperties \n ", (x, y) -> x.concat(" ").concat(String.format("%40s = '%s'\n",
                y, NullSafe.getStringObjValue(System.getProperty(y)))));
    }

    //==========================================================================
    public static final String getMemoryStatistics() {

        int mb = 1024 * 1024;

        // get Runtime instance
        final Runtime instance = Runtime.getRuntime();

        return String.format(
                "Heap utilization statistics [MB] "
                + ", Total Memory: %d"
                + ", Free Memory: %d"
                + ", Used Memory: %d"
                + ", Max Memory: %d",
                instance.totalMemory() / mb,
                instance.freeMemory() / mb,
                (instance.totalMemory() - instance.freeMemory()) / mb,
                instance.maxMemory() / mb);
    }
}
