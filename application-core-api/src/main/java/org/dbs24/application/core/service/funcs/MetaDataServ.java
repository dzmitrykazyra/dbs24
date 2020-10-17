/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import org.dbs24.application.core.nullsafe.NullSafe;
import static org.dbs24.application.core.sysconst.SysConst.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Wrapper;
import java.util.Arrays;

/**
 *
 * @author Козыро Дмитрий
 */
public final class MetaDataServ {

    final private static String skipMethods = ",getTypeInfo,notify,wait,equals,notifyAll,";

    public static final <T extends Wrapper> String getAbstractMetaData(final T metaData, final String mdName) {

        final CustomCollectionImpl customCollection = NullSafe.createObject(CustomCollectionImpl.class, mdName, (entity)-> {});

        NullSafe.create()
                .execute(() -> {

                    //final DatabaseMetaData dbmd = this.getConnection().getMetaData();
                    final String stringMask = "%50s::%s = '%s'\n";

                    // методы
                    Arrays.stream(metaData.getClass().getMethods())
                            .unordered()
                            .filter(method -> ((!method.getName().substring(0, 3).equals("set"))
                            && (skipMethods.indexOf(method.getName())) < 0))
                            .forEach(method -> {

                                if (method.getReturnType().equals(java.sql.ResultSet.class)) {

                                    customCollection.addCustomRecord(()
                                            -> MetaDataServ.getResultSetRecords(method.getName(),
                                                    ServiceFuncs.<ResultSet>getPropertySafe(() -> method.invoke(metaData, null))));

                                } else {

                                    customCollection.addCustomRecord(() -> String.format(stringMask,
                                            method.getName(),
                                            method.getReturnType().getName(),
                                            ServiceFuncs.<String>getPropertySafe(() -> method.invoke(metaData, null), NOT_DEFINED)));
                                }
                            });
                    // свойства
                    Arrays.stream(metaData.getClass().getFields())
                            .unordered()
                            //.filter(method -> !method.getName().substring(0, 3).equals("set"))
                            .forEach(field -> {

                                field.setAccessible(BOOLEAN_TRUE);

                                customCollection.addCustomRecord(() -> String.format(stringMask,
                                        field.getName(),
                                        field.getType().getName(),
                                        ServiceFuncs.<String>getPropertySafe(() -> field.get(metaData), NOT_DEFINED)));
                            });

                });
        //LogService.LogInfo(this.getClass(), this.getMetaData());

        return customCollection.getRecord();

    }

    //==========================================================================
    public static final String getResultSetRecords(final String rsName, final ResultSet resultSet) {

        final CustomCollectionImpl customCollection = NullSafe.createObject(CustomCollectionImpl.class, String.format("%50s::%s = '%s' ",
                rsName, ResultSet.class.getName(), " collection of records"));

        NullSafe.create(resultSet)
                .whenIsNull(() -> {
                    customCollection.addCustomRecord(() -> " is empty or is null\n");
                })
                .safeExecute((ns_resultSet) -> {

                    final ResultSetMetaData rsmd = resultSet.getMetaData();

                    final int columnCount = rsmd.getColumnCount();
                    int counter = 0;
                    //final List<Method> setters = this.getObjectSettersList(object);

                    while (resultSet.next()) {

                        //String columnName;
                        String record = String.format("\n%50d:\n", ++counter);

                        for (int i = 1; i <= columnCount; i++) {

                            final String columnName = rsmd.getColumnName(i);

                            record = record.concat(String.format("%50s: '%s';\n",
                                    columnName,
                                    ServiceFuncs.<String>getPropertySafe(() -> resultSet.getObject(columnName), NOT_DEFINED)));
                            //columnClass = this.getJavaTypeBySqlType(rsmd.getColumnType(i));
                        }

                        final String finalRecord = record;

                        customCollection.addCustomRecord(() -> finalRecord);
                    }
                    customCollection.addCustomRecord(() -> "\n");
                });

        return customCollection.getRecord();

    }

}
