/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.core.list;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
import java.util.HashMap;
import java.util.Map;
import org.dbs24.entity.core.api.EntityWarningsList;
import org.dbs24.application.core.log.LogService;
import org.dbs24.application.core.nullsafe.NullSafe;
import java.util.stream.Collectors;

/**
 *
 * @author kazyra_d
 */
public class EntWarningListImpl implements EntityWarningsList {

    Map<String, String> warningList = null;

    public void addWarning(final String fldKey, final String warningMsg) {

        LogService.LogWarn(this.getClass(),
                LogService.getCurrentObjProcName(this),
                () -> String.format("%s: %s", fldKey, warningMsg));

        (NullSafe.create(this.warningList)
                .whenIsNull(() -> {
                    this.warningList = ServiceFuncs.<String, String>getOrCreateMap(ServiceFuncs.MAP_NULL);
                    return this.warningList;
                }).<Map<String, String>>getObject()).put(fldKey, warningMsg);
    }

    //==========================================================================
    @Override
    public int getWarningsCount() {
        return (NullSafe.create(warningList)
                .whenIsNull(() -> {
                    return new HashMap<>();
                })
                .<Map<String, String>>getObject())
                .size();
    }

    @Override
    public void clearWarnings() {

        NullSafe.create(warningList)
                .safeExecute(() -> {
                    warningList.clear();
                });
    }

    //==========================================================================
    @Override
    public String get_field_warning(final String fldName) {

        return NullSafe.create(this.warningList)
                .whenIsNull(() -> {
                    return null;
                })
                .safeExecute2result((ns_wl) -> {

                    String field_warning = null;
                    Map<String, String> mapFldWarn = (((Map<String, String>) ns_wl).entrySet())
                            .stream()
                            .filter(p -> p.getKey().equals(fldName))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    if (!mapFldWarn.isEmpty()) {
                        field_warning = (String) mapFldWarn.get(fldName);
                    }
                    return field_warning;
                })
                .<String>getObject();

    }
}
