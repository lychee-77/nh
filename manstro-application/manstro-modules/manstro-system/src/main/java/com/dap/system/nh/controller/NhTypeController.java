package com.dap.system.nh.controller;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.dap.system.nh.domain.vo.NhEquipmentTypeVo;
import com.dap.system.nh.domain.vo.NhRoomVo;
import com.dap.system.nh.domain.vo.TreeNode;
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
import com.dap.system.nh.domain.NhType;
import com.dap.system.nh.service.INhTypeService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;

/**
 * 能耗类型Controller
 *
 * @author dap
 * @date 2021-12-07
 */
@RestController
@RequestMapping("/nh/type")
public class NhTypeController extends BaseController
{
    @Autowired
    private INhTypeService nhTypeService;

    /**
     * 给前端显示时默认的分隔符
     */
    private static final String DEFAULT_NAME_DELIMITER = ",";

    /**
     * 查询能耗类型列表
     */
    @PreAuthorize(hasPermi = "nh:type:list")
    @GetMapping("/list")
    public TableDataInfo list(NhType nhType)
    {
        List<NhType> list = nhTypeService.selectNhTypeListPage(nhType);
        return getDataTable(list);
    }

    /**
     * 查询能耗类型列表
     */
    @PreAuthorize(hasPermi = "nh:type:treeList")
    @GetMapping("/treeList")
    public TableDataInfo treeList()
    {
        List<TreeNode> list = nhTypeService.selectNhTypeTreeList();
        return getDataTable(list);
    }

    /**
     * 导出能耗类型列表
     */
    @PreAuthorize(hasPermi = "nh:type:export")
    @Log(title = "能耗类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NhType nhType) throws IOException
    {
        List<NhType> list = nhTypeService.selectNhTypeList(nhType);
        ExcelUtil<NhType> util = new ExcelUtil<NhType>(NhType.class);
        util.exportExcel(response, list, "type");
    }

    /**
     * 获取能耗类型详细信息
     */
    @PreAuthorize(hasPermi = "nh:type:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(nhTypeService.selectNhTypeById(id));
    }

    /**
     * 新增能耗类型
     */
    @PreAuthorize(hasPermi = "nh:type:add")
    @Log(title = "能耗类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NhType nhType)
    {

        // 新增重复判断
        NhType existsNhType = nhTypeService.existsNhType(nhType);
        if (existsNhType != null) {
            return AjaxResult.error("你新增的能耗类型[" + existsNhType.getName()+"]已经存在存在,禁止插入!");
        }
        return toAjax(nhTypeService.insertNhType(nhType));
    }

    /**
     * 修改能耗类型
     */
    @PreAuthorize(hasPermi = "nh:type:edit")
    @Log(title = "能耗类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NhType nhType)
    {
        // 当前能耗类型是否已经在统计表中出现
        List<String> nhTypeNames = nhTypeService.currentNhTypeIsExistsMeasure(Arrays.asList(nhType.getId()));
        if (nhTypeNames.size() > 0) {
            // List<String> 转换为String
            String appendNames = StringUtils.collectionToDelimitedString(nhTypeNames, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error("在能耗统计中该类型已经被注册[" + appendNames + "],禁止修改!");
        }


        // 新增重复判断
        NhType existsNhType = nhTypeService.existsNhType(nhType);
        if (existsNhType != null) {
            return AjaxResult.error("你修改后的的能耗类型[" + existsNhType.getName()+"]已经存在,禁止修改!");
        }
        return toAjax(nhTypeService.updateNhType(nhType));
    }

    /**
     * 删除能耗类型
     */
    @PreAuthorize(hasPermi = "nh:type:remove")
    @Log(title = "能耗类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 当前能耗类型是否已经在统计表中出现
        List<String> nhTypeNames = nhTypeService.currentNhTypeIsExistsMeasure(Arrays.asList(ids));
        if (nhTypeNames.size() > 0) {
            // List<String> 转换为String
            String appendNames = StringUtils.collectionToDelimitedString(nhTypeNames, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error("在能耗统计中该类型已经被注册[" + appendNames + "],禁止删除!");
        }

        return toAjax(nhTypeService.deleteNhTypeByIds(ids));
    }
}
