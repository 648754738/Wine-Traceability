-- 葡萄酒溯源系统第二层主数据
-- 包含：葡萄品种、产品、产品品种关联、酿酒车间、容器

create table if not exists trace_grape_variety (
  variety_id         bigint(20)      not null auto_increment comment '品种ID',
  variety_name       varchar(100)    not null                comment '品种名称',
  english_name       varchar(100)    default null            comment '英文名称',
  status             char(1)         default '0'             comment '状态（0正常 1停用）',
  del_flag           char(1)         default '0'             comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''              comment '创建者',
  create_time        datetime                                comment '创建时间',
  update_by          varchar(64)     default ''              comment '更新者',
  update_time        datetime                                comment '更新时间',
  remark             varchar(500)    default null            comment '备注',
  primary key (variety_id),
  key idx_trace_variety_status (status),
  unique key uk_trace_variety_name (variety_name, del_flag)
) engine=innodb auto_increment=1 comment='葡萄品种表';

create table if not exists trace_product (
  product_id         bigint(20)      not null auto_increment comment '产品ID',
  enterprise_id      bigint(20)      not null                comment '酒庄ID',
  product_name       varchar(100)    not null                comment '产品名称',
  category           varchar(50)     default null            comment '产品类别',
  alcohol_content    decimal(5,2)    default null            comment '酒精度',
  net_content        varchar(50)     default null            comment '净含量',
  shelf_life         int(4)          default null            comment '保质期（月）',
  status             char(1)         default '0'             comment '状态（0正常 1停用）',
  del_flag           char(1)         default '0'             comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''              comment '创建者',
  create_time        datetime                                comment '创建时间',
  update_by          varchar(64)     default ''              comment '更新者',
  update_time        datetime                                comment '更新时间',
  remark             varchar(500)    default null            comment '备注',
  primary key (product_id),
  key idx_trace_product_enterprise_id (enterprise_id),
  key idx_trace_product_status (status),
  unique key uk_trace_product_name (enterprise_id, product_name, del_flag)
) engine=innodb auto_increment=1 comment='产品表';

create table if not exists trace_product_variety (
  id                 bigint(20)      not null auto_increment comment '主键ID',
  product_id         bigint(20)      not null                comment '产品ID',
  variety_id         bigint(20)      not null                comment '品种ID',
  percentage         decimal(5,2)    default null            comment '占比',
  primary key (id),
  key idx_trace_product_variety_product_id (product_id),
  key idx_trace_product_variety_variety_id (variety_id),
  unique key uk_trace_product_variety (product_id, variety_id)
) engine=innodb auto_increment=1 comment='产品品种关联表';

create table if not exists trace_workshop (
  workshop_id        bigint(20)      not null auto_increment comment '车间ID',
  enterprise_id      bigint(20)      not null                comment '酒庄ID',
  workshop_name      varchar(100)    not null                comment '车间名称',
  location           varchar(255)    default null            comment '位置描述',
  supervisor         varchar(50)     default null            comment '负责人',
  status             char(1)         default '0'             comment '状态（0正常 1停用）',
  del_flag           char(1)         default '0'             comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''              comment '创建者',
  create_time        datetime                                comment '创建时间',
  update_by          varchar(64)     default ''              comment '更新者',
  update_time        datetime                                comment '更新时间',
  remark             varchar(500)    default null            comment '备注',
  primary key (workshop_id),
  key idx_trace_workshop_enterprise_id (enterprise_id),
  key idx_trace_workshop_status (status),
  unique key uk_trace_workshop_name (enterprise_id, workshop_name, del_flag)
) engine=innodb auto_increment=1 comment='酿酒车间表';

create table if not exists trace_container (
  container_id       bigint(20)      not null auto_increment comment '容器ID',
  enterprise_id      bigint(20)      not null                comment '酒庄ID',
  workshop_id        bigint(20)      default null            comment '车间ID',
  container_type     varchar(30)     not null                comment '容器类型',
  container_code     varchar(100)    not null                comment '容器编码',
  capacity_l         decimal(10,2)   default null            comment '容积（升）',
  location_detail    varchar(255)    default null            comment '详细位置',
  status             varchar(30)     default 'idle'          comment '容器状态',
  purchase_date      date                                     comment '购入日期',
  last_toasted       date                                     comment '最近烘烤日期',
  del_flag           char(1)         default '0'             comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''              comment '创建者',
  create_time        datetime                                comment '创建时间',
  update_by          varchar(64)     default ''              comment '更新者',
  update_time        datetime                                comment '更新时间',
  remark             varchar(500)    default null            comment '备注',
  primary key (container_id),
  key idx_trace_container_enterprise_id (enterprise_id),
  key idx_trace_container_workshop_id (workshop_id),
  key idx_trace_container_status (status),
  unique key uk_trace_container_code (container_code, del_flag)
) engine=innodb auto_increment=1 comment='容器表';

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2016, '品种管理', 2000, 4, 'variety', 'trace/variety/index', '', '', 1, 0, 'C', '0', '0', 'trace:variety:list', 'dict', 'admin', sysdate(), '', null, '葡萄品种管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2016);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2017, '产品管理', 2000, 5, 'product', 'trace/product/index', '', '', 1, 0, 'C', '0', '0', 'trace:product:list', 'list', 'admin', sysdate(), '', null, '产品管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2017);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2018, '车间管理', 2000, 6, 'workshop', 'trace/workshop/index', '', '', 1, 0, 'C', '0', '0', 'trace:workshop:list', 'build', 'admin', sysdate(), '', null, '车间管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2018);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2019, '容器管理', 2000, 7, 'container', 'trace/container/index', '', '', 1, 0, 'C', '0', '0', 'trace:container:list', 'guide', 'admin', sysdate(), '', null, '容器管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2019);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2020, '品种查询', 2016, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:variety:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2020);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2021, '品种新增', 2016, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:variety:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2021);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2022, '品种修改', 2016, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:variety:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2022);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2023, '品种删除', 2016, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:variety:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2023);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2024, '产品查询', 2017, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:product:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2024);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2025, '产品新增', 2017, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:product:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2025);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2026, '产品修改', 2017, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:product:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2026);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2027, '产品删除', 2017, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:product:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2027);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2028, '车间查询', 2018, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:workshop:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2028);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2029, '车间新增', 2018, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:workshop:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2029);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2030, '车间修改', 2018, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:workshop:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2030);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2031, '车间删除', 2018, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:workshop:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2031);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2032, '容器查询', 2019, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:container:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2032);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2033, '容器新增', 2019, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:container:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2033);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2034, '容器修改', 2019, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:container:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2034);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2035, '容器删除', 2019, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:container:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2035);

insert into sys_role_menu (role_id, menu_id)
select 2, t.menu_id
from (
    select 2016 as menu_id union all
    select 2017 union all
    select 2018 union all
    select 2019 union all
    select 2020 union all
    select 2021 union all
    select 2022 union all
    select 2023 union all
    select 2024 union all
    select 2025 union all
    select 2026 union all
    select 2027 union all
    select 2028 union all
    select 2029 union all
    select 2030 union all
    select 2031 union all
    select 2032 union all
    select 2033 union all
    select 2034 union all
    select 2035
) t
where not exists (
    select 1 from sys_role_menu rm where rm.role_id = 2 and rm.menu_id = t.menu_id
);
