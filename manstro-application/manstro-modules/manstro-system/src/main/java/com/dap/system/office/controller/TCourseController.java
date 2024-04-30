package com.dap.system.office.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.TStudent;
import com.dap.system.office.domain.vo.TCourseSelectorVo;
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
import com.dap.system.office.domain.TCourse;
import com.dap.system.office.service.ITCourseService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 课程Controller
 *
 * @author dap
 * @date 2021-11-10
 */
@RestController
@RequestMapping("/office/course")
public class TCourseController extends BaseController
{
    @Autowired
    private ITCourseService tCourseService;

    /**
     * 查询课程列表
     */
    @PreAuthorize(hasPermi = "office:course:list")
    @GetMapping("/list")
    public TableDataInfo list(TCourse tCourse)
    {
        tCourse.setDelFlag("0");
        List<TCourse> list = tCourseService.selectTCourseListPage(tCourse);
        return getDataTable(list);
    }

    @PreAuthorize(hasPermi = "office:course:list")
    @GetMapping("/allName")
    public TableDataInfo allName()
    {
            List<TCourseSelectorVo> list = tCourseService.selectTCourseNameList();
            return getDataTable(list);
    }


    /**
     * 导出课程列表
     */
    @PreAuthorize(hasPermi = "office:course:export")
    @Log(title = "课程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCourse tCourse) throws IOException
    {
        List<TCourse> list = tCourseService.selectTCourseList(tCourse);
        ExcelUtil<TCourse> util = new ExcelUtil<TCourse>(TCourse.class);
        util.exportExcel(response, list, "course");
    }

    /**
     * 获取课程详细信息
     */
    @PreAuthorize(hasPermi = "office:course:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tCourseService.selectTCourseById(id));
    }

    /**
     * 新增课程
     */
    @PreAuthorize(hasPermi = "office:course:add")
    @Log(title = "课程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCourse tCourse)
    {
        return toAjax(tCourseService.insertTCourse(tCourse),"该课程已经存在");
    }

    /**
     * 修改课程
     */
    @PreAuthorize(hasPermi = "office:course:edit")
    @Log(title = "课程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCourse tCourse)
    {

        return toAjax(tCourseService.updateTCourse(tCourse));
    }
    @Log(title = "课程管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<TCourse> util = new ExcelUtil<TCourse>(TCourse.class);
        List<TCourse> semesterList = util.importExcel(file.getInputStream());

        return toAjax(
                tCourseService.importData(semesterList, updateSupport),
                "批量导入数据出错!,请检查数据"
        );
    }

    // 下载模板方法
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<TCourse> util = new ExcelUtil<TCourse>(TCourse.class);
        util.importTemplateExcel(response, "课程列表");
    }

    /**
     * 删除课程
     */
    @PreAuthorize(hasPermi = "office:course:remove")
    @Log(title = "课程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(tCourseService.deleteTCourseByIds(ids));
    }
}
