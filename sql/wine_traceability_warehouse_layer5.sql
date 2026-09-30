create table if not exists trace_warehouse_bill (
  bill_id                 bigint(20)      not null auto_increment comment '仓储单据ID',
  bill_type               varchar(16)     not null                comment '单据类型(IN入库 OUT出库)',
  bill_no                 varchar(64)     not null                comment '单据编号',
  operator_id             bigint(20)      default null            comment '操作人ID',
  operate_time            datetime                                comment '操作时间',
  remark                  varchar(500)    default null            comment '备注',
  del_flag                char(1)         default '0'             comment '删除标志(0存在 2删除)',
  create_by               varchar(64)     default ''              comment '创建者',
  create_time             datetime                                comment '创建时间',
  update_by               varchar(64)     default ''              comment '更新者',
  update_time             datetime                                comment '更新时间',
  primary key (bill_id),
  key idx_trace_bill_type (bill_type),
  key idx_trace_bill_operate_time (operate_time),
  unique key uk_trace_bill_no (bill_no, del_flag)
) engine=innodb auto_increment=1 comment='仓储单据表';

create table if not exists trace_warehouse_bill_detail (
  detail_id               bigint(20)      not null auto_increment comment '单据明细ID',
  bill_id                 bigint(20)      not null                comment '单据ID',
  bottling_id             bigint(20)      not null                comment '灌装ID',
  quantity                int(11)         not null                comment '数量(瓶)',
  start_trace_code        varchar(64)     default null            comment '起始溯源码',
  end_trace_code          varchar(64)     default null            comment '结束溯源码',
  del_flag                char(1)         default '0'             comment '删除标志(0存在 2删除)',
  create_by               varchar(64)     default ''              comment '创建者',
  create_time             datetime                                comment '创建时间',
  update_by               varchar(64)     default ''              comment '更新者',
  update_time             datetime                                comment '更新时间',
  primary key (detail_id),
  key idx_trace_bill_detail_bill_id (bill_id),
  key idx_trace_bill_detail_bottling_id (bottling_id)
) engine=innodb auto_increment=1 comment='仓储单据明细表';

create table if not exists trace_logistics (
  logistics_id            bigint(20)      not null auto_increment comment '物流信息ID',
  out_bill_id             bigint(20)      not null                comment '出库单ID',
  carrier                 varchar(100)    default null            comment '承运商',
  tracking_no             varchar(100)    default null            comment '运单号',
  consignee               varchar(100)    default null            comment '收货方',
  destination             varchar(255)    default null            comment '目的地',
  ship_date               datetime                                comment '发货日期',
  remark                  varchar(500)    default null            comment '备注',
  del_flag                char(1)         default '0'             comment '删除标志(0存在 2删除)',
  create_by               varchar(64)     default ''              comment '创建者',
  create_time             datetime                                comment '创建时间',
  update_by               varchar(64)     default ''              comment '更新者',
  update_time             datetime                                comment '更新时间',
  primary key (logistics_id),
  key idx_trace_logistics_out_bill_id (out_bill_id),
  key idx_trace_logistics_tracking_no (tracking_no),
  unique key uk_trace_logistics_out_bill_id (out_bill_id, del_flag)
) engine=innodb auto_increment=1 comment='物流信息表';

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2080, '仓储物流', 0, 8, 'trace-warehouse', null, '', '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', null, '葡萄酒仓储物流目录'
from dual
where not exists (select 1 from sys_menu where menu_id = 2080);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2081, '仓储单据', 2080, 1, 'warehouse', 'trace/warehouse/index', '', '', 1, 0, 'C', '0', '0', 'trace:warehouse:list', 'nested', 'admin', sysdate(), '', null, '仓储单据管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2081);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2082, '物流信息', 2080, 2, 'logistics', 'trace/logistics/index', '', '', 1, 0, 'C', '0', '0', 'trace:logistics:list', 'deliver', 'admin', sysdate(), '', null, '物流信息管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2082);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2083, '溯源查询', 2080, 3, 'query', 'trace/query/index', '', '', 1, 0, 'C', '0', '0', 'trace:query:view', 'search', 'admin', sysdate(), '', null, '溯源查询菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2083);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2084, '单据查询', 2081, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:warehouse:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2084);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2085, '单据新增', 2081, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:warehouse:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2085);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2086, '单据修改', 2081, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:warehouse:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2086);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2087, '单据删除', 2081, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:warehouse:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2087);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2088, '物流查询', 2082, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:logistics:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2088);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2089, '物流新增', 2082, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:logistics:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2089);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2090, '物流修改', 2082, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:logistics:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2090);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2091, '物流删除', 2082, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:logistics:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2091);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2092, '溯源查看', 2083, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:query:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2092);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2093, '公开查询', 2083, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:query:view', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2093);

insert into sys_role_menu (role_id, menu_id)
select 2, t.menu_id
from (
    select 2080 as menu_id union all
    select 2081 union all
    select 2082 union all
    select 2083 union all
    select 2084 union all
    select 2085 union all
    select 2086 union all
    select 2087 union all
    select 2088 union all
    select 2089 union all
    select 2090 union all
    select 2091 union all
    select 2092 union all
    select 2093
) t
where not exists (
    select 1 from sys_role_menu rm where rm.role_id = 2 and rm.menu_id = t.menu_id
);