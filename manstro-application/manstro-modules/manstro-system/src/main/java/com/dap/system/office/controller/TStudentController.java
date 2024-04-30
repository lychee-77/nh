package com.dap.system.office.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.vo.TCascaderNode;
import com.dap.system.office.domain.vo.TStudentSelectorVo;
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
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.service.ITStudentService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 学生Controller
 *
 * @author dap
 * @date 2021-11-11
 */
@RestController
@RequestMapping("/office/student")
public class TStudentController extends BaseController
{
    @Autowired
    private ITStudentService tStudentService;

    /**
     * 查询学生列表
     */
    @PreAuthorize(hasPermi = "office:student:list")
    @GetMapping("/list")
    public TableDataInfo list(TStudent tStudent)
    {
        List<TStudent> list = tStudentService.selectTStudentListPage(tStudent);
        return getDataTable(list);
    }


    /**
     * 查询学生姓名列表
     */
    @PreAuthorize(hasPermi = "office:student:list")
    @GetMapping("/allName")
    public TableDataInfo allName(TStudent tStudent)
    {
        List<TCascaderNode> list = tStudentService.selectTStudentNameList(tStudent);
        return getDataTable(list);
    }

    /**
     * 导出学生列表
     */
    @PreAuthorize(hasPermi = "office:student:export")
    @Log(title = "学生", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TStudent tStudent) throws IOException
    {
        List<TStudent> list = tStudentService.selectTStudentList(tStudent);
        ExcelUtil<TStudent> util = new ExcelUtil<TStudent>(TStudent.class);
        util.exportExcel(response, list, "student");
    }

    /**
     * 获取学生详细信息
     */
    @PreAuthorize(hasPermi = "office:student:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(tStudentService.selectTStudentById(id));
    }


    @Log(title = "班级管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<TStudent> util = new ExcelUtil<TStudent>(TStudent.class);
        List<TStudent> semesterList = util.importExcel(file.getInputStream());

        return toAjax(
                tStudentService.importData(semesterList, updateSupport),
                "批量导入数据出错!,请检查数据"
        );
    }

    // 下载模板方法
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response, TClass tClassSelectorVo) throws IOException {
        //
        ExcelUtil<TStudent> util = new ExcelUtil<TStudent>(TStudent.class);
        util.importTemplateExcel(response, "班级列表");
    }

    /**
     * 新增学生
     */
    @PreAuthorize(hasPermi = "office:student:add")
    @Log(title = "学生", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStudent tStudent)
    {
        return toAjax(tStudentService.insertTStudent(tStudent),"该学生的身份证号或者学号已被注入");
    }

    /**
     * 修改学生
     */
    @PreAuthorize(hasPermi = "office:student:edit")
    @Log(title = "学生", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStudent tStudent)
    {
        return toAjax(tStudentService.updateTStudent(tStudent),"该学号或者身份证号已被注册");
    }

    /**
     * 删除学生
     */
    @PreAuthorize(hasPermi = "office:student:remove")
    @Log(title = "学生", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(tStudentService.deleteTStudentByIds(ids));
    }
}
