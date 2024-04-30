package com.dap.system.office.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.dap.system.office.domain.TSemester;
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.domain.vo.TClassSelectorVo;
import com.dap.system.office.domain.vo.TCourseSelectorVo;
import com.dap.system.office.domain.vo.TSemesterCascaderVo;
import com.dap.system.office.domain.vo.TSemesterSelectorVo;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.dap.system.office.domain.TClass;
import com.dap.system.office.service.ITClassService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 班级Controller
 *
 * @author dap
 * @date 2021-11-11
 */
@RestController
@RequestMapping("/office/class")
public class TClassController extends BaseController
{
    @Autowired
    private ITClassService tClassService;

    /**
     * 查询班级列表
     */
    @PreAuthorize(hasPermi = "office:class:list")
    @GetMapping("/list")
    public TableDataInfo list(TClass tClass)
    {
        List<TClass> list = tClassService.selectTClassListPage(tClass);
        return getDataTable(list);
    }
//
    @Log(title = "班级管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<TClass> util = new ExcelUtil<TClass>(TClass.class);
        List<TClass> semesterList = util.importExcel(file.getInputStream());

        return toAjax(
                tClassService.importData(semesterList, updateSupport),
                "批量导入数据出错!,请检查数据"
        );
    }

    // 下载模板方法
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response,TClass tClass) throws IOException {
        ExcelUtil<TClass> util = new ExcelUtil<TClass>(TClass.class);
        util.importTemplateExcel(response, "学生列表");
    }

    @PreAuthorize(hasPermi = "office:class:list")
    @GetMapping("/allName")
    public TableDataInfo allName(TClass tClass)
    {
        List<TClassSelectorVo> list = tClassService.selectTClassNameList(tClass);
        return getDataTable(list);
    }





    /**
     * 导出班级列表
     */
    @PreAuthorize(hasPermi = "office:class:export")
    @Log(title = "班级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,TClass tClass) throws IOException
    {
        List<TClass> list = tClassService.selectTClassListPage(tClass);
        ExcelUtil<TClass> util = new ExcelUtil<TClass>(TClass.class);
        util.exportExcel(response, list, "class");
    }

    /**
     * 获取班级详细信息
     */
    @PreAuthorize(hasPermi = "office:class:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tClassService.selectTClassById(id));
    }

    /**
     * 新增班级
     */
    @PreAuthorize(hasPermi = "office:class:add")
    @Log(title = "班级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TClass tClass)
    {
        return toAjax(tClassService.insertTClass(tClass),"该班级在数据库中已有记录");
    }

    /**
     * 修改班级
     */
    @PreAuthorize(hasPermi = "office:class:edit")
    @Log(title = "班级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TClass tClass)
    {
        return toAjax(tClassService.updateTClass(tClass));
    }

    /**
     * 删除班级
     */
    @PreAuthorize(hasPermi = "office:class:remove")
    @Log(title = "班级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(tClassService.deleteTClassByIds(ids),"当前班级已有学生,无法删除");
    }
}
