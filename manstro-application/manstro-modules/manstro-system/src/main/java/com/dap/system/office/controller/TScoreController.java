package com.dap.system.office.controller;

import java.util.Collections;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.dap.common.core.utils.poi.FileWithExcelUtil;
import com.dap.system.office.domain.TCourse;
import com.dap.system.office.domain.vo.TStudentDetailScoreVo;
import com.dap.system.office.domain.vo.TStudentScoreInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.dap.system.office.domain.TScore;
import com.dap.system.office.service.ITScoreService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;

/**
 * 分数Controller
 *
 * @author dap
 * @date 2021-11-12
 */
@RestController
@RequestMapping("/office/score")
public class TScoreController extends BaseController {
    @Autowired
    private ITScoreService tScoreService;

    /**
     * 查询人列表
     */
    @PreAuthorize(hasPermi = "office:score:list")
    @GetMapping("/list")
    public TableDataInfo list(TScore tScore) {
        List<TScore> list = tScoreService.selectTScoreListPage(tScore);
        return getDataTable(list);
    }


    /**
     * 查询每个学生的总分与平均分即等级
     */
    @PreAuthorize(hasPermi = "office:score:list")
    @GetMapping("/studentScoreInfo")
    public TableDataInfo studentScoreInfo(TScore tScore) {
        List<TStudentScoreInfoVo> list = tScoreService.selectStudentScoreInfo(tScore);
        return getDataTable(list);
    }

    /**
     * 导出分数列表
     */
    @PreAuthorize(hasPermi = "office:score:export")
    @Log(title = "分数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TScore tScore) throws IOException {
        List<TStudentScoreInfoVo> list = tScoreService.selectStudentScoreInfo(tScore);
        FileWithExcelUtil.exportExcel(list,"成绩表","客户表",TStudentScoreInfoVo.class,"成绩表",response);

    }


    /**
     * 获取分数详细信息
     */
    @PreAuthorize(hasPermi = "office:score:query")
    @GetMapping(value = "/{id}/{semId}")
    public AjaxResult getInfo(@PathVariable("id") Integer id
            ,@PathVariable("semId") Integer semId) {

        return AjaxResult.success(tScoreService.selectTScoreById(id,semId));
    }

    /**
     * 新增分数
     */
    @PreAuthorize(hasPermi = "office:score:add")
    @Log(title = "分数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Validated TStudentDetailScoreVo tScore) {
        return toAjax(tScoreService.insertTScore(tScore), "该同学已经有成绩了,请不要重复插入");
    }

    /**
     * 修改分数
     */
    @PreAuthorize(hasPermi = "office:score:edit")
    @Log(title = "分数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody @Validated TStudentDetailScoreVo tStudentDetailScoreVo) {
        return toAjax(tScoreService.updateTScore(tStudentDetailScoreVo),"你修改的学期已经有成绩!");
    }

    /**
     * 删除分数
     */
    @PreAuthorize(hasPermi = "office:score:remove")
    @Log(title = "分数", businessType = BusinessType.DELETE)
    @DeleteMapping("/{stuId}/{semId}")
    public AjaxResult remove(@PathVariable Integer stuId,@PathVariable Integer semId) {
        return toAjax(tScoreService.deleteTScoreByIds(stuId,semId),"该学生已经有成绩,禁止重复插入!");
    }

    // 下载模板方法
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        FileWithExcelUtil.exportExcel(Collections.emptyList(),"成绩表","客户表",TStudentScoreInfoVo.class,"成绩表模板",response);
    }
}
