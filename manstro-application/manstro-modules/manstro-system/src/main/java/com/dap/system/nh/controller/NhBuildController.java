package com.dap.system.nh.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.dap.system.nh.domain.vo.NhFloorVo;
import com.dap.system.nh.mapper.NhBuildMapper;
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
import com.dap.system.nh.domain.NhBuild;
import com.dap.system.nh.service.INhBuildService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;

/**
 * 楼宇Controller
 *
 * @author dap
 * @date 2021-12-02
 */
@RestController
@RequestMapping("/nh/build")
public class NhBuildController extends BaseController
{
    @Autowired
    private INhBuildService nhBuildService;


    /**
     * 给前端显示时默认的分隔符
     */
    private static final String DEFAULT_NAME_DELIMITER = ",";

    private static final String DEFAULT_REMOVE_ERROR_MSG = "当前%s下已经存在名称为[%s]的%s了,禁止删除";

    /**
     * 查询楼宇列表
     */
    @PreAuthorize(hasPermi = "build:build:list")
    @GetMapping("/list")
    public TableDataInfo list(NhBuild nhBuild)
    {
        List<NhBuild> list = nhBuildService.selectNhBuildListPage(nhBuild);
        return getDataTable(list);
    }

    /**
     * 构造下拉框
     * @return
     */
    @PreAuthorize(hasPermi = "build:build:list")
    @GetMapping("/treeList")
    public TableDataInfo treeList(){
        return getDataTable(nhBuildService.selectNhBuildTreeList());
    }

    /**
     * 导出楼宇列表
     */
    @PreAuthorize(hasPermi = "build:build:export")
    @Log(title = "楼宇", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NhBuild nhBuild) throws IOException
    {
        List<NhBuild> list = nhBuildService.selectNhBuildList(nhBuild);
        ExcelUtil<NhBuild> util = new ExcelUtil<>(NhBuild.class);
        util.exportExcel(response, list, "build");
    }

    /**
     * 获取楼宇详细信息
     */
    @PreAuthorize(hasPermi = "build:build:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(nhBuildService.selectNhBuildById(id));
    }

    /**
     * 新增楼宇
     */
    @PreAuthorize(hasPermi = "build:build:add")
    @Log(title = "楼宇", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NhBuild nhBuild)
    {
        // 增加的楼宇已经存在
        String buildName = nhBuild.getBuildName();
        if(nhBuildService.existsBuilderByBuilderName(nhBuild)){
            return AjaxResult.error(String.format("你新增的%s楼宇已经存在!", buildName));
        }

        return toAjax(nhBuildService.insertNhBuild(nhBuild));
    }

    /**
     * 修改楼宇
     */
    @PreAuthorize(hasPermi = "build:build:edit")
    @Log(title = "楼宇", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NhBuild nhBuild)
    {
        // 要修改的楼宇下已经有楼层禁止修改
        List<String> nhFloorVos = nhBuildService.currentBuilderIsExistsFloorer(Arrays.asList(nhBuild.getId()));
        if(nhFloorVos.size() > 0){
            // List<String> 转换为String
            String appendNames = StringUtils.collectionToDelimitedString(nhFloorVos,DEFAULT_NAME_DELIMITER);
            return AjaxResult.error(String.format("你要修改下楼宇存在名称为["+appendNames + "]的楼层了,禁止修改!" ));
        }

        // 待修改的楼宇名称已经被注册,无法修改
        String buildName = nhBuild.getBuildName();
        if(nhBuildService.existsBuilderByBuilderName(nhBuild)){
            return AjaxResult.error(String.format("你要修改的楼宇名称为["+buildName + "]已经被注册,无法修改!" ));
        }

        return toAjax(nhBuildService.updateNhBuild(nhBuild));
    }




    /**
     * 删除楼宇
     */
    @PreAuthorize(hasPermi = "build:build:remove")
    @Log(title = "楼宇", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {

        // 查询楼宇下是否有楼层如果有禁止删除
        List<String> nhFloorVos = nhBuildService.currentBuilderIsExistsFloorer(Arrays.asList(ids));

        if(nhFloorVos.size() > 0){
            // List<String> 转换为String
            String appendNames = StringUtils.collectionToDelimitedString(nhFloorVos,DEFAULT_NAME_DELIMITER);
            return AjaxResult.error(String.format(DEFAULT_REMOVE_ERROR_MSG, "楼宇",appendNames,"楼层"));
        }

        return toAjax(nhBuildService.deleteNhBuildByIds(ids));
    }


}
