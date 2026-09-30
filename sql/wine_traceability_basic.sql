-- 葡萄酒溯源系统基础主数据
-- 包含：产区、酒庄、地块，以及后台菜单权限初始化

create table if not exists trace_region (
  region_id          bigint(20)      not null auto_increment comment '产区ID',
  parent_id          bigint(20)      default 0               comment '父产区ID',
  ancestors          varchar(255)    default '0'             comment '祖级列表',
  region_name        varchar(100)    not null                comment '产区名称',
  region_level       int(4)          default 1               comment '层级',
  order_num          int(4)          default 0               comment '显示顺序',
  status             char(1)         default '0'             comment '状态（0正常 1停用）',
  del_flag           char(1)         default '0'             comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''              comment '创建者',
  create_time        datetime                                comment '创建时间',
  update_by          varchar(64)     default ''              comment '更新者',
  update_time        datetime                                comment '更新时间',
  description        varchar(500)    default null            comment '描述',
  remark             varchar(500)    default null            comment '备注',
  primary key (region_id),
  key idx_trace_region_parent_id (parent_id),
  key idx_trace_region_status (status),
  unique key uk_trace_region_name (parent_id, region_name, del_flag)
) engine=innodb auto_increment=1 comment='产区信息表';

create table if not exists trace_enterprise (
  enterprise_id      bigint(20)      not null auto_increment comment '酒庄ID',
  region_id          bigint(20)      not null                comment '所属产区ID',
  enterprise_name    varchar(100)    not null                comment '酒庄名称',
  license_no         varchar(100)    default null            comment '营业执照号',
  contact            varchar(50)     default null            comment '联系人',
  phone              varchar(20)     default null            comment '联系电话',
  address            varchar(255)    default null            comment '地址',
  status             char(1)         default '0'             comment '状态（0正常 1停用）',
  del_flag           char(1)         default '0'             comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''              comment '创建者',
  create_time        datetime                                comment '创建时间',
  update_by          varchar(64)     default ''              comment '更新者',
  update_time        datetime                                comment '更新时间',
  remark             varchar(500)    default null            comment '备注',
  primary key (enterprise_id),
  key idx_trace_enterprise_region_id (region_id),
  key idx_trace_enterprise_status (status),
  unique key uk_trace_enterprise_name (enterprise_name, del_flag),
  unique key uk_trace_enterprise_license_no (license_no, del_flag)
) engine=innodb auto_increment=1 comment='酒庄信息表';

create table if not exists trace_vineyard_plot (
  plot_id            bigint(20)      not null auto_increment comment '地块ID',
  enterprise_id      bigint(20)      not null                comment '酒庄ID',
  plot_name          varchar(100)    not null                comment '地块名称',
  plot_code          varchar(100)    not null                comment '地块编码',
  area_ha            decimal(10,2)   default null            comment '面积（公顷）',
  soil_type          varchar(100)    default null            comment '土壤类型',
  elevation_m        decimal(10,2)   default null            comment '海拔（米）',
  gps_coordinates    varchar(255)    default null            comment 'GPS坐标',
  planted_variety    varchar(100)    default null            comment '主要品种',
  vine_age           int(4)          default null            comment '平均树龄',
  status             char(1)         default '0'             comment '状态（0正常 1停用）',
  del_flag           char(1)         default '0'             comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''              comment '创建者',
  create_time        datetime                                comment '创建时间',
  update_by          varchar(64)     default ''              comment '更新者',
  update_time        datetime                                comment '更新时间',
  remark             varchar(500)    default null            comment '备注',
  primary key (plot_id),
  key idx_trace_plot_enterprise_id (enterprise_id),
  key idx_trace_plot_status (status),
  unique key uk_trace_plot_code (plot_code, del_flag)
) engine=innodb auto_increment=1 comment='葡萄园地块表';

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2000, '溯源基础', 0, 5, 'trace', null, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', sysdate(), '', null, '葡萄酒溯源基础信息目录'
from dual
where not exists (select 1 from sys_menu where menu_id = 2000);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2001, '产区管理', 2000, 1, 'region', 'trace/region/index', '', '', 1, 0, 'C', '0', '0', 'trace:region:list', 'tree-table', 'admin', sysdate(), '', null, '产区管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2001);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2002, '酒庄管理', 2000, 2, 'enterprise', 'trace/enterprise/index', '', '', 1, 0, 'C', '0', '0', 'trace:enterprise:list', 'peoples', 'admin', sysdate(), '', null, '酒庄管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2002);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2003, '地块管理', 2000, 3, 'plot', 'trace/plot/index', '', '', 1, 0, 'C', '0', '0', 'trace:plot:list', 'guide', 'admin', sysdate(), '', null, '地块管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2003);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2004, '产区查询', 2001, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:region:query', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2004);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2005, '产区新增', 2001, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:region:add', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2005);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2006, '产区修改', 2001, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:region:edit', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2006);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2007, '产区删除', 2001, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:region:remove', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2007);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2008, '酒庄查询', 2002, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:enterprise:query', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2008);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2009, '酒庄新增', 2002, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:enterprise:add', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2009);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2010, '酒庄修改', 2002, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:enterprise:edit', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2010);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2011, '酒庄删除', 2002, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:enterprise:remove', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2011);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2012, '地块查询', 2003, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:plot:query', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2012);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2013, '地块新增', 2003, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:plot:add', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2013);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2014, '地块修改', 2003, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:plot:edit', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2014);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2015, '地块删除', 2003, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:plot:remove', '#', 'admin', sysdate(), '', null, ''
from dual
where not exists (select 1 from sys_menu where menu_id = 2015);

insert into sys_role_menu (role_id, menu_id)
select 2, t.menu_id
from (
    select 2000 as menu_id union all
    select 2001 union all
    select 2002 union all
    select 2003 union all
    select 2004 union all
    select 2005 union all
    select 2006 union all
    select 2007 union all
    select 2008 union all
    select 2009 union all
    select 2010 union all
    select 2011 union all
    select 2012 union all
    select 2013 union all
    select 2014 union all
    select 2015
) t
where not exists (
    select 1 from sys_role_menu rm where rm.role_id = 2 and rm.menu_id = t.menu_id
);
