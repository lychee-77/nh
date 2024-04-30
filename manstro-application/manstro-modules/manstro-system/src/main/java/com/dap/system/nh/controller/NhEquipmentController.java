package com.dap.system.nh.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.dap.system.nh.domain.vo.NhEquipmentVo;
import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.dap.system.nh.domain.vo.TreeNode;
import com.dap.system.nh.service.INhMeasureService;
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
import com.dap.system.nh.domain.NhEquipment;
import com.dap.system.nh.service.INhEquipmentService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;

/**
 * 设备表Controller
 *
 * @author dap
 * @date 2021-12-04
 */
@RestController
@RequestMapping("/nh/equipment")
public class NhEquipmentController extends BaseController
{
    @Autowired
    private INhEquipmentService nhEquipmentService;

    @Autowired
    private INhMeasureService nhmeasureService;

    private static final String DEFAULT_NAME_DELIMITER = ",";

    /**
     * 查询设备表列表
     */
    @PreAuthorize(hasPermi = "nh:equipment:list")
    @GetMapping("/list")
    public TableDataInfo list(NhEquipmentVo nhEquipment)
    {
        List<NhEquipment> list = nhEquipmentService.selectNhEquipmentListPage(nhEquipment);
        return getDataTable(list);
    }

    /**
     * 查询设备表列表
     */
    @PreAuthorize(hasPermi = "nh:equipment:treeList")
    @GetMapping("/treeList")
    public TableDataInfo treeList()
    {
        List<TreeNode> list = nhEquipmentService.selectNhEquipmentTreeList();
        return getDataTable(list);
    }

    /**
     * 导出设备表列表
     */
    @PreAuthorize(hasPermi = "nh:equipment:export")
    @Log(title = "设备表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NhEquipmentVo nhEquipment) throws IOException
    {
        List<NhEquipment> list = nhEquipmentService.selectNhEquipmentList(nhEquipment);
        ExcelUtil<NhEquipment> util = new ExcelUtil<NhEquipment>(NhEquipment.class);
        util.exportExcel(response, list, "equipment");
    }

    /**
     * 获取设备表详细信息
     */
    @PreAuthorize(hasPermi = "nh:equipment:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(nhEquipmentService.selectNhEquipmentById(id));
    }

    /**
     * 新增设备表
     */
    @PreAuthorize(hasPermi = "nh:equipment:add")
    @Log(title = "设备表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NhEquipmentVo nhEquipment)
    {
        List<NhEquipmentVo> nhEquipmentVo = nhEquipmentService.existsNhType(nhEquipment);

        if (nhEquipmentVo.size() > 0 ) {

            return AjaxResult.error("你新增的设备名称[" + nhEquipmentVo.get(0).getTypeModel() + "" +
                    "]已经在" + nhEquipmentVo.get(0).getBuildName() + "楼宇"+nhEquipmentVo.get(0).getFloorName() +
                    "楼层"+nhEquipmentVo.get(0).getRoomName() +"房屋中存在,禁止插入!");
        }
        return toAjax(nhEquipmentService.insertNhEquipment(nhEquipment));
    }

    /**
     * 修改设备表
     */
    @PreAuthorize(hasPermi = "nh:equipment:edit")
    @Log(title = "设备表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NhEquipmentVo nhEquipment)
    {

        List<NhEquipmentVo> nhEquipmentVo = nhEquipmentService.existsNhType(nhEquipment);

        if (nhEquipmentVo.size() > 0 ) {

            return AjaxResult.error("你修改后的设备名称[" + nhEquipmentVo.get(0).getTypeModel() + "" +
                    "]已经在" + nhEquipmentVo.get(0).getBuildName() + "楼宇"+nhEquipmentVo.get(0).getFloorName() +
                    "楼层"+nhEquipmentVo.get(0).getRoomName() +"房屋中存在,禁止修改!");
        }
        return toAjax(nhEquipmentService.updateNhEquipment(nhEquipment));
    }

    /**
     * 删除设备表
     */
    @PreAuthorize(hasPermi = "nh:equipment:remove")
    @Log(title = "设备表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        List<NhMeasureVo> queryObj = new ArrayList<>();
        for (Long id : ids) {
            NhMeasureVo nhMeasureVo = new NhMeasureVo();
            nhMeasureVo.setEquipId(id);
            queryObj.add(nhMeasureVo);
        }

        // 根据条件查询出来集合
        List<NhMeasureVo> existsNhmeasure = nhmeasureService.currentSpaceIdIsExistsMeasure(queryObj);

        if(existsNhmeasure.size() > 0){
            String msg = "当前删除的设备[%s]已经在计量中已经存在,无法删除";
            List<String> equipmentNames = existsNhmeasure.stream().map(NhMeasureVo::getTypeModel).collect(Collectors.toList());
            String appendNames = StringUtils.collectionToDelimitedString(equipmentNames, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error(String.format(msg,appendNames ));

        }
        return toAjax(nhEquipmentService.deleteNhEquipmentByIds(ids));
    }
}
