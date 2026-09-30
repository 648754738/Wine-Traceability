set names utf8mb4;

-- 宁夏葡萄酒溯源系统：产区模拟数据
-- 仅补充产区层级，适合用于“产区管理”模块演示

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select 0, '0', '宁夏葡萄酒产区', 1, 1, '0', '0', 'admin', now(), '宁夏葡萄酒核心产区根节点', '模拟数据'
from dual
where not exists (
  select 1 from trace_region where region_name = '宁夏葡萄酒产区' and parent_id = 0 and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '贺兰山东麓', 2, 1, '0', '0', 'admin', now(), '宁夏最具代表性的葡萄酒产区带', '模拟数据'
from trace_region r
where r.region_name = '宁夏葡萄酒产区' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '贺兰山东麓' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '红寺堡产区', 2, 2, '0', '0', 'admin', now(), '吴忠市红寺堡区特色葡萄酒产区', '模拟数据'
from trace_region r
where r.region_name = '宁夏葡萄酒产区' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '红寺堡产区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '中卫产区', 2, 3, '0', '0', 'admin', now(), '宁夏中卫市特色葡萄酒产区', '模拟数据'
from trace_region r
where r.region_name = '宁夏葡萄酒产区' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '中卫产区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '镇北堡产区', 3, 1, '0', '0', 'admin', now(), '贺兰山东麓代表性子产区', '模拟数据'
from trace_region r
where r.region_name = '贺兰山东麓' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '镇北堡产区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '西夏区产区', 3, 2, '0', '0', 'admin', now(), '银川西夏区重点示范产区', '模拟数据'
from trace_region r
where r.region_name = '贺兰山东麓' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '西夏区产区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '青铜峡产区', 3, 3, '0', '0', 'admin', now(), '青铜峡鸽子山葡萄酒示范产区', '模拟数据'
from trace_region r
where r.region_name = '贺兰山东麓' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '青铜峡产区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '永宁产区', 3, 4, '0', '0', 'admin', now(), '永宁县葡萄酒示范产区', '模拟数据'
from trace_region r
where r.region_name = '贺兰山东麓' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '永宁产区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '红寺堡北部片区', 3, 1, '0', '0', 'admin', now(), '红寺堡北部示范种植片区', '模拟数据'
from trace_region r
where r.region_name = '红寺堡产区' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '红寺堡北部片区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '红寺堡中部片区', 3, 2, '0', '0', 'admin', now(), '红寺堡中部重点酿酒葡萄片区', '模拟数据'
from trace_region r
where r.region_name = '红寺堡产区' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '红寺堡中部片区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '沙坡头片区', 3, 1, '0', '0', 'admin', now(), '中卫沙坡头特色产区片区', '模拟数据'
from trace_region r
where r.region_name = '中卫产区' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '沙坡头片区' and del_flag = '0'
);

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '宣和片区', 3, 2, '0', '0', 'admin', now(), '中卫宣和镇葡萄酒片区', '模拟数据'
from trace_region r
where r.region_name = '中卫产区' and r.del_flag = '0'
and not exists (
  select 1 from trace_region where region_name = '宣和片区' and del_flag = '0'
);
