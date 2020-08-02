/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.entity.marks;

import org.dbs24.application.core.service.funcs.ServiceFuncs;
//import org.dbs24.jdbc.api.exception.QueryExecutionException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import org.dbs24.entity.marks.api.EntityMarkHistory;
import org.dbs24.entity.marks.api.EntityMark;
import org.dbs24.application.core.log.LogService;
//import org.dbs24.services.api.ServiceLocator;
//import org.dbs24.services.FS24JdbcService;
import org.dbs24.application.core.sysconst.SysConst;
import org.dbs24.application.core.nullsafe.NullSafe;

/**
 *
 * @author kazyra_d
 */
public final class EntityMarkList {

    //private List<EntityMark> entityMarks;
    private Collection<EntityMarkHistory> entityMarksActions; // история установки отметок над сущностью
    private Collection<EntityMark> entityMarks;

    private Long entity_id;

    public EntityMarkList() {
        super();
    }

    public EntityMarkList(final Long entity_id) {
        this.entity_id = entity_id;
        NullSafe.create()
                .execute(() -> {
                    this.refresh();
                });
    }

    //==========================================================================
    public Collection<EntityMarkHistory> getEntityMarksActions() {
        return entityMarksActions;
    }

    public void setEntityMarksActions(final Collection<EntityMarkHistory> entityMarksActions) {
        this.entityMarksActions = entityMarksActions;
    }
    //==========================================================================

    public void refresh(final Long entity_id, Date from, Date to) {
        //List<EntityHistoryAction> historyActions = new ArrayList<>();
        this.entityMarksActions = ServiceFuncs.<EntityMarkHistory>getOrCreateCollection(this.entityMarksActions);

        entityMarksActions.clear();

//        ServiceLocator
//                .find(FS24JdbcService.class)
//                .createRsCall("{call core_get_entity_marks(:E, :D1, :D2)}")
//                //.setParamByName("E", ((EntityView) this.getAttributeValue("entityView", EntityView.class)).getEntity().getEntity_id())
//                .setParamByName("E", entity_id)
//                .setParamByName("D1", from)
//                .setParamByName("D2", new java.util.Date(to.getTime() + (24 * 60 * 60 * 1000)))
//                .execRsCallStmt((qry) -> {
//
//                    while (qry.next()) {
//
//                        int action_code, mark_id, mark_value_id;
//                        String err_msg, action_name, action_address, mark_name, mark_value_name, notes;
//                        LocalDateTime execute_date;
//                        LocalTime action_duration;
//                        Boolean mark_direction;
//
//                        Long action_id, user_id;
//
//                        action_id = qry.getLong("action_id");
//                        action_code = qry.getInteger("action_code");
//                        user_id = qry.getLong("user_id");
//                        execute_date = qry.getLocalDateTime("execute_date");
//                        action_name = qry.getString("action_name");
//                        err_msg = qry.getString("err_msg");
//                        action_address = qry.getString("action_address");
//                        action_duration = qry.getLocalTime("action_duration");
//                        mark_id = qry.getInteger("mark_id");
//                        mark_value_id = qry.getInteger("mark_value_id");
//                        mark_direction = qry.getBoolean("mark_direction");
//                        mark_name = qry.getString("mark_name");
//                        mark_value_name = qry.getString("mark_value_name");
//                        notes = qry.getString("notes");
//
//                        entityMarksActions.add(new EntityMarkHistory() {
//
//                            @Override
//                            public Boolean getMark_direction() {
//                                return mark_direction;
//                            }
//
//                            @Override
//                            public int getMark_value_id() {
//                                return mark_value_id;
//                            }
//
//                            @Override
//                            public int getMark_id() {
//                                return mark_id;
//                            }
//
//                            @Override
//                            public String getMark_name() {
//                                return mark_name;
//                            }
//
//                            @Override
//                            public String getMark_value_name() {
//                                return mark_value_name;
//                            }
//
//                            @Override
//                            public Long getAction_id() {
//                                return action_id;
//                            }
//
//                            @Override
//                            public Long getEntity_id() {
//                                return entity_id;
//                            }
//
//                            @Override
//                            public int action_code() {
//                                return action_code;
//                            }
//
//                            @Override
//                            public Long getUser_id() {
//                                return user_id;
//                            }
//
//                            @Override
//                            public LocalDateTime getExecute_date() {
//                                return execute_date;
//                            }
//
//                            @Override
//                            public String getNotes() {
//                                return notes;
//                            }
//
//                            @Override
//                            public String getErr_msg() {
//                                return err_msg;
//                            }
//
//                            @Override
//                            public String getAction_name() {
//                                return action_name;
//                            }
//
//                            @Override
//                            public String getApp_name() {
//                                return "????";
//                            }
//
//                            @Override
//                            public String getAction_address() {
//                                return action_address;
//                            }
//
//                            @Override
//                            public LocalTime getAction_duration() {
//                                return action_duration;
//                            }
//                        });
//                    }
//                });
    }

    public void refresh() {
        //List<EntityHistoryAction> historyActions = new ArrayList<>();}
        this.entityMarks = ServiceFuncs.<EntityMark>getOrCreateCollection(this.entityMarks);

        entityMarks.clear();

//        ServiceLocator
//                .find(FS24JdbcService.class)
//                .createRsCall("{call core_get_actual_entity_marks(:ENT)}")
//                .setParamByName("ENT", entity_id)
//                .execRsCallStmt((qry) -> {
//
//                    while (qry.next()) {
//
//                        int mark_id, mark_value_id;
//                        mark_id = qry.getInteger("mark_id");
//                        mark_value_id = qry.getInteger("mark_value_id");
//
//                        entityMarks.add(new EntityMark() {
//
//                            @Override
//                            public Integer getMark_value_id() {
//                                return mark_value_id;
//                            }
//
//                            @Override
//                            public Integer getMark_id() {
//                                return mark_id;
//                            }
//
//                        });
//                    }
//                });
    }

    //==========================================================================
    public Integer getMarkValue(final Integer mark_id, final Integer default_mark_value_id) {

        return NullSafe.create(default_mark_value_id, SysConst.IS_SILENT_EXECUTE)
                .inititialize(() -> {
                    this.entityMarks = ServiceFuncs.<EntityMark>getOrCreateCollection(this.entityMarks);
                })
                .execute2result(() -> {
                    return ServiceFuncs.<EntityMark>getCollectionElement_silent(this.entityMarks,
                            em -> (em.getMark_id().equals(mark_id)))
                            .getMark_value_id();
                }).<Integer>getObject();

    }

    //==========================================================================
    public void saveEntityMark(final Long entity_id, final Long action_id, final Integer mark_id, final Integer mark_value_id) {

//        ServiceLocator
//                .find(FS24JdbcService.class)
//                .createCallQuery("{call core_insert_entity_mark(:E, :A, :M, :V)}")
//                .setParamByName("E", entity_id)
//                .setParamByName("A", action_id)
//                .setParamByName("M", mark_id)
//                .setParamByName("V", mark_value_id)
//                .execCallStmt();
    }

    //==========================================================================
    public void addEntityMark(final Integer mark_id, final Integer mark_value_id) {
        //List<EntityHistoryAction> historyActions = new ArrayList<>();
        Integer v_mark_id = mark_id;
        Integer v_mark_value_id = mark_value_id;

        entityMarks.add(new EntityMark() {

            @Override
            public Integer getMark_value_id() {
                return v_mark_value_id;
            }

            @Override
            public Integer getMark_id() {
                return v_mark_id;
            }

        });
    }

    //==========================================================================
    public void removeEntityMark(final Integer mark_id) {

        entityMarks.remove(
                ServiceFuncs.<EntityMark>getCollectionElement(this.entityMarks,
                        mark -> mark.getMark_id().equals(mark_id),
                        String.format("Mark is not found (%d)", mark_id)));

    }
}
