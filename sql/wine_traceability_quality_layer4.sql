create table if not exists trace_quality_report (
  report_id              bigint(20)      not null auto_increment comment '质检报告ID',
  batch_id               bigint(20)      not null                comment '批次ID',
  inspection_date        date                                     comment '检测日期',
  inspector              varchar(100)    default null            comment '检测机构/人员',
  alcohol                decimal(5,2)    default null            comment '酒精度',
  total_sugar            decimal(10,2)   default null            comment '总糖',
  volatile_acid          decimal(10,2)   default null            comment '挥发酸',
  sulfite                decimal(10,2)   default null            comment '二氧化硫',
  is_qualified           char(1)         default '1'             comment '是否合格(0不合格 1合格)',
  report_url             varchar(255)    default null            comment '报告附件地址',
  remarks                varchar(500)    default null            comment '备注',
  del_flag               char(1)         default '0'             comment '删除标志(0存在 2删除)',
  create_by              varchar(64)     default ''              comment '创建者',
  create_time            datetime                                comment '创建时间',
  update_by              varchar(64)     default ''              comment '更新者',
  update_time            datetime                                comment '更新时间',
  primary key (report_id),
  key idx_trace_quality_batch_id (batch_id),
  key idx_trace_quality_inspection_date (inspection_date)
) engine=innodb auto_increment=1 comment='质检报告表';

create table if not exists trace_bottling_info (
  bottling_id            bigint(20)      not null auto_increment comment '灌装ID',
  batch_id               bigint(20)      not null                comment '批次ID',
  source_container_id    bigint(20)      default null            comment '来源容器ID',
  trace_code_prefix      varchar(32)     not null                comment '溯源码前缀',
  trace_code_start       bigint(20)      not null                comment '起始序号',
  trace_code_end         bigint(20)      not null                comment '结束序号',
  bottling_date          datetime                                comment '灌装日期',
  bottle_spec            varchar(100)    default null            comment '瓶型规格',
  cap_code               varchar(100)    default null            comment '瓶盖码',
  qr_code_url            varchar(255)    default null            comment '二维码图片地址',
  remarks                varchar(500)    default null            comment '备注',
  del_flag               char(1)         default '0'             comment '删除标志(0存在 2删除)',
  create_by              varchar(64)     default ''              comment '创建者',
  create_time            datetime                                comment '创建时间',
  update_by              varchar(64)     default ''              comment '更新者',
  update_time            datetime                                comment '更新时间',
  primary key (bottling_id),
  key idx_trace_bottling_batch_id (batch_id),
  key idx_trace_bottling_container_id (source_container_id),
  key idx_trace_bottling_prefix (trace_code_prefix),
  unique key uk_trace_bottling_range (trace_code_prefix, trace_code_start, trace_code_end, del_flag)
) engine=innodb auto_increment=1 comment='灌装信息表';

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2060, '质检灌装', 0, 7, 'trace-quality', null, '', '', 1, 0, 'M', '0', '0', '', 'edit', 'admin', sysdate(), '', null, '葡萄酒质检与灌装目录'
from dual
where not exists (select 1 from sys_menu where menu_id = 2060);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2061, '质检报告', 2060, 1, 'quality', 'trace/quality/index', '', '', 1, 0, 'C', '0', '0', 'trace:quality:list', 'documentation', 'admin', sysdate(), '', null, '质检报告管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2061);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2062, '灌装信息', 2060, 2, 'bottling', 'trace/bottling/index', '', '', 1, 0, 'C', '0', '0', 'trace:bottling:list', 'example', 'admin', sysdate(), '', null, '灌装信息管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2062);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2063, '质检查询', 2061, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:quality:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2063);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2064, '质检新增', 2061, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:quality:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2064);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2065, '质检修改', 2061, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:quality:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2065);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2066, '质检删除', 2061, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:quality:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2066);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2067, '灌装查询', 2062, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:bottling:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2067);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2068, '灌装新增', 2062, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:bottling:add', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2068);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2069, '灌装修改', 2062, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:bottling:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2069);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2070, '灌装删除', 2062, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:bottling:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2070);

insert into sys_role_menu (role_id, menu_id)
select 2, t.menu_id
from (
    select 2060 as menu_id union all
    select 2061 union all
    select 2062 union all
    select 2063 union all
    select 2064 union all
    select 2065 union all
    select 2066 union all
    select 2067 union all
    select 2068 union all
    select 2069 union all
    select 2070
) t
where not exists (
    select 1 from sys_role_menu rm where rm.role_id = 2 and rm.menu_id = t.menu_id
);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2071, '二维码管理', 2060, 3, 'qrcode', 'trace/qr/index', '', '', 1, 0, 'C', '0', '0', 'trace:bottling:list', 'guide', 'admin', sysdate(), '', null, '二维码生成与预览管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2071);

insert into sys_role_menu (role_id, menu_id)
select 2, 2071
from dual
where not exists (
    select 1 from sys_role_menu rm where rm.role_id = 2 and rm.menu_id = 2071
);
