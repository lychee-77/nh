package com.dap.system.office.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.dap.common.core.utils.ServletUtils;
import com.dap.common.core.web.domain.BaseEntity;
import com.dap.system.api.domain.SysUser;
import com.dap.system.api.model.LoginUser;
import com.dap.system.office.domain.TClass;
import com.dap.system.office.domain.vo.TSemesterCascaderVo;
import com.dap.system.office.domain.vo.TSemesterSelectorVo;
import com.dap.system.office.domain.vo.TTeacherSelectorVo;
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
import com.dap.system.office.domain.TSemester;
import com.dap.system.office.service.ITSemesterService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 学期管理Controller
 *
 * @author dap
 * @date 2021-11-11
 */
@RestController
@RequestMapping("/office/semester")
public class TSemesterController extends BaseController {
    @Autowired
    private ITSemesterService tSemesterService;

    /**
     * 查询学期管理列表
     */
    @PreAuthorize(hasPermi = "office:semester:list")
    @GetMapping("/list")
    public TableDataInfo list(TSemester tSemester) {
        List<TSemester> list = tSemesterService.selectTSemesterListPage(tSemester);
        return getDataTable(list);
    }

    /**
     * 查询学期列表,封装为tree结构
     */
    @PreAuthorize(hasPermi = "office:semester:list")
    @GetMapping("/treeList")
    public TableDataInfo treeList() {
        List<TSemesterCascaderVo> list = tSemesterService.selectTSemesterCascaderVo();
        return getDataTable(list);
    }

    /**
     * 查询学期列表,学期下拉框
     */
    @PreAuthorize(hasPermi = "office:semester:list")
    @GetMapping("/allName")
    public TableDataInfo allName() {
        List<TSemesterSelectorVo> tSemesterSelectorVos = tSemesterService.selectTSemesterNameListPage();
        return getDataTable(tSemesterSelectorVos);
    }

    /**
     * 导出学期管理列表
     */
    @PreAuthorize(hasPermi = "office:semester:export")
    @Log(title = "学期管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSemester tSemester) throws IOException {
        List<TSemester> list = tSemesterService.selectTSemesterList(tSemester);
        ExcelUtil<TSemester> util = new ExcelUtil<TSemester>(TSemester.class);
        util.exportExcel(response, list, "semester");
    }

    /**
     * 获取学期管理详细信息
     */
    @PreAuthorize(hasPermi = "office:semester:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(tSemesterService.selectTSemesterById(id));
    }

    /**
     * 新增学期管理
     */
    @PreAuthorize(hasPermi = "office:semester:add")
    @Log(title = "学期管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSemester tSemester) {
        return toAjax(tSemesterService.insertTSemester(tSemester), "学期已存在");
    }

    /**
     * 修改学期管理
     */
    @PreAuthorize(hasPermi = "office:semester:edit")
    @Log(title = "学期管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSemester tSemester) {
        return toAjax(tSemesterService.updateTSemester(tSemester), "被修改的学期已经存在");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<TSemester> util = new ExcelUtil<TSemester>(TSemester.class);
        List<TSemester> semesterList = util.importExcel(file.getInputStream());

        return toAjax(
                tSemesterService.importSemester(semesterList, updateSupport),
                "批量导入数据出错!,请检查数据"
        );
    }

    // 下载模板方法
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {

        ExcelUtil<TSemester> util = new ExcelUtil<TSemester>(TSemester.class);
        util.importTemplateExcel(response, "学期列表");
    }

    /**
     * 删除学期管理
     */
    @PreAuthorize(hasPermi = "office:semester:remove")
    @Log(title = "学期管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(tSemesterService.deleteTSemesterByIds(ids),"该学期下已经存在班级,无法删除");
    }
}
