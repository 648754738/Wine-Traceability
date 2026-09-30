create table if not exists trace_feedback (
  feedback_id              bigint(20)      not null auto_increment comment '反馈ID',
  trace_code               varchar(64)     not null                comment '溯源码',
  bottling_id              bigint(20)      not null                comment '灌装ID',
  rating                   int(11)         default null            comment '评分(1-5)',
  content                  varchar(500)    default null            comment '反馈内容',
  is_suspicious            char(1)         default '0'             comment '是否可疑(0否 1是)',
  status                   char(1)         default '0'             comment '处理状态(0待处理 1已处理)',
  reply_content            varchar(500)    default null            comment '回复内容',
  del_flag                 char(1)         default '0'             comment '删除标志',
  create_by                varchar(64)     default ''              comment '创建者',
  create_time              datetime                                comment '创建时间',
  update_by                varchar(64)     default ''              comment '更新者',
  update_time              datetime                                comment '更新时间',
  primary key (feedback_id),
  key idx_trace_feedback_trace_code (trace_code),
  key idx_trace_feedback_bottling_id (bottling_id),
  key idx_trace_feedback_status (status),
  key idx_trace_feedback_suspicious (is_suspicious)
) engine=innodb auto_increment=1 comment='消费者反馈表';

create table if not exists trace_query_log (
  log_id                   bigint(20)      not null auto_increment comment '查询日志ID',
  trace_code               varchar(64)     not null                comment '溯源码',
  bottling_id              bigint(20)      default null            comment '灌装ID',
  batch_id                 bigint(20)      default null            comment '批次ID',
  enterprise_name          varchar(100)    default null            comment '酒庄名称',
  product_name             varchar(100)    default null            comment '产品名称',
  query_source             varchar(16)     not null                comment '查询来源(PUBLIC/ADMIN)',
  query_status             char(1)         default '1'             comment '查询状态(0失败 1成功)',
  query_time               datetime                                comment '查询时间',
  primary key (log_id),
  key idx_trace_query_log_trace_code (trace_code),
  key idx_trace_query_log_bottling_id (bottling_id),
  key idx_trace_query_log_query_time (query_time),
  key idx_trace_query_log_source (query_source)
) engine=innodb auto_increment=1 comment='溯源查询日志表';

update sys_menu set menu_name = '反馈监控', remark = '反馈与统计监控目录' where menu_id = 2100;
update sys_menu set menu_name = '消费者反馈', remark = '消费者反馈管理菜单' where menu_id = 2101;
update sys_menu set menu_name = '统计看板', remark = '统计看板菜单' where menu_id = 2102;
update sys_menu set menu_name = '反馈查询' where menu_id = 2103;
update sys_menu set menu_name = '反馈处理' where menu_id = 2104;
update sys_menu set menu_name = '反馈删除' where menu_id = 2105;
update sys_menu set menu_name = '看板查看' where menu_id = 2106;

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2100, '反馈监控', 0, 9, 'trace-monitor', null, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', sysdate(), '', null, '反馈与统计监控目录'
from dual
where not exists (select 1 from sys_menu where menu_id = 2100);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2101, '消费者反馈', 2100, 1, 'feedback', 'trace/feedback/index', '', '', 1, 0, 'C', '0', '0', 'trace:feedback:list', 'message', 'admin', sysdate(), '', null, '消费者反馈管理菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2101);

insert into sys_menu
  (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select
  2102, '统计看板', 2100, 2, 'stats', 'trace/stats/index', '', '', 1, 0, 'C', '0', '0', 'trace:stats:view', 'data-analysis', 'admin', sysdate(), '', null, '统计看板菜单'
from dual
where not exists (select 1 from sys_menu where menu_id = 2102);

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2103, '反馈查询', 2101, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:feedback:query', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2103);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2104, '反馈处理', 2101, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:feedback:edit', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2104);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2105, '反馈删除', 2101, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:feedback:remove', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2105);
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
select 2106, '看板查看', 2102, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'trace:stats:view', '#', 'admin', sysdate(), '', null, '' from dual
where not exists (select 1 from sys_menu where menu_id = 2106);

insert into sys_role_menu (role_id, menu_id)
select 2, t.menu_id
from (
    select 2100 as menu_id union all
    select 2101 union all
    select 2102 union all
    select 2103 union all
    select 2104 union all
    select 2105 union all
    select 2106
) t
where not exists (
    select 1 from sys_role_menu rm where rm.role_id = 2 and rm.menu_id = t.menu_id
);