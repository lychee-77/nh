package com.dap.system.nh.controller;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.dap.system.nh.domain.NhEquipBaseinfo;
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
import com.dap.system.nh.domain.NhEquipType;
import com.dap.system.nh.service.INhEquipTypeService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;

/**
 * 设备类型Controller
 *
 * @author dap
 * @date 2021-12-04
 */
@RestController
@RequestMapping("/nh/equipType")
public class NhEquipTypeController extends BaseController
{
    @Autowired
    private INhEquipTypeService nhEquipTypeService;

    /**
     * 给前端显示时默认的分隔符
     */
    private static final String DEFAULT_NAME_DELIMITER = ",";

    /**
     * 查询设备类型列表
     */
    @PreAuthorize(hasPermi = "nh:type:list")
    @GetMapping("/list")
    public TableDataInfo list(NhEquipType nhEquipType)
    {
        List<NhEquipType> list = nhEquipTypeService.selectNhEquipTypeListPage(nhEquipType);
        return getDataTable(list);
    }

    /**
     * 查询设备类型列表
     */
    @PreAuthorize(hasPermi = "nh:type:list")
    @GetMapping("/treeList")
    public TableDataInfo treeList()
    {
        List<TreeNode> list = nhEquipTypeService.selectNhEquipTypeTreeList();
        return getDataTable(list);
    }

    /**
     * 导出设备类型列表
     */
    @PreAuthorize(hasPermi = "nh:type:export")
    @Log(title = "设备类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NhEquipType nhEquipType) throws IOException
    {
        List<NhEquipType> list = nhEquipTypeService.selectNhEquipTypeList(nhEquipType);
        ExcelUtil<NhEquipType> util = new ExcelUtil<NhEquipType>(NhEquipType.class);
        util.exportExcel(response, list, "type");
    }

    /**
     * 获取设备类型详细信息
     */
    @PreAuthorize(hasPermi = "nh:type:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(nhEquipTypeService.selectNhEquipTypeById(id));
    }

    /**
     * 新增设备类型
     */
    @PreAuthorize(hasPermi = "nh:type:add")
    @Log(title = "设备类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NhEquipType nhEquipType)
    {
       // 新增重复判断
        NhEquipType existsNhEquipType = nhEquipTypeService.existsEquipType(nhEquipType);
        if (existsNhEquipType != null) {
            return AjaxResult.error("该设备类型["+existsNhEquipType.getTypeModel() +"]已经存在,请不要重复插入!");
        }

        return toAjax(nhEquipTypeService.insertNhEquipType(nhEquipType));
    }

    /**
     * 修改设备类型
     */
    @PreAuthorize(hasPermi = "nh:type:edit")
    @Log(title = "设备类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NhEquipType nhEquipType)
    {

        // 修改重复判断
        NhEquipType existsNhEquipType = nhEquipTypeService.existsEquipType(nhEquipType);
        if (existsNhEquipType != null) {
            return AjaxResult.error("该设备类型["+existsNhEquipType.getTypeModel() +"]已经存在,禁止修改!");
        }

        return toAjax(nhEquipTypeService.updateNhEquipType(nhEquipType));
    }

    /**
     * 删除设备类型
     */
    @PreAuthorize(hasPermi = "nh:type:remove")
    @Log(title = "设备类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 查询基本信息下是否有
        List<String> nhFloorVos = nhEquipTypeService.currentEquipmentTypeIsExistsEquipment(Arrays.asList(ids));
        if (nhFloorVos.size() > 0) {
            // List<String> 转换为String
            String appendNames = StringUtils.collectionToDelimitedString(nhFloorVos, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error("当前设备类别下[" + appendNames + "]下存在产品,禁止删除");
        }

        return toAjax(nhEquipTypeService.deleteNhEquipTypeByIds(ids));
    }
}
