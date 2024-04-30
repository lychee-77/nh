package com.dap.system.nh.mapper;

import com.dap.system.nh.domain.vo.NhStatisticsVo;

import java.util.List;

public interface NhStatisticsMapper {
    public List<NhStatisticsVo> selectNhStatisRingRatio(NhStatisticsVo nhStatisticsVo);

}
