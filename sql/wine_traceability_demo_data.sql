set names utf8mb4;

-- 葡萄酒溯源系统中文演示数据
-- 建议在已执行第 1-6 层建表脚本后再执行本脚本

-- 1. 产区
insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select 0, '0', '宁夏葡萄酒产区', 1, 1, '0', '0', 'admin', now(), '宁夏葡萄酒核心产区', '演示数据'
from dual
where not exists (select 1 from trace_region where region_name = '宁夏葡萄酒产区' and del_flag = '0');

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '贺兰山东麓', 2, 1, '0', '0', 'admin', now(), '宁夏代表性产区', '演示数据'
from trace_region r
where r.region_name = '宁夏葡萄酒产区' and r.del_flag = '0'
and not exists (select 1 from trace_region where region_name = '贺兰山东麓' and del_flag = '0');

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '镇北堡产区', 3, 1, '0', '0', 'admin', now(), '志辉源石所在示例产区', '演示数据'
from trace_region r
where r.region_name = '贺兰山东麓' and r.del_flag = '0'
and not exists (select 1 from trace_region where region_name = '镇北堡产区' and del_flag = '0');

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '西夏区产区', 3, 2, '0', '0', 'admin', now(), '皇蔻酒庄所在示例产区', '演示数据'
from trace_region r
where r.region_name = '贺兰山东麓' and r.del_flag = '0'
and not exists (select 1 from trace_region where region_name = '西夏区产区' and del_flag = '0');

insert into trace_region (parent_id, ancestors, region_name, region_level, order_num, status, del_flag, create_by, create_time, description, remark)
select r.region_id, concat(r.ancestors, ',', r.region_id), '青铜峡产区', 3, 3, '0', '0', 'admin', now(), '银色高地所在示例产区', '演示数据'
from trace_region r
where r.region_name = '贺兰山东麓' and r.del_flag = '0'
and not exists (select 1 from trace_region where region_name = '青铜峡产区' and del_flag = '0');

-- 2. 酒庄
insert into trace_enterprise (region_id, enterprise_name, license_no, contact, phone, address, status, del_flag, create_by, create_time, remark)
select r.region_id, '志辉源石酒庄', 'NX-ZHYS-2025-001', '张莉', '13895010001', '宁夏银川市西夏区镇北堡镇', '0', '0', 'admin', now(), '演示酒庄'
from trace_region r
where r.region_name = '镇北堡产区' and r.del_flag = '0'
and not exists (select 1 from trace_enterprise where enterprise_name = '志辉源石酒庄' and del_flag = '0');

insert into trace_enterprise (region_id, enterprise_name, license_no, contact, phone, address, status, del_flag, create_by, create_time, remark)
select r.region_id, '皇蔻酒庄', 'NX-HK-2025-001', '王璟', '13895010002', '宁夏银川市西夏区贺兰山路', '0', '0', 'admin', now(), '演示酒庄'
from trace_region r
where r.region_name = '西夏区产区' and r.del_flag = '0'
and not exists (select 1 from trace_enterprise where enterprise_name = '皇蔻酒庄' and del_flag = '0');

insert into trace_enterprise (region_id, enterprise_name, license_no, contact, phone, address, status, del_flag, create_by, create_time, remark)
select r.region_id, '银色高地酒庄', 'NX-YSGD-2025-001', '刘芳', '13895010003', '宁夏吴忠市青铜峡市鸽子山', '0', '0', 'admin', now(), '演示酒庄'
from trace_region r
where r.region_name = '青铜峡产区' and r.del_flag = '0'
and not exists (select 1 from trace_enterprise where enterprise_name = '银色高地酒庄' and del_flag = '0');

-- 3. 地块
insert into trace_vineyard_plot (enterprise_id, plot_name, plot_code, area_ha, soil_type, elevation_m, gps_coordinates, planted_variety, vine_age, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '西区3号田', 'YSH-PLOT-003', 8.50, '砾石土', 1150, '38.5412,106.1201', '赤霞珠', 15, '0', '0', 'admin', now(), '演示地块'
from trace_enterprise e
where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_vineyard_plot where plot_code = 'YSH-PLOT-003' and del_flag = '0');

insert into trace_vineyard_plot (enterprise_id, plot_name, plot_code, area_ha, soil_type, elevation_m, gps_coordinates, planted_variety, vine_age, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '东区1号田', 'YSH-PLOT-001', 6.20, '砂砾土', 1138, '38.5448,106.1189', '美乐', 12, '0', '0', 'admin', now(), '演示地块'
from trace_enterprise e
where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_vineyard_plot where plot_code = 'YSH-PLOT-001' and del_flag = '0');

insert into trace_vineyard_plot (enterprise_id, plot_name, plot_code, area_ha, soil_type, elevation_m, gps_coordinates, planted_variety, vine_age, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '皇蔻北麓1号地', 'HK-PLOT-001', 10.00, '风积砂土', 1165, '38.5055,106.0811', '马瑟兰', 10, '0', '0', 'admin', now(), '演示地块'
from trace_enterprise e
where e.enterprise_name = '皇蔻酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_vineyard_plot where plot_code = 'HK-PLOT-001' and del_flag = '0');

insert into trace_vineyard_plot (enterprise_id, plot_name, plot_code, area_ha, soil_type, elevation_m, gps_coordinates, planted_variety, vine_age, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '青铜峡南坡园', 'YSGD-PLOT-001', 7.80, '砂壤土', 1128, '38.0123,106.0688', '赤霞珠', 11, '0', '0', 'admin', now(), '演示地块'
from trace_enterprise e
where e.enterprise_name = '银色高地酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_vineyard_plot where plot_code = 'YSGD-PLOT-001' and del_flag = '0');

-- 4. 品种
insert into trace_grape_variety (variety_name, english_name, status, del_flag, create_by, create_time, remark)
select '赤霞珠', 'Cabernet Sauvignon', '0', '0', 'admin', now(), '演示品种' from dual
where not exists (select 1 from trace_grape_variety where variety_name = '赤霞珠' and del_flag = '0');
insert into trace_grape_variety (variety_name, english_name, status, del_flag, create_by, create_time, remark)
select '美乐', 'Merlot', '0', '0', 'admin', now(), '演示品种' from dual
where not exists (select 1 from trace_grape_variety where variety_name = '美乐' and del_flag = '0');
insert into trace_grape_variety (variety_name, english_name, status, del_flag, create_by, create_time, remark)
select '品丽珠', 'Cabernet Franc', '0', '0', 'admin', now(), '演示品种' from dual
where not exists (select 1 from trace_grape_variety where variety_name = '品丽珠' and del_flag = '0');
insert into trace_grape_variety (variety_name, english_name, status, del_flag, create_by, create_time, remark)
select '马瑟兰', 'Marselan', '0', '0', 'admin', now(), '演示品种' from dual
where not exists (select 1 from trace_grape_variety where variety_name = '马瑟兰' and del_flag = '0');
insert into trace_grape_variety (variety_name, english_name, status, del_flag, create_by, create_time, remark)
select '西拉', 'Syrah', '0', '0', 'admin', now(), '演示品种' from dual
where not exists (select 1 from trace_grape_variety where variety_name = '西拉' and del_flag = '0');

-- 5. 产品
insert into trace_product (enterprise_id, product_name, category, alcohol_content, net_content, shelf_life, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '志辉源石·山之子干红', '干红', 13.50, '750ml', 120, '0', '0', 'admin', now(), '演示产品'
from trace_enterprise e where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_product where product_name = '志辉源石·山之子干红' and del_flag = '0');

insert into trace_product (enterprise_id, product_name, category, alcohol_content, net_content, shelf_life, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '志辉源石·贺兰之光干红', '干红', 13.20, '750ml', 120, '0', '0', 'admin', now(), '演示产品'
from trace_enterprise e where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_product where product_name = '志辉源石·贺兰之光干红' and del_flag = '0');

insert into trace_product (enterprise_id, product_name, category, alcohol_content, net_content, shelf_life, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '皇蔻·北麓珍藏干红', '干红', 13.80, '750ml', 120, '0', '0', 'admin', now(), '演示产品'
from trace_enterprise e where e.enterprise_name = '皇蔻酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_product where product_name = '皇蔻·北麓珍藏干红' and del_flag = '0');

insert into trace_product (enterprise_id, product_name, category, alcohol_content, net_content, shelf_life, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '银色高地·家族珍藏干红', '干红', 13.60, '750ml', 120, '0', '0', 'admin', now(), '演示产品'
from trace_enterprise e where e.enterprise_name = '银色高地酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_product where product_name = '银色高地·家族珍藏干红' and del_flag = '0');

-- 6. 产品品种关联
insert into trace_product_variety (product_id, variety_id, percentage)
select p.product_id, v.variety_id, 70.00
from trace_product p, trace_grape_variety v
where p.product_name = '志辉源石·山之子干红' and v.variety_name = '赤霞珠'
and not exists (select 1 from trace_product_variety where product_id = p.product_id and variety_id = v.variety_id);
insert into trace_product_variety (product_id, variety_id, percentage)
select p.product_id, v.variety_id, 30.00
from trace_product p, trace_grape_variety v
where p.product_name = '志辉源石·山之子干红' and v.variety_name = '美乐'
and not exists (select 1 from trace_product_variety where product_id = p.product_id and variety_id = v.variety_id);
insert into trace_product_variety (product_id, variety_id, percentage)
select p.product_id, v.variety_id, 60.00
from trace_product p, trace_grape_variety v
where p.product_name = '志辉源石·贺兰之光干红' and v.variety_name = '赤霞珠'
and not exists (select 1 from trace_product_variety where product_id = p.product_id and variety_id = v.variety_id);
insert into trace_product_variety (product_id, variety_id, percentage)
select p.product_id, v.variety_id, 40.00
from trace_product p, trace_grape_variety v
where p.product_name = '志辉源石·贺兰之光干红' and v.variety_name = '品丽珠'
and not exists (select 1 from trace_product_variety where product_id = p.product_id and variety_id = v.variety_id);
insert into trace_product_variety (product_id, variety_id, percentage)
select p.product_id, v.variety_id, 85.00
from trace_product p, trace_grape_variety v
where p.product_name = '皇蔻·北麓珍藏干红' and v.variety_name = '马瑟兰'
and not exists (select 1 from trace_product_variety where product_id = p.product_id and variety_id = v.variety_id);
insert into trace_product_variety (product_id, variety_id, percentage)
select p.product_id, v.variety_id, 15.00
from trace_product p, trace_grape_variety v
where p.product_name = '皇蔻·北麓珍藏干红' and v.variety_name = '西拉'
and not exists (select 1 from trace_product_variety where product_id = p.product_id and variety_id = v.variety_id);
insert into trace_product_variety (product_id, variety_id, percentage)
select p.product_id, v.variety_id, 80.00
from trace_product p, trace_grape_variety v
where p.product_name = '银色高地·家族珍藏干红' and v.variety_name = '赤霞珠'
and not exists (select 1 from trace_product_variety where product_id = p.product_id and variety_id = v.variety_id);
insert into trace_product_variety (product_id, variety_id, percentage)
select p.product_id, v.variety_id, 20.00
from trace_product p, trace_grape_variety v
where p.product_name = '银色高地·家族珍藏干红' and v.variety_name = '美乐'
and not exists (select 1 from trace_product_variety where product_id = p.product_id and variety_id = v.variety_id);
-- 7. 车间
insert into trace_workshop (enterprise_id, workshop_name, location, supervisor, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '发酵车间A线', '镇北堡厂区A栋', '李建国', '0', '0', 'admin', now(), '演示车间'
from trace_enterprise e where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_workshop where workshop_name = '发酵车间A线' and enterprise_id = e.enterprise_id and del_flag = '0');
insert into trace_workshop (enterprise_id, workshop_name, location, supervisor, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '灌装车间B线', '镇北堡厂区B栋', '马瑞', '0', '0', 'admin', now(), '演示车间'
from trace_enterprise e where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_workshop where workshop_name = '灌装车间B线' and enterprise_id = e.enterprise_id and del_flag = '0');
insert into trace_workshop (enterprise_id, workshop_name, location, supervisor, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '皇蔻发酵车间', '西夏区主厂房', '王宁', '0', '0', 'admin', now(), '演示车间'
from trace_enterprise e where e.enterprise_name = '皇蔻酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_workshop where workshop_name = '皇蔻发酵车间' and enterprise_id = e.enterprise_id and del_flag = '0');
insert into trace_workshop (enterprise_id, workshop_name, location, supervisor, status, del_flag, create_by, create_time, remark)
select e.enterprise_id, '银色高地发酵车间', '青铜峡厂区一号车间', '赵晨', '0', '0', 'admin', now(), '演示车间'
from trace_enterprise e where e.enterprise_name = '银色高地酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_workshop where workshop_name = '银色高地发酵车间' and enterprise_id = e.enterprise_id and del_flag = '0');

-- 8. 容器
insert into trace_container (enterprise_id, workshop_id, container_type, container_code, capacity_l, location_detail, status, purchase_date, last_toasted, del_flag, create_by, create_time, remark)
select e.enterprise_id, w.workshop_id, '橡木桶', 'B-03-12', 225.00, '3区12排2层', '使用中', '2022-03-15', '2025-03-01', '0', 'admin', now(), '演示容器'
from trace_enterprise e
join trace_workshop w on w.enterprise_id = e.enterprise_id and w.workshop_name = '发酵车间A线' and w.del_flag = '0'
where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_container where container_code = 'B-03-12' and del_flag = '0');
insert into trace_container (enterprise_id, workshop_id, container_type, container_code, capacity_l, location_detail, status, purchase_date, last_toasted, del_flag, create_by, create_time, remark)
select e.enterprise_id, w.workshop_id, '橡木桶', 'B-03-13', 225.00, '3区13排2层', '使用中', '2022-03-15', '2025-03-01', '0', 'admin', now(), '演示容器'
from trace_enterprise e
join trace_workshop w on w.enterprise_id = e.enterprise_id and w.workshop_name = '发酵车间A线' and w.del_flag = '0'
where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_container where container_code = 'B-03-13' and del_flag = '0');
insert into trace_container (enterprise_id, workshop_id, container_type, container_code, capacity_l, location_detail, status, purchase_date, last_toasted, del_flag, create_by, create_time, remark)
select e.enterprise_id, w.workshop_id, '不锈钢罐', 'T-01-01', 5000.00, 'A栋1区1号罐', '空闲', '2021-08-01', null, '0', 'admin', now(), '演示容器'
from trace_enterprise e
join trace_workshop w on w.enterprise_id = e.enterprise_id and w.workshop_name = '发酵车间A线' and w.del_flag = '0'
where e.enterprise_name = '志辉源石酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_container where container_code = 'T-01-01' and del_flag = '0');
insert into trace_container (enterprise_id, workshop_id, container_type, container_code, capacity_l, location_detail, status, purchase_date, last_toasted, del_flag, create_by, create_time, remark)
select e.enterprise_id, w.workshop_id, '橡木桶', 'HK-OAK-01', 225.00, '皇蔻酒窖A排1号', '使用中', '2023-01-12', '2025-02-20', '0', 'admin', now(), '演示容器'
from trace_enterprise e
join trace_workshop w on w.enterprise_id = e.enterprise_id and w.workshop_name = '皇蔻发酵车间' and w.del_flag = '0'
where e.enterprise_name = '皇蔻酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_container where container_code = 'HK-OAK-01' and del_flag = '0');
insert into trace_container (enterprise_id, workshop_id, container_type, container_code, capacity_l, location_detail, status, purchase_date, last_toasted, del_flag, create_by, create_time, remark)
select e.enterprise_id, w.workshop_id, '不锈钢罐', 'HK-TANK-01', 3000.00, '皇蔻罐区1号', '空闲', '2022-11-10', null, '0', 'admin', now(), '演示容器'
from trace_enterprise e
join trace_workshop w on w.enterprise_id = e.enterprise_id and w.workshop_name = '皇蔻发酵车间' and w.del_flag = '0'
where e.enterprise_name = '皇蔻酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_container where container_code = 'HK-TANK-01' and del_flag = '0');
insert into trace_container (enterprise_id, workshop_id, container_type, container_code, capacity_l, location_detail, status, purchase_date, last_toasted, del_flag, create_by, create_time, remark)
select e.enterprise_id, w.workshop_id, '橡木桶', 'YSGD-OAK-01', 225.00, '青铜峡酒窖1排1号', '空闲', '2023-04-08', '2025-01-15', '0', 'admin', now(), '演示容器'
from trace_enterprise e
join trace_workshop w on w.enterprise_id = e.enterprise_id and w.workshop_name = '银色高地发酵车间' and w.del_flag = '0'
where e.enterprise_name = '银色高地酒庄' and e.del_flag = '0'
and not exists (select 1 from trace_container where container_code = 'YSGD-OAK-01' and del_flag = '0');

-- 9. 生产批次
insert into trace_batch (batch_no, product_id, plot_id, workshop_id, entry_workshop_time, harvest_date, harvest_location, total_yield_l, status, del_flag, create_by, create_time, remark)
select 'YSH-2025-001', p.product_id, vp.plot_id, w.workshop_id, '2025-09-15 08:30:00', '2025-09-15', '西区3号田', 4200.00, '0', '0', 'admin', now(), '核心演示批次'
from trace_product p
join trace_vineyard_plot vp on vp.plot_code = 'YSH-PLOT-003' and vp.del_flag = '0'
join trace_workshop w on w.workshop_name = '发酵车间A线' and w.del_flag = '0'
where p.product_name = '志辉源石·山之子干红' and p.del_flag = '0'
and not exists (select 1 from trace_batch where batch_no = 'YSH-2025-001' and del_flag = '0');
insert into trace_batch (batch_no, product_id, plot_id, workshop_id, entry_workshop_time, harvest_date, harvest_location, total_yield_l, status, del_flag, create_by, create_time, remark)
select 'YSH-2025-002', p.product_id, vp.plot_id, w.workshop_id, '2025-09-18 09:10:00', '2025-09-18', '东区1号田', 3600.00, '0', '0', 'admin', now(), '核心演示批次'
from trace_product p
join trace_vineyard_plot vp on vp.plot_code = 'YSH-PLOT-001' and vp.del_flag = '0'
join trace_workshop w on w.workshop_name = '发酵车间A线' and w.del_flag = '0'
where p.product_name = '志辉源石·贺兰之光干红' and p.del_flag = '0'
and not exists (select 1 from trace_batch where batch_no = 'YSH-2025-002' and del_flag = '0');
insert into trace_batch (batch_no, product_id, plot_id, workshop_id, entry_workshop_time, harvest_date, harvest_location, total_yield_l, status, del_flag, create_by, create_time, remark)
select 'HK-2025-001', p.product_id, vp.plot_id, w.workshop_id, '2025-09-20 07:50:00', '2025-09-20', '皇蔻北麓1号地', 3900.00, '0', '0', 'admin', now(), '核心演示批次'
from trace_product p
join trace_vineyard_plot vp on vp.plot_code = 'HK-PLOT-001' and vp.del_flag = '0'
join trace_workshop w on w.workshop_name = '皇蔻发酵车间' and w.del_flag = '0'
where p.product_name = '皇蔻·北麓珍藏干红' and p.del_flag = '0'
and not exists (select 1 from trace_batch where batch_no = 'HK-2025-001' and del_flag = '0');
insert into trace_batch (batch_no, product_id, plot_id, workshop_id, entry_workshop_time, harvest_date, harvest_location, total_yield_l, status, del_flag, create_by, create_time, remark)
select 'YSGD-2025-001', p.product_id, vp.plot_id, w.workshop_id, '2025-09-22 10:00:00', '2025-09-22', '青铜峡南坡园', 3100.00, '0', '0', 'admin', now(), '核心演示批次'
from trace_product p
join trace_vineyard_plot vp on vp.plot_code = 'YSGD-PLOT-001' and vp.del_flag = '0'
join trace_workshop w on w.workshop_name = '银色高地发酵车间' and w.del_flag = '0'
where p.product_name = '银色高地·家族珍藏干红' and p.del_flag = '0'
and not exists (select 1 from trace_batch where batch_no = 'YSGD-2025-001' and del_flag = '0');

-- 10. 工序记录
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '破碎除梗', '2025-09-15 09:00:00', '2025-09-15 11:00:00', '18-20℃', '李强', '葡萄入厂后完成破碎除梗', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSH-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '破碎除梗');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '酒精发酵', '2025-09-15 12:00:00', '2025-09-28 18:00:00', '24-28℃', '李强', '控温酒精发酵', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSH-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '酒精发酵');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '橡木桶陈酿', '2025-10-02 09:00:00', '2026-03-30 17:00:00', '14-18℃', '马瑞', '进入法国产橡木桶陈酿180天', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSH-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '橡木桶陈酿');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '破碎除梗', '2025-09-18 10:00:00', '2025-09-18 12:00:00', '18-20℃', '韩梅', '葡萄原料预处理', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSH-2025-002' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '破碎除梗');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '酒精发酵', '2025-09-18 13:00:00', '2025-09-30 16:00:00', '23-27℃', '韩梅', '发酵周期12天', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSH-2025-002' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '酒精发酵');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '短期陈酿', '2025-10-01 08:00:00', '2025-12-15 17:00:00', '16-18℃', '韩梅', '短期桶陈及澄清稳定', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSH-2025-002' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '短期陈酿');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '破碎除梗', '2025-09-20 08:00:00', '2025-09-20 10:00:00', '18-20℃', '王宁', '皇蔻批次预处理', '0', 'admin', now() from trace_batch b where b.batch_no = 'HK-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '破碎除梗');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '酒精发酵', '2025-09-20 11:00:00', '2025-10-03 17:00:00', '24-26℃', '王宁', '使用不锈钢罐发酵', '0', 'admin', now() from trace_batch b where b.batch_no = 'HK-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '酒精发酵');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '橡木桶陈酿', '2025-10-05 08:30:00', '2026-02-28 16:30:00', '14-17℃', '王宁', '桶陈145天', '0', 'admin', now() from trace_batch b where b.batch_no = 'HK-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '橡木桶陈酿');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '破碎除梗', '2025-09-22 10:30:00', '2025-09-22 12:00:00', '18-20℃', '赵晨', '银色高地原料预处理', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSGD-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '破碎除梗');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '酒精发酵', '2025-09-22 13:00:00', '2025-10-05 14:00:00', '22-26℃', '赵晨', '不锈钢控温发酵', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSGD-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '酒精发酵');
insert into trace_process_record (batch_id, process_type, start_time, end_time, temp_control, operator_name, remarks, del_flag, create_by, create_time)
select b.batch_id, '静置澄清', '2025-10-06 09:00:00', '2025-10-20 17:00:00', '15-18℃', '赵晨', '发酵后静置澄清', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSGD-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_process_record where batch_id = b.batch_id and process_type = '静置澄清');
-- 11. 容器流转
insert into trace_container_usage (container_id, batch_id, start_time, end_time, fill_quantity_l, remarks, del_flag, create_by, create_time)
select c.container_id, b.batch_id, '2025-09-15 12:00:00', '2025-10-02 08:30:00', 4200.00, '先入不锈钢罐发酵', '0', 'admin', now() from trace_container c, trace_batch b where c.container_code = 'T-01-01' and c.del_flag = '0' and b.batch_no = 'YSH-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_container_usage where container_id = c.container_id and batch_id = b.batch_id and start_time = '2025-09-15 12:00:00');
insert into trace_container_usage (container_id, batch_id, start_time, end_time, fill_quantity_l, remarks, del_flag, create_by, create_time)
select c.container_id, b.batch_id, '2025-10-02 09:00:00', '2026-03-30 17:00:00', 225.00, '转入橡木桶陈酿', '0', 'admin', now() from trace_container c, trace_batch b where c.container_code = 'B-03-12' and c.del_flag = '0' and b.batch_no = 'YSH-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_container_usage where container_id = c.container_id and batch_id = b.batch_id and start_time = '2025-10-02 09:00:00');
insert into trace_container_usage (container_id, batch_id, start_time, end_time, fill_quantity_l, remarks, del_flag, create_by, create_time)
select c.container_id, b.batch_id, '2025-10-01 08:00:00', '2025-12-15 17:00:00', 225.00, '短期橡木桶陈酿', '0', 'admin', now() from trace_container c, trace_batch b where c.container_code = 'B-03-13' and c.del_flag = '0' and b.batch_no = 'YSH-2025-002' and b.del_flag = '0' and not exists (select 1 from trace_container_usage where container_id = c.container_id and batch_id = b.batch_id and start_time = '2025-10-01 08:00:00');
insert into trace_container_usage (container_id, batch_id, start_time, end_time, fill_quantity_l, remarks, del_flag, create_by, create_time)
select c.container_id, b.batch_id, '2025-09-20 11:00:00', '2025-10-05 08:00:00', 3900.00, '不锈钢罐发酵', '0', 'admin', now() from trace_container c, trace_batch b where c.container_code = 'HK-TANK-01' and c.del_flag = '0' and b.batch_no = 'HK-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_container_usage where container_id = c.container_id and batch_id = b.batch_id and start_time = '2025-09-20 11:00:00');
insert into trace_container_usage (container_id, batch_id, start_time, end_time, fill_quantity_l, remarks, del_flag, create_by, create_time)
select c.container_id, b.batch_id, '2025-10-05 08:30:00', '2026-02-28 16:30:00', 225.00, '橡木桶陈酿', '0', 'admin', now() from trace_container c, trace_batch b where c.container_code = 'HK-OAK-01' and c.del_flag = '0' and b.batch_no = 'HK-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_container_usage where container_id = c.container_id and batch_id = b.batch_id and start_time = '2025-10-05 08:30:00');
insert into trace_container_usage (container_id, batch_id, start_time, end_time, fill_quantity_l, remarks, del_flag, create_by, create_time)
select c.container_id, b.batch_id, '2025-10-21 09:00:00', '2026-01-20 16:00:00', 225.00, '家族珍藏桶陈', '0', 'admin', now() from trace_container c, trace_batch b where c.container_code = 'YSGD-OAK-01' and c.del_flag = '0' and b.batch_no = 'YSGD-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_container_usage where container_id = c.container_id and batch_id = b.batch_id and start_time = '2025-10-21 09:00:00');

-- 12. 质检报告
insert into trace_quality_report (batch_id, inspection_date, inspector, alcohol, total_sugar, volatile_acid, sulfite, is_qualified, report_url, remarks, del_flag, create_by, create_time)
select b.batch_id, '2026-03-31', '宁夏葡萄酒质量检测中心', 13.50, 2.10, 0.42, 85.00, '1', '/profile/report/YSH-2025-001.pdf', '批次检测合格', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSH-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_quality_report where batch_id = b.batch_id and inspection_date = '2026-03-31');
insert into trace_quality_report (batch_id, inspection_date, inspector, alcohol, total_sugar, volatile_acid, sulfite, is_qualified, report_url, remarks, del_flag, create_by, create_time)
select b.batch_id, '2025-12-18', '宁夏葡萄酒质量检测中心', 13.20, 3.00, 0.39, 78.00, '1', '/profile/report/YSH-2025-002.pdf', '批次检测合格', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSH-2025-002' and b.del_flag = '0' and not exists (select 1 from trace_quality_report where batch_id = b.batch_id and inspection_date = '2025-12-18');
insert into trace_quality_report (batch_id, inspection_date, inspector, alcohol, total_sugar, volatile_acid, sulfite, is_qualified, report_url, remarks, del_flag, create_by, create_time)
select b.batch_id, '2026-03-02', '宁夏葡萄酒质量检测中心', 13.80, 1.80, 0.41, 82.00, '1', '/profile/report/HK-2025-001.pdf', '批次检测合格', '0', 'admin', now() from trace_batch b where b.batch_no = 'HK-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_quality_report where batch_id = b.batch_id and inspection_date = '2026-03-02');
insert into trace_quality_report (batch_id, inspection_date, inspector, alcohol, total_sugar, volatile_acid, sulfite, is_qualified, report_url, remarks, del_flag, create_by, create_time)
select b.batch_id, '2026-01-25', '宁夏葡萄酒质量检测中心', 13.60, 2.40, 0.38, 80.00, '1', '/profile/report/YSGD-2025-001.pdf', '批次检测合格', '0', 'admin', now() from trace_batch b where b.batch_no = 'YSGD-2025-001' and b.del_flag = '0' and not exists (select 1 from trace_quality_report where batch_id = b.batch_id and inspection_date = '2026-01-25');

-- 13. 灌装信息
insert into trace_bottling_info (batch_id, source_container_id, trace_code_prefix, trace_code_start, trace_code_end, bottling_date, bottle_spec, cap_code, qr_code_url, remarks, del_flag, create_by, create_time)
select b.batch_id, c.container_id, 'YSH', 100001, 101200, '2026-04-01 09:00:00', '750ml波尔多瓶', 'CAP-YSH-01', '/profile/qrcode/YSH-100001-101200.png', '山之子干红灌装批次', '0', 'admin', now() from trace_batch b, trace_container c where b.batch_no = 'YSH-2025-001' and b.del_flag = '0' and c.container_code = 'B-03-12' and c.del_flag = '0' and not exists (select 1 from trace_bottling_info where batch_id = b.batch_id and trace_code_prefix = 'YSH' and trace_code_start = 100001 and del_flag = '0');
insert into trace_bottling_info (batch_id, source_container_id, trace_code_prefix, trace_code_start, trace_code_end, bottling_date, bottle_spec, cap_code, qr_code_url, remarks, del_flag, create_by, create_time)
select b.batch_id, c.container_id, 'YSH', 101201, 102000, '2025-12-20 10:00:00', '750ml波尔多瓶', 'CAP-YSH-02', '/profile/qrcode/YSH-101201-102000.png', '贺兰之光干红灌装批次', '0', 'admin', now() from trace_batch b, trace_container c where b.batch_no = 'YSH-2025-002' and b.del_flag = '0' and c.container_code = 'B-03-13' and c.del_flag = '0' and not exists (select 1 from trace_bottling_info where batch_id = b.batch_id and trace_code_prefix = 'YSH' and trace_code_start = 101201 and del_flag = '0');
insert into trace_bottling_info (batch_id, source_container_id, trace_code_prefix, trace_code_start, trace_code_end, bottling_date, bottle_spec, cap_code, qr_code_url, remarks, del_flag, create_by, create_time)
select b.batch_id, c.container_id, 'HK', 200001, 200900, '2026-03-05 14:00:00', '750ml勃艮第瓶', 'CAP-HK-01', '/profile/qrcode/HK-200001-200900.png', '皇蔻珍藏干红灌装批次', '0', 'admin', now() from trace_batch b, trace_container c where b.batch_no = 'HK-2025-001' and b.del_flag = '0' and c.container_code = 'HK-OAK-01' and c.del_flag = '0' and not exists (select 1 from trace_bottling_info where batch_id = b.batch_id and trace_code_prefix = 'HK' and trace_code_start = 200001 and del_flag = '0');
insert into trace_bottling_info (batch_id, source_container_id, trace_code_prefix, trace_code_start, trace_code_end, bottling_date, bottle_spec, cap_code, qr_code_url, remarks, del_flag, create_by, create_time)
select b.batch_id, c.container_id, 'YSGD', 300001, 300600, '2026-01-28 15:00:00', '750ml波尔多瓶', 'CAP-YSGD-01', '/profile/qrcode/YSGD-300001-300600.png', '银色高地家族珍藏灌装批次', '0', 'admin', now() from trace_batch b, trace_container c where b.batch_no = 'YSGD-2025-001' and b.del_flag = '0' and c.container_code = 'YSGD-OAK-01' and c.del_flag = '0' and not exists (select 1 from trace_bottling_info where batch_id = b.batch_id and trace_code_prefix = 'YSGD' and trace_code_start = 300001 and del_flag = '0');
-- 14. 仓储单据与明细
insert into trace_warehouse_bill (bill_type, bill_no, operator_id, operate_time, remark, del_flag, create_by, create_time)
select 'IN', 'RK-20260401-001', 1, '2026-04-01 17:00:00', '山之子干红成品入库', '0', 'admin', now() from dual where not exists (select 1 from trace_warehouse_bill where bill_no = 'RK-20260401-001' and del_flag = '0');
insert into trace_warehouse_bill (bill_type, bill_no, operator_id, operate_time, remark, del_flag, create_by, create_time)
select 'IN', 'RK-20251220-001', 1, '2025-12-20 18:00:00', '贺兰之光干红成品入库', '0', 'admin', now() from dual where not exists (select 1 from trace_warehouse_bill where bill_no = 'RK-20251220-001' and del_flag = '0');
insert into trace_warehouse_bill (bill_type, bill_no, operator_id, operate_time, remark, del_flag, create_by, create_time)
select 'IN', 'RK-20260305-001', 1, '2026-03-05 19:00:00', '皇蔻珍藏干红成品入库', '0', 'admin', now() from dual where not exists (select 1 from trace_warehouse_bill where bill_no = 'RK-20260305-001' and del_flag = '0');
insert into trace_warehouse_bill (bill_type, bill_no, operator_id, operate_time, remark, del_flag, create_by, create_time)
select 'IN', 'RK-20260128-001', 1, '2026-01-28 18:00:00', '银色高地家族珍藏入库', '0', 'admin', now() from dual where not exists (select 1 from trace_warehouse_bill where bill_no = 'RK-20260128-001' and del_flag = '0');
insert into trace_warehouse_bill (bill_type, bill_no, operator_id, operate_time, remark, del_flag, create_by, create_time)
select 'OUT', 'CK-20260410-001', 1, '2026-04-10 10:00:00', '发往上海经销商', '0', 'admin', now() from dual where not exists (select 1 from trace_warehouse_bill where bill_no = 'CK-20260410-001' and del_flag = '0');
insert into trace_warehouse_bill (bill_type, bill_no, operator_id, operate_time, remark, del_flag, create_by, create_time)
select 'OUT', 'CK-20260312-001', 1, '2026-03-12 09:30:00', '发往北京渠道客户', '0', 'admin', now() from dual where not exists (select 1 from trace_warehouse_bill where bill_no = 'CK-20260312-001' and del_flag = '0');

insert into trace_warehouse_bill_detail (bill_id, bottling_id, quantity, start_trace_code, end_trace_code, del_flag, create_by, create_time)
select wb.bill_id, bi.bottling_id, 1200, 'YSH100001', 'YSH101200', '0', 'admin', now() from trace_warehouse_bill wb, trace_bottling_info bi where wb.bill_no = 'RK-20260401-001' and bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and not exists (select 1 from trace_warehouse_bill_detail where bill_id = wb.bill_id and bottling_id = bi.bottling_id and del_flag = '0');
insert into trace_warehouse_bill_detail (bill_id, bottling_id, quantity, start_trace_code, end_trace_code, del_flag, create_by, create_time)
select wb.bill_id, bi.bottling_id, 800, 'YSH101201', 'YSH102000', '0', 'admin', now() from trace_warehouse_bill wb, trace_bottling_info bi where wb.bill_no = 'RK-20251220-001' and bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 101201 and not exists (select 1 from trace_warehouse_bill_detail where bill_id = wb.bill_id and bottling_id = bi.bottling_id and del_flag = '0');
insert into trace_warehouse_bill_detail (bill_id, bottling_id, quantity, start_trace_code, end_trace_code, del_flag, create_by, create_time)
select wb.bill_id, bi.bottling_id, 900, 'HK200001', 'HK200900', '0', 'admin', now() from trace_warehouse_bill wb, trace_bottling_info bi where wb.bill_no = 'RK-20260305-001' and bi.trace_code_prefix = 'HK' and bi.trace_code_start = 200001 and not exists (select 1 from trace_warehouse_bill_detail where bill_id = wb.bill_id and bottling_id = bi.bottling_id and del_flag = '0');
insert into trace_warehouse_bill_detail (bill_id, bottling_id, quantity, start_trace_code, end_trace_code, del_flag, create_by, create_time)
select wb.bill_id, bi.bottling_id, 600, 'YSGD300001', 'YSGD300600', '0', 'admin', now() from trace_warehouse_bill wb, trace_bottling_info bi where wb.bill_no = 'RK-20260128-001' and bi.trace_code_prefix = 'YSGD' and bi.trace_code_start = 300001 and not exists (select 1 from trace_warehouse_bill_detail where bill_id = wb.bill_id and bottling_id = bi.bottling_id and del_flag = '0');
insert into trace_warehouse_bill_detail (bill_id, bottling_id, quantity, start_trace_code, end_trace_code, del_flag, create_by, create_time)
select wb.bill_id, bi.bottling_id, 300, 'YSH100001', 'YSH100300', '0', 'admin', now() from trace_warehouse_bill wb, trace_bottling_info bi where wb.bill_no = 'CK-20260410-001' and bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and not exists (select 1 from trace_warehouse_bill_detail where bill_id = wb.bill_id and bottling_id = bi.bottling_id and start_trace_code = 'YSH100001' and end_trace_code = 'YSH100300' and del_flag = '0');
insert into trace_warehouse_bill_detail (bill_id, bottling_id, quantity, start_trace_code, end_trace_code, del_flag, create_by, create_time)
select wb.bill_id, bi.bottling_id, 240, 'HK200001', 'HK200240', '0', 'admin', now() from trace_warehouse_bill wb, trace_bottling_info bi where wb.bill_no = 'CK-20260312-001' and bi.trace_code_prefix = 'HK' and bi.trace_code_start = 200001 and not exists (select 1 from trace_warehouse_bill_detail where bill_id = wb.bill_id and bottling_id = bi.bottling_id and start_trace_code = 'HK200001' and end_trace_code = 'HK200240' and del_flag = '0');

-- 15. 物流
insert into trace_logistics (out_bill_id, carrier, tracking_no, consignee, destination, ship_date, remark, del_flag, create_by, create_time)
select wb.bill_id, '顺丰冷链', 'SF202604100001', '上海星醇酒业', '上海市浦东新区', '2026-04-10 13:00:00', '演示物流单', '0', 'admin', now() from trace_warehouse_bill wb where wb.bill_no = 'CK-20260410-001' and wb.del_flag = '0' and not exists (select 1 from trace_logistics where out_bill_id = wb.bill_id and del_flag = '0');
insert into trace_logistics (out_bill_id, carrier, tracking_no, consignee, destination, ship_date, remark, del_flag, create_by, create_time)
select wb.bill_id, '京东物流', 'JD202603120001', '北京臻选商贸', '北京市朝阳区', '2026-03-12 12:00:00', '演示物流单', '0', 'admin', now() from trace_warehouse_bill wb where wb.bill_no = 'CK-20260312-001' and wb.del_flag = '0' and not exists (select 1 from trace_logistics where out_bill_id = wb.bill_id and del_flag = '0');

-- 16. 消费者反馈（12条）
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSH100001', bi.bottling_id, 5, '酒体饱满，果香和橡木香很协调。', '0', '1', '感谢您的支持，期待再次品鉴。', '0', '消费者A', '2026-04-11 09:00:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSH100001' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSH100028', bi.bottling_id, 4, '入口顺滑，醒酒二十分钟后层次更明显。', '0', '0', null, '0', '消费者B', '2026-04-11 10:15:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSH100028' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSH100105', bi.bottling_id, 5, '配餐表现很好，和牛排搭配很合适。', '0', '0', null, '0', '消费者C', '2026-04-11 13:20:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSH100105' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSH101250', bi.bottling_id, 4, '酒标设计很漂亮，适合送礼。', '0', '1', '感谢反馈，欢迎继续关注新品。', '0', '消费者D', '2026-01-05 08:40:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 101201 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSH101250' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSH101666', bi.bottling_id, 3, '整体还不错，但希望物流外箱再结实一点。', '0', '0', null, '0', '消费者E', '2026-01-06 14:05:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 101201 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSH101666' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'HK200001', bi.bottling_id, 5, '开瓶香气非常集中，回味很长。', '0', '1', '感谢您的认可。', '0', '消费者F', '2026-03-15 11:00:00' from trace_bottling_info bi where bi.trace_code_prefix = 'HK' and bi.trace_code_start = 200001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'HK200001' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'HK200088', bi.bottling_id, 2, '二维码有轻微磨损，但还能扫出来。', '1', '0', null, '0', '消费者G', '2026-03-15 16:25:00' from trace_bottling_info bi where bi.trace_code_prefix = 'HK' and bi.trace_code_start = 200001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'HK200088' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'HK200199', bi.bottling_id, 4, '酒体扎实，单宁成熟。', '0', '0', null, '0', '消费者H', '2026-03-16 09:15:00' from trace_bottling_info bi where bi.trace_code_prefix = 'HK' and bi.trace_code_start = 200001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'HK200199' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSGD300001', bi.bottling_id, 5, '香气优雅，结构平衡，很有宁夏风格。', '0', '0', null, '0', '消费者I', '2026-02-02 10:10:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSGD' and bi.trace_code_start = 300001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSGD300001' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSGD300166', bi.bottling_id, 4, '酸度清爽，适合搭配烤肉。', '0', '0', null, '0', '消费者J', '2026-02-03 15:45:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSGD' and bi.trace_code_start = 300001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSGD300166' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSGD300288', bi.bottling_id, 4, '包装质感不错，送朋友很体面。', '0', '1', '感谢您的喜欢。', '0', '消费者K', '2026-02-04 18:20:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSGD' and bi.trace_code_start = 300001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSGD300288' and del_flag = '0');
insert into trace_feedback (trace_code, bottling_id, rating, content, is_suspicious, status, reply_content, del_flag, create_by, create_time)
select 'YSH100210', bi.bottling_id, 1, '怀疑瓶身标签有二次粘贴痕迹，希望核查。', '1', '0', null, '0', '消费者L', '2026-04-12 09:35:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_feedback where trace_code = 'YSH100210' and del_flag = '0');

-- 17. 查询日志
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSH100001', bi.bottling_id, bi.batch_id, '志辉源石酒庄', '志辉源石·山之子干红', 'PUBLIC', '1', '2026-04-11 08:50:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSH100001' and query_time = '2026-04-11 08:50:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSH100001', bi.bottling_id, bi.batch_id, '志辉源石酒庄', '志辉源石·山之子干红', 'PUBLIC', '1', '2026-04-11 09:01:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSH100001' and query_time = '2026-04-11 09:01:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSH100028', bi.bottling_id, bi.batch_id, '志辉源石酒庄', '志辉源石·山之子干红', 'PUBLIC', '1', '2026-04-11 10:10:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSH100028' and query_time = '2026-04-11 10:10:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSH100105', bi.bottling_id, bi.batch_id, '志辉源石酒庄', '志辉源石·山之子干红', 'ADMIN', '1', '2026-04-11 13:05:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSH100105' and query_time = '2026-04-11 13:05:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSH101250', bi.bottling_id, bi.batch_id, '志辉源石酒庄', '志辉源石·贺兰之光干红', 'PUBLIC', '1', '2026-01-05 08:30:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 101201 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSH101250' and query_time = '2026-01-05 08:30:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSH101666', bi.bottling_id, bi.batch_id, '志辉源石酒庄', '志辉源石·贺兰之光干红', 'PUBLIC', '1', '2026-01-06 13:50:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 101201 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSH101666' and query_time = '2026-01-06 13:50:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'HK200001', bi.bottling_id, bi.batch_id, '皇蔻酒庄', '皇蔻·北麓珍藏干红', 'PUBLIC', '1', '2026-03-15 10:45:00' from trace_bottling_info bi where bi.trace_code_prefix = 'HK' and bi.trace_code_start = 200001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'HK200001' and query_time = '2026-03-15 10:45:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'HK200088', bi.bottling_id, bi.batch_id, '皇蔻酒庄', '皇蔻·北麓珍藏干红', 'PUBLIC', '1', '2026-03-15 16:10:00' from trace_bottling_info bi where bi.trace_code_prefix = 'HK' and bi.trace_code_start = 200001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'HK200088' and query_time = '2026-03-15 16:10:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'HK200199', bi.bottling_id, bi.batch_id, '皇蔻酒庄', '皇蔻·北麓珍藏干红', 'ADMIN', '1', '2026-03-16 09:00:00' from trace_bottling_info bi where bi.trace_code_prefix = 'HK' and bi.trace_code_start = 200001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'HK200199' and query_time = '2026-03-16 09:00:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSGD300001', bi.bottling_id, bi.batch_id, '银色高地酒庄', '银色高地·家族珍藏干红', 'PUBLIC', '1', '2026-02-02 10:00:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSGD' and bi.trace_code_start = 300001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSGD300001' and query_time = '2026-02-02 10:00:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSGD300166', bi.bottling_id, bi.batch_id, '银色高地酒庄', '银色高地·家族珍藏干红', 'PUBLIC', '1', '2026-02-03 15:30:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSGD' and bi.trace_code_start = 300001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSGD300166' and query_time = '2026-02-03 15:30:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSGD300288', bi.bottling_id, bi.batch_id, '银色高地酒庄', '银色高地·家族珍藏干红', 'ADMIN', '1', '2026-02-04 18:00:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSGD' and bi.trace_code_start = 300001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSGD300288' and query_time = '2026-02-04 18:00:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSH100210', bi.bottling_id, bi.batch_id, '志辉源石酒庄', '志辉源石·山之子干红', 'PUBLIC', '1', '2026-04-12 09:20:00' from trace_bottling_info bi where bi.trace_code_prefix = 'YSH' and bi.trace_code_start = 100001 and bi.del_flag = '0' and not exists (select 1 from trace_query_log where trace_code = 'YSH100210' and query_time = '2026-04-12 09:20:00');
insert into trace_query_log (trace_code, bottling_id, batch_id, enterprise_name, product_name, query_source, query_status, query_time)
select 'YSH199999', null, null, null, null, 'PUBLIC', '0', '2026-04-12 09:25:00' from dual where not exists (select 1 from trace_query_log where trace_code = 'YSH199999' and query_time = '2026-04-12 09:25:00');