/* Drop Tables */

DROP TABLE IF EXISTS tm_cost_to_quantity CASCADE
;

DROP TABLE IF EXISTS tm_currencies_ref CASCADE
;

DROP TABLE IF EXISTS tm_hearts_price CASCADE
;

DROP TABLE IF EXISTS tm_order_action_types_ref CASCADE
;

DROP TABLE IF EXISTS tm_order_actions CASCADE
;

DROP TABLE IF EXISTS tm_order_execution_progresses CASCADE
;

DROP TABLE IF EXISTS tm_order_statuses_ref CASCADE
;

DROP TABLE IF EXISTS tm_user_deposits CASCADE
;

DROP TABLE IF EXISTS tm_user_deposits_hist CASCADE
;

DROP TABLE IF EXISTS tm_user_orders CASCADE
;

DROP TABLE IF EXISTS tm_user_statuses_ref CASCADE
;

DROP TABLE IF EXISTS tm_users CASCADE
;

DROP TABLE IF EXISTS tm_users_hist CASCADE
;

/* Create Tables */

CREATE TABLE tm_cost_to_quantity
(
    cost_id integer NOT NULL,
    "action_type_id" integer NOT NULL,
    up_to_cost_quantity integer NOT NULL,
    sum integer NOT NULL
)
;

CREATE TABLE tm_currencies_ref
(
    currency_iso varchar(3) NOT NULL,
    currency_id integer NOT NULL,
    currency_name varchar(100) NOT NULL
)
;

CREATE TABLE tm_hearts_price
(
    hearts_price_id integer NOT NULL,
    hearts_amount integer NOT NULL,
    price integer NOT NULL,
    currency_iso varchar(3) NULL
)
;

CREATE TABLE tm_order_action_types_ref
(
    order_action_type_id integer NOT NULL,
    order_action_type_name varchar(100) NOT NULL
)
;

CREATE TABLE tm_order_actions
(
    order_action_id integer NOT NULL,
    order_id integer NOT NULL,
    user_id integer NOT NULL,
    start_date timestamp without time zone NOT NULL,
    finish_date timestamp without time zone NOT NULL
)
;

CREATE TABLE tm_order_execution_progresses
(
    order_execution_progress_id integer NOT NULL,
    order_id integer NOT NULL,
    done_actions_quantity integer NOT NULL
)
;

CREATE TABLE tm_order_statuses_ref
(
    order_status_id integer NOT NULL,
    order_status_name varchar(50) NOT NULL
)
;

CREATE TABLE tm_user_deposits
(
    user_id integer NOT NULL,
    actual_date timestamp without time zone NOT NULL,
    rest_sum integer NOT NULL
)
;

CREATE TABLE tm_user_deposits_hist
(
    user_id integer NOT NULL,
    actual_date timestamp without time zone NOT NULL,
    rest_sum integer NOT NULL
)
;

CREATE TABLE tm_user_orders
(
    order_id integer NOT NULL,
    order_status_id integer NOT NULL,
    action_type_id integer NOT NULL,
    actual_date timestamp without time zone NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    actions_amount integer NOT NULL,
    order_sum integer NOT NULL,
    order_name varchar(100) NULL,
    tiktok_uri varchar(200) NULL,
    aweme_id varchar(40) NULL,
    user_id integer NOT NULL
)
;

CREATE TABLE tm_user_statuses_ref
(
    user_status_id integer NOT NULL,
    user_status_name varchar(100) NOT NULL
)
;

CREATE TABLE tm_users
(
    user_id integer NOT NULL,
    actual_date timestamp without time zone NOT NULL,
    user_name varchar(128) NOT NULL,
    hash_pass varchar(200) NOT NULL,
    email varchar(320) NULL,
    user_status_id integer NOT NULL,
    sec_user_id varchar(200) NULL
)
;

CREATE TABLE tm_users_hist
(
    user_id integer NOT NULL,
    actual_date timestamp without time zone NOT NULL,
    user_name varchar(128) NOT NULL,
    hash_pass varchar(50) NOT NULL,
    email varchar(320) NULL,
    user_status_id integer NOT NULL,
    sec_user_id varchar(200) NULL
)
;

/* Create Primary Keys, Indexes, Uniques, Checks */

ALTER TABLE tm_cost_to_quantity ADD CONSTRAINT "PK_tm_cost_to_quantity"
    PRIMARY KEY (cost_id)
;

CREATE INDEX "IXFK_tm_cost_to_quantity_tm_order_action_types_ref" ON tm_cost_to_quantity ("action_type_id" ASC)
;

ALTER TABLE tm_currencies_ref ADD CONSTRAINT "PK_tm_currencies_ref"
    PRIMARY KEY (currency_iso)
;

ALTER TABLE tm_hearts_price ADD CONSTRAINT "PK_tm_hearts_price"
    PRIMARY KEY (hearts_price_id)
;

CREATE INDEX "IXFK_tm_hearts_price_tm_currencies_ref" ON tm_hearts_price (currency_iso ASC)
;

ALTER TABLE tm_order_action_types_ref ADD CONSTRAINT "PK_order_action_types"
    PRIMARY KEY (order_action_type_id)
;

ALTER TABLE tm_order_actions ADD CONSTRAINT "PK_tik_order_actions"
    PRIMARY KEY (order_action_id)
;

CREATE INDEX "IXFK_tik_order_actions_tik_orders" ON tm_order_actions (order_id ASC)
;

CREATE INDEX "IXFK_tik_order_actions_tik_users" ON tm_order_actions (user_id ASC)
;

ALTER TABLE tm_order_execution_progresses ADD CONSTRAINT "PK_tik_order_execution_progress"
    PRIMARY KEY (order_execution_progress_id)
;

CREATE INDEX "IXFK_tik_order_execution_progress_tik_orders" ON tm_order_execution_progresses (order_id ASC)
;

ALTER TABLE tm_order_statuses_ref ADD CONSTRAINT "PK_order_statuses"
    PRIMARY KEY (order_status_id)
;

ALTER TABLE tm_user_deposits ADD CONSTRAINT "PK_tik_users_deposit"
    PRIMARY KEY (user_id)
;

CREATE INDEX "IXFK_tik_user_deposits_tik_user_deposits_hit" ON tm_user_deposits (user_id ASC)
;

CREATE INDEX "IXFK_tik_users_deposit_tik_users" ON tm_user_deposits (user_id ASC)
;

CREATE INDEX "IXFK_tm_user_deposits_tm_user_deposits_hist" ON tm_user_deposits (user_id ASC,actual_date ASC)
;

CREATE INDEX "IXFK_tm_user_deposits_tm_users" ON tm_user_deposits (user_id ASC)
;

ALTER TABLE tm_user_deposits_hist ADD CONSTRAINT "PK_tm_user_deposits_hist"
    PRIMARY KEY (user_id,actual_date)
;

CREATE INDEX "IXFK_tm_user_deposits_hist_tm_user_deposits" ON tm_user_deposits_hist (user_id ASC)
;

ALTER TABLE tm_user_orders ADD CONSTRAINT "PK_tik_orders"
    PRIMARY KEY (order_id)
;

CREATE INDEX "IXFK_tik_orders_order_statuses" ON tm_user_orders (order_status_id ASC)
;

CREATE INDEX "IXFK_tik_orders_tik_users" ON tm_user_orders (user_id ASC)
;

CREATE INDEX "IXFK_tik_user_orders_order_action_types" ON tm_user_orders (action_type_id ASC)
;

ALTER TABLE tm_user_statuses_ref ADD CONSTRAINT "PK_tik_user_statuses"
    PRIMARY KEY (user_status_id)
;

ALTER TABLE tm_users ADD CONSTRAINT "PK_tik_users"
    PRIMARY KEY (user_id)
;

CREATE INDEX "IXFK_tik_users_tik_user_statuses" ON tm_users (user_status_id ASC)
;

ALTER TABLE tm_users_hist ADD CONSTRAINT "PK_tik_users_hist"
    PRIMARY KEY (user_id,actual_date)
;

CREATE INDEX "IXFK_tik_users_hist_tik_users" ON tm_users_hist (user_id ASC)
;

/* Create Foreign Key Constraints */

ALTER TABLE tm_cost_to_quantity ADD CONSTRAINT "FK_tm_cost_to_quantity_tm_order_action_types_ref"
    FOREIGN KEY ("action_type_id") REFERENCES tm_order_action_types_ref (order_action_type_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_hearts_price ADD CONSTRAINT "FK_tm_hearts_price_tm_currencies_ref"
    FOREIGN KEY (currency_iso) REFERENCES tm_currencies_ref (currency_iso) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_order_actions ADD CONSTRAINT "FK_tik_order_actions_tik_orders"
    FOREIGN KEY (order_id) REFERENCES tm_user_orders (order_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_order_actions ADD CONSTRAINT "FK_tik_order_actions_tik_users"
    FOREIGN KEY (user_id) REFERENCES tm_users (user_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_order_execution_progresses ADD CONSTRAINT "FK_tik_order_execution_progress_tik_orders"
    FOREIGN KEY (order_id) REFERENCES tm_user_orders (order_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_user_deposits ADD CONSTRAINT "FK_tm_user_deposits_tm_users"
    FOREIGN KEY (user_id) REFERENCES tm_users (user_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_user_orders ADD CONSTRAINT "FK_tik_orders_order_statuses"
    FOREIGN KEY (order_status_id) REFERENCES tm_order_statuses_ref (order_status_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_user_orders ADD CONSTRAINT "FK_tik_orders_tik_users"
    FOREIGN KEY (user_id) REFERENCES tm_users (user_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_user_orders ADD CONSTRAINT "FK_tik_user_orders_order_action_types"
    FOREIGN KEY (action_type_id) REFERENCES tm_order_action_types_ref (order_action_type_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_users ADD CONSTRAINT "FK_tik_users_tik_user_statuses"
    FOREIGN KEY (user_status_id) REFERENCES tm_user_statuses_ref (user_status_id) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE tm_users_hist ADD CONSTRAINT "FK_tik_users_hist_tik_users"
    FOREIGN KEY (user_id) REFERENCES tm_users (user_id) ON DELETE No Action ON UPDATE No Action
;
