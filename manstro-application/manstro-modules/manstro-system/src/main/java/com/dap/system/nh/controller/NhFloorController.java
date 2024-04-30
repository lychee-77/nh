package com.dap.system.nh.controller;

import java.util.Arrays;
import java.util.List;

import com.dap.system.nh.domain.vo.NhFloorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dap.common.log.annotation.Log;
import com.dap.common.log.enums.BusinessType;
import com.dap.common.security.annotation.PreAuthorize;
import com.dap.system.nh.domain.NhFloor;
import com.dap.system.nh.service.INhFloorService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.web.page.TableDataInfo;

/**
 * 楼层Controller
 *
 * @author dap
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/nh/floor")
public class NhFloorController extends BaseController {
    @Autowired
    private INhFloorService nhFloorService;

    /**
     * 给前端显示时默认的分隔符
     */
    private static final String DEFAULT_NAME_DELIMITER = ",";


    /**
     * 查询楼层列表
     */
    @PreAuthorize(hasPermi = "system:floor:list")
    @GetMapping("/list")
    public TableDataInfo list(NhFloorVo nhFloor) {
        List<NhFloorVo> list = nhFloorService.selectNhFloorListPage(nhFloor);
        return getDataTable(list);
    }

    /**
     * 获取楼层详细信息
     */
    @PreAuthorize(hasPermi = "system:floor:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(nhFloorService.selectNhFloorById(id));
    }

    /**
     * 新增楼层
     */
    @PreAuthorize(hasPermi = "system:floor:add")
    @Log(title = "楼层", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NhFloor nhFloor) {

        // 新增重复判断
        NhFloorVo nhFloorVo = nhFloorService.existsFloorer(nhFloor);
        if (nhFloorVo != null) {
            return AjaxResult.error("你新增的楼层[" + nhFloorVo.getFloorName() + "" +
                    "]已经在" + nhFloorVo.getBuildName() + "楼宇中存在,禁止插入!");
        }

        return toAjax(nhFloorService.insertNhFloor(nhFloor));
    }

    /**
     * 修改楼层
     */
    @PreAuthorize(hasPermi = "system:floor:edit")
    @Log(title = "楼层", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NhFloor nhFloor) {
        // 当前楼层是否已经存在房屋
        List<String> roomNamesList = nhFloorService.currentFloorerIsExistsRoom(Arrays.asList(nhFloor.getId()));
        if (roomNamesList.size() > 0) {
            // List<String> 转换为String
            String appendNames = StringUtils.collectionToDelimitedString(roomNamesList, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error("你要修改的楼层下存在名称为[" + appendNames + "]的房屋了,禁止修改!");
        }



        // 修改的楼层名字重复
        NhFloorVo nhFloorVo = nhFloorService.existsFloorer(nhFloor);
        if (nhFloorVo != null) {
            return AjaxResult.error("你修改的房层[" + nhFloorVo.getFloorName() + "" +
                    "]已经在" + nhFloorVo.getBuildName() + "楼宇中存在,无法进行修改!");
        }



        return toAjax(nhFloorService.updateNhFloor(nhFloor));
    }

    /**
     * 删除楼层
     */
    @PreAuthorize(hasPermi = "system:floor:remove")
    @Log(title = "楼层", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        // 查询楼宇下是否有楼层如果有禁止删除
        List<String> nhFloorVos = nhFloorService.currentFloorerIsExistsRoom(Arrays.asList(ids));

        if(nhFloorVos.size() > 0){
            // List<String> 转换为String
            String appendNames = StringUtils.collectionToDelimitedString(nhFloorVos, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error("你要删除的楼层下存在名称为[" + appendNames + "]的房屋了,禁止删除!");
        }

        return toAjax(nhFloorService.deleteNhFloorByIds(ids));
    }

    /**
     * 构造级联框
     * @return
     */
    @PreAuthorize(hasPermi = "build:build:list")
    @GetMapping("/treeList")
    public TableDataInfo treeList(){
        return getDataTable(nhFloorService.selectNhBuildTreeList());
    }
}
