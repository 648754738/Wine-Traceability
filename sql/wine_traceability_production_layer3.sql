create table if not exists trace_batch (
  batch_id              bigint(20)      not null auto_increment comment '生产批次ID',
  batch_no              varchar(64)     not null                comment '批次号',
  product_id            bigint(20)      not null                comment '产品ID',
  plot_id               bigint(20)      not null                comment '地块ID',
  workshop_id           bigint(20)      not null                comment '车间ID',
  entry_workshop_time   datetime                                comment '进入车间时间',
  harvest_date          date                                     comment '采收日期',
  harvest_location      varchar(255)    default null            comment '采收地描述',
  total_yield_l         decimal(10,2)   default null            comment '原酒产量(升)',
  status                char(1)         default '0'             comment '状态(0正常 1停用)',
  del_flag              char(1)         default '0'             comment '删除标志(0存在 2删除)',
  create_by             varchar(64)     default ''              comment '创建者',
  create_time           datetime                                comment '创建时间',
  update_by             varchar(64)     default ''              comment '更新者',
  update_time           datetime                                comment '更新时间',
  remark                varchar(500)    default null            comment '备注',
  primary key (batch_id),
  key idx_trace_batch_product_id (product_id),
  key idx_trace_batch_plot_id (plot_id),
  key idx_trace_batch_workshop_id (workshop_id),
  key idx_trace_batch_status (status),
  unique key uk_trace_batch_no (batch_no, del_flag)
) engine=innodb auto_increment=1 comment='生产批次表';

create table if not exists trace_process_record (
  process_id            bigint(20)      not null auto_increment comment '工序记录ID',
  batch_id              bigint(20)      not null                comment '批次ID',
  process_type          varchar(50)     not null                comment '工序类型',
  start_time            datetime                                comment '开始时间',
  end_time              datetime                                comment '结束时间',
  temp_control          varchar(100)    default null            comment '温控范围',
  operator_name         varchar(50)     default null            comment '操作人',
  remarks               varchar(500)    default null            comment '工序说明',
  del_flag              char(1)         default '0'             comment '删除标志(0存在 2删除)',
  create_by             varchar(64)     default ''              comment '创建者',
  create_time           datetime                                comment '创建时间',
  update_by             varchar(64)     default ''              comment '更新者',
  update_time           datetime                                comment '更新时间',
  primary key (process_id),
  key idx_trace_process_batch_id (batch_id),
  key idx_trace_process_type (process_type)
) engine=innodb auto_increment=1 comment='酿造工序记录表';

create table if not exists trace_container_usage (
  usage_id              bigint(20)      not null auto_increment comment '容器使用记录ID',
  container_id          bigint(20)      not null                comment '容器ID',
  batch_id              bigint(20)      not null                comment '批次ID',
  start_time            datetime                                comment '入桶/入罐时间',
  end_time              datetime                                comment '出桶/出罐时间',
  fill_quantity_l       decimal(10,2)   default null            comment '装入量(升)',
  remarks               varchar(500)    default null            comment '备注',
  del_flag              char(1)         default '0'             comment '删除标志(0存在 2删除)',
  create_by             varchar(64)     default ''              comment '创建者',
  create_time           datetime                                comment '创建时间',
  update_by             varchar(64)     default ''              comment '更新者',
  update_time           datetime                                comment '更新时间',
  primary key (usage_id),
  key idx_trace_usage_container_id (container_id),
  key idx_trace_usage_batch_id (batch_id)
) engine=innodb auto_increment=1 comment='容器使用记录表';

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2040, '生产追溯', 0, 6, 'trace-production', null, '', '', 1, 0, 'M', '0', '0', '', 'form', 'admin', sysdate(), '', null, '葡萄酒生产追溯目录'
from dual
where not exists (select 1 from sys_menu where menu_id = 2040);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2041, '生产批次', 2040, 1, 'batch', 'trace/batch/index', '', '', 1, 0, 'C', '0', '0', 'trace:batch:list', 'date', 'admin', sysdate(), '', null, '生产批次管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2041);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2042, '工序记录', 2040, 2, 'process', 'trace/process/index', '', '', 1, 0, 'C', '0', '0', 'trace:process:list', 'clipboard', 'admin', sysdate(), '', null, '工序记录管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2042);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2043, '容器流转', 2040, 3, 'usage', 'trace/usage/index', '', '', 1, 0, 'C', '0', '0', 'trace:usage:list', 'shuffle', 'admin', sysdate(), '', null, '容器流转管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2043);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2044, '批次查询', 2041, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:batch:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2044);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2045, '批次新增', 2041, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:batch:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2045);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2046, '批次修改', 2041, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:batch:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2046);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2047, '批次删除', 2041, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:batch:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2047);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2048, '工序查询', 2042, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:process:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2048);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2049, '工序新增', 2042, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:process:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2049);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2050, '工序修改', 2042, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:process:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2050);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2051, '工序删除', 2042, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:process:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2051);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2052, '流转查询', 2043, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:usage:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2052);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2053, '流转新增', 2043, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:usage:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2053);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2054, '流转修改', 2043, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:usage:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2054);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2055, '流转删除', 2043, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:usage:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2055);

insert into sys_role_menu (role_id, menu_id)
select 2, t.menu_id
from (
    select 2040 as menu_id union all
    select 2041 union all
    select 2042 union all
    select 2043 union all
    select 2044 union all
    select 2045 union all
    select 2046 union all
    select 2047 union all
    select 2048 union all
    select 2049 union all
    select 2050 union all
    select 2051 union all
    select 2052 union all
    select 2053 union all
    select 2054 union all
    select 2055
) t
where not exists (
    select 1 from sys_role_menu rm where rm.role_id = 2 and rm.menu_id = t.menu_id
);
