package com.wine.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wine.common.constant.UserConstants;
import com.wine.common.core.text.Convert;
import com.wine.common.exception.ServiceException;
import com.wine.common.utils.StringUtils;
import com.wine.system.domain.TraceRegion;
import com.wine.system.mapper.TraceRegionMapper;
import com.wine.system.service.ITraceRegionService;

@Service
public class TraceRegionServiceImpl implements ITraceRegionService
{
    @Autowired
    private TraceRegionMapper regionMapper;

    @Override
    public List<TraceRegion> selectRegionList(TraceRegion region)
    {
        return regionMapper.selectRegionList(region);
    }

    @Override
    public List<TraceRegion> buildRegionTree(List<TraceRegion> regions)
    {
        List<TraceRegion> returnList = new ArrayList<>();
        List<Long> ids = regions.stream().map(TraceRegion::getRegionId).collect(Collectors.toList());
        for (TraceRegion region : regions)
        {
            if (!ids.contains(region.getParentId()))
            {
                recursionFn(regions, region);
                returnList.add(region);
            }
        }
        return returnList.isEmpty() ? regions : returnList;
    }

    private void recursionFn(List<TraceRegion> list, TraceRegion region)
    {
        List<TraceRegion> childList = getChildList(list, region);
        region.setChildren(childList);
        for (TraceRegion child : childList)
        {
            if (hasChild(list, child))
            {
                recursionFn(list, child);
            }
        }
    }

    private List<TraceRegion> getChildList(List<TraceRegion> list, TraceRegion region)
    {
        List<TraceRegion> childList = new ArrayList<>();
        for (TraceRegion item : list)
        {
            if (StringUtils.isNotNull(item.getParentId()) && item.getParentId().longValue() == region.getRegionId().longValue())
            {
                childList.add(item);
            }
        }
        return childList;
    }

    private boolean hasChild(List<TraceRegion> list, TraceRegion region)
    {
        return !getChildList(list, region).isEmpty();
    }

    @Override
    public TraceRegion selectRegionById(Long regionId)
    {
        return regionMapper.selectRegionById(regionId);
    }

    @Override
    public boolean checkRegionNameUnique(TraceRegion region)
    {
        Long regionId = StringUtils.isNull(region.getRegionId()) ? -1L : region.getRegionId();
        TraceRegion info = regionMapper.checkRegionNameUnique(region.getRegionName(), region.getParentId());
        if (StringUtils.isNotNull(info) && info.getRegionId().longValue() != regionId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean hasChildByRegionId(Long regionId)
    {
        return regionMapper.hasChildByRegionId(regionId) > 0;
    }

    @Override
    public boolean checkRegionExistEnterprise(Long regionId)
    {
        return regionMapper.checkRegionExistEnterprise(regionId) > 0;
    }

    @Override
    public int selectNormalChildrenRegionById(Long regionId)
    {
        return regionMapper.selectNormalChildrenRegionById(regionId);
    }

    @Override
    public int insertRegion(TraceRegion region)
    {
        Long parentId = StringUtils.isNull(region.getParentId()) ? 0L : region.getParentId();
        region.setParentId(parentId);
        if (parentId == 0L)
        {
            region.setAncestors("0");
        }
        else
        {
            TraceRegion parent = regionMapper.selectRegionById(parentId);
            if (StringUtils.isNull(parent))
            {
                throw new ServiceException("上级产区不存在");
            }
            if (!UserConstants.NORMAL.equals(parent.getStatus()))
            {
                throw new ServiceException("上级产区已停用，不能新增下级产区");
            }
            region.setAncestors(parent.getAncestors() + "," + parentId);
        }
        region.setRegionLevel(calcLevel(region.getAncestors()));
        return regionMapper.insertRegion(region);
    }

    @Override
    public int updateRegion(TraceRegion region)
    {
        Long parentId = StringUtils.isNull(region.getParentId()) ? 0L : region.getParentId();
        region.setParentId(parentId);
        TraceRegion oldRegion = regionMapper.selectRegionById(region.getRegionId());
        if (StringUtils.isNull(oldRegion))
        {
            throw new ServiceException("产区不存在");
        }

        String newAncestors;
        if (parentId == 0L)
        {
            newAncestors = "0";
        }
        else
        {
            TraceRegion newParent = regionMapper.selectRegionById(parentId);
            if (StringUtils.isNull(newParent))
            {
                throw new ServiceException("上级产区不存在");
            }
            if (!UserConstants.NORMAL.equals(newParent.getStatus()))
            {
                throw new ServiceException("上级产区已停用，不能挂接到该产区下");
            }
            newAncestors = newParent.getAncestors() + "," + newParent.getRegionId();
        }

        String oldAncestors = oldRegion.getAncestors();
        region.setAncestors(newAncestors);
        region.setRegionLevel(calcLevel(newAncestors));
        updateRegionChildren(region.getRegionId(), newAncestors, oldAncestors);
        int rows = regionMapper.updateRegion(region);
        if (UserConstants.NORMAL.equals(region.getStatus()) && StringUtils.isNotEmpty(region.getAncestors())
            && !"0".equals(region.getAncestors()))
        {
            updateParentRegionStatusNormal(region);
        }
        return rows;
    }

    private void updateRegionChildren(Long regionId, String newAncestors, String oldAncestors)
    {
        List<TraceRegion> children = regionMapper.selectChildrenRegionById(regionId);
        for (TraceRegion child : children)
        {
            child.setAncestors(StringUtils.replace(child.getAncestors(), oldAncestors, newAncestors));
            child.setRegionLevel(calcLevel(child.getAncestors()));
        }
        if (!children.isEmpty())
        {
            regionMapper.updateRegionChildren(children);
        }
    }

    private void updateParentRegionStatusNormal(TraceRegion region)
    {
        Long[] regionIds = Convert.toLongArray(region.getAncestors());
        regionMapper.updateRegionStatusNormal(regionIds);
    }

    private Integer calcLevel(String ancestors)
    {
        return StringUtils.isEmpty(ancestors) ? 1 : ancestors.split(",").length;
    }

    @Override
    public int deleteRegionById(Long regionId)
    {
        return regionMapper.deleteRegionById(regionId);
    }
}
