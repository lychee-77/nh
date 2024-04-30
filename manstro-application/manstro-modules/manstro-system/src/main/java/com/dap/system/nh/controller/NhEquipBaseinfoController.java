package com.dap.system.nh.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

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
import com.dap.system.nh.domain.NhEquipBaseinfo;
import com.dap.system.nh.service.INhEquipBaseinfoService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;

/**
 * 设备基本信息Controller
 *
 * @author dap
 * @date 2021-12-04
 */
@RestController
@RequestMapping("/nh/baseInfo")
public class NhEquipBaseinfoController extends BaseController {
    @Autowired
    private INhEquipBaseinfoService nhEquipBaseinfoService;

    /**
     * 给前端显示时默认的分隔符
     */
    private static final String DEFAULT_NAME_DELIMITER = ",";

    /**
     * 查询设备基本信息列表
     */
    @PreAuthorize(hasPermi = "nh:baseinfo:list")
    @GetMapping("/list")
    public TableDataInfo list(NhEquipBaseinfo nhEquipBaseinfo) {
        List<NhEquipBaseinfo> list = nhEquipBaseinfoService.selectNhEquipBaseinfoListPage(nhEquipBaseinfo);
        return getDataTable(list);
    }

    /**
     * 查询设备基本信息树列表
     */
    @PreAuthorize(hasPermi = "nh:baseinfo:treeList")
    @GetMapping("/treeList")
    public TableDataInfo treeList() {
        List<TreeNode> list = nhEquipBaseinfoService.selectNhEquipBaseinfoTreeList();
        return getDataTable(list);
    }


    /**
     * 导出设备基本信息列表
     */
    @PreAuthorize(hasPermi = "nh:baseinfo:export")
    @Log(title = "设备基本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NhEquipBaseinfo nhEquipBaseinfo) throws IOException {
        List<NhEquipBaseinfo> list = nhEquipBaseinfoService.selectNhEquipBaseinfoList(nhEquipBaseinfo);
        ExcelUtil<NhEquipBaseinfo> util = new ExcelUtil<>(NhEquipBaseinfo.class);
        util.exportExcel(response, list, "baseinfo");
    }

    /**
     * 获取设备基本信息详细信息
     */
    @PreAuthorize(hasPermi = "nh:baseinfo:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(nhEquipBaseinfoService.selectNhEquipBaseinfoById(id));
    }

    /**
     * 新增设备基本信息
     */
    @PreAuthorize(hasPermi = "nh:baseinfo:add")
    @Log(title = "设备基本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NhEquipBaseinfo nhEquipBaseinfo) {

        // 新增重复判断
        NhEquipBaseinfo existsNhEquipBaseinfo = nhEquipBaseinfoService.existsEquipBaseInfo(nhEquipBaseinfo);
        if (existsNhEquipBaseinfo != null) {
            return AjaxResult.error("该公司名字[" + existsNhEquipBaseinfo.getVenderName() + "]已经存在,请不要重复插入!");
        }

        return toAjax(nhEquipBaseinfoService.insertNhEquipBaseinfo(nhEquipBaseinfo));
    }

    /**
     * 修改设备基本信息
     */
    @PreAuthorize(hasPermi = "nh:baseinfo:edit")
    @Log(title = "设备基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NhEquipBaseinfo nhEquipBaseinfo) {


        // 修改重复判断
        NhEquipBaseinfo existsNhEquipBaseinfo = nhEquipBaseinfoService.existsEquipBaseInfo(nhEquipBaseinfo);
        if (existsNhEquipBaseinfo != null) {
            return AjaxResult.error("该公司名字[" + existsNhEquipBaseinfo.getVenderName() + "]已经存在,禁止修改!");
        }

        return toAjax(nhEquipBaseinfoService.updateNhEquipBaseinfo(nhEquipBaseinfo));
    }

    /**
     * 删除设备基本信息
     */
    @PreAuthorize(hasPermi = "nh:baseinfo:remove")
    @Log(title = "设备基本信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {

        // 查询基本信息下是否有
        List<String> nhFloorVos = nhEquipBaseinfoService.currentBaseInfoIsExistsEquipment(Arrays.asList(ids));

        if (nhFloorVos.size() > 0) {
            // List<String> 转换为String
            String appendNames = StringUtils.collectionToDelimitedString(nhFloorVos, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error("当前厂商[" + appendNames + "]下存在产品,禁止删除");
        }
        return toAjax(nhEquipBaseinfoService.deleteNhEquipBaseinfoByIds(ids));
    }
}
