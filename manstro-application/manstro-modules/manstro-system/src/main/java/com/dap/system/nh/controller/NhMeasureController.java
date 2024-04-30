package com.dap.system.nh.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.nacos.common.utils.Objects;
import com.dap.system.nh.domain.NhMeasure;
import com.dap.system.nh.domain.vo.NhMeasureVo;
import com.dap.system.nh.service.INhEquipmentService;
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
import com.dap.system.nh.service.INhMeasureService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 能耗统计Controller
 *
 * @author dap
 * @date 2021-12-07
 */
@RestController
@RequestMapping("/nh/measure")
public class NhMeasureController extends BaseController {
    @Autowired
    private INhMeasureService nhMeasureService;

    @Autowired
    private INhEquipmentService nhEquipmentService;


    private static final String DEFAULT_NAME_DELIMITER = ",";

    /**
     * 查询能耗统计列表
     */
    @PreAuthorize(hasPermi = "nh:Measure:list")
    @GetMapping("/list")
    public TableDataInfo list(NhMeasureVo nhMeasure) {
        // 不做统计
        List<NhMeasureVo> list = nhMeasureService.selectNhMeasureListPage(nhMeasure,false);
        return getDataTable(list);
    }

    /**
     * 导出能耗统计列表
     */
    @PreAuthorize(hasPermi = "nh:Measure:export")
    @Log(title = "能耗统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NhMeasureVo nhMeasure) throws IOException {
        List<NhMeasureVo> list = nhMeasureService.selectNhMeasureList(nhMeasure,false);
        ExcelUtil<NhMeasureVo> util = new ExcelUtil<>(NhMeasureVo.class);
        util.exportExcel(response, list, "Measure");
    }

    /**
     * 获取能耗统计详细信息
     */
    @PreAuthorize(hasPermi = "nh:Measure:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(nhMeasureService.selectNhMeasureById(id));
    }

    /**
     * 新增能耗统计
     */
    @PreAuthorize(hasPermi = "nh:Measure:add")
    @Log(title = "能耗统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NhMeasureVo nhMeasure) {

        // 设备表中的设备可以重复
        // 但是计量表中不可以在相同的,日期,空间,设备类型,能耗类型不可以出现相同的设备
        List<NhMeasureVo> nhMeasureVos = nhMeasureService.existsEquipmentByStatistic(nhMeasure);
        // 根据 日期,空间,设备只能确定一条记录
        if (nhMeasureVos.size() > 0) {
            // 设备型号集合
            NhMeasureVo nhMeasureVo = nhMeasureVos.get(0);
            String msg = "在[%s]下已存在设备型号[%s]能耗类型[%s]的记录了,禁止添加";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentFormatDate = sdf.format(nhMeasureVo.getCurrentDate());
            return AjaxResult.error(String.format(msg, currentFormatDate, nhMeasureVo.getTypeModel(), nhMeasureVo.getNhTypeModel()));
        }


        // 当前日期如果要修改所允许的最小总值
        List<BigDecimal> totalRange = nhMeasureService.selectTotalNumRange(nhMeasure);
        BigDecimal minRange = totalRange.get(0);
        BigDecimal maxRange = totalRange.get(1);
        // 如果修改的总值小于允许修改的最小值
        // 如果修改的总值小于允许修改的最小值或者总值大于允许修改的最大值
        if(nhMeasure.getTotalNum().compareTo(minRange) < 0
                ||
                nhMeasure.getTotalNum().compareTo(maxRange) > 0
        ){
            return AjaxResult.error("你所新增记录的总值必须大于"+minRange+"且小于"+maxRange);
        }


        return toAjax(nhMeasureService.insertNhMeasure(nhMeasure));
    }


    @Log(title = "课程管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<NhMeasureVo> util = new ExcelUtil<>(NhMeasureVo.class);
        // 从Excel中获取的数据
        List<NhMeasureVo> nhMeasureVos = util.importExcel(file.getInputStream());

        // 非空处理
        if (nhMeasureVos.isEmpty()) {
           return AjaxResult.error("导入的数据不能为空");
        }

        // 数据库中所有记录
        List<NhMeasureVo> dataBaseNhMeasureVos = nhMeasureService.selectNhMeasureList(new NhMeasureVo(),false);

        // 与数据库中数据重复的数据
        List<NhMeasureVo> nhMeasureVos1 = nhMeasureService.checkExcelDataValidity(dataBaseNhMeasureVos, nhMeasureVos);

        // 不强制更新且数据库中有重复数据
        if (!updateSupport && nhMeasureVos1.size() > 0) {
            String msg = "[%s]型号的统计数据,已经在表中存在,请不要重复导入";
            List<String> nhMeasureNamesVo = nhMeasureVos1.stream().map(NhMeasureVo::getTypeModel).distinct().collect(Collectors.toList());
            String appends = org.springframework.util.StringUtils.collectionToDelimitedString(nhMeasureNamesVo, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error(String.format(msg, appends));
        }


        // 待插入的List
        nhMeasureVos.removeAll(nhMeasureVos1);

        // 获取 楼宇,楼层,房间,设备id,能耗类型id
        // 插入
        List<NhMeasure> insertNhMeasure = nhMeasureService.nhMeasureByNhStatisticVos(nhMeasureVos);


        int insertRow = 0;
        // 插入操作
        for (NhMeasure nhStatistic : insertNhMeasure) {
            insertRow += nhMeasureService.insertNhMeasure(nhStatistic);
        }

        // 不触发更新操作
        if (!updateSupport) {
            if(insertRow == 0){
                return AjaxResult.error("数据导入失败,请检查一个或多个数据项后重试");
            }

            //全部更新成功
            if(insertRow >0 && insertRow == nhMeasureVos.size()){
                return toAjax(insertRow, String.format("成功导入,插入了%s条数据", insertRow));
            }

            // 部分更新成功
            return AjaxResult
                    .success(String.format("成功导入了%s条数据,失败了%s条数据", insertRow, nhMeasureVos.size() - insertRow));
        }


        // 获取到待更新的数据
        List<NhMeasure> updateNhMeasure = nhMeasureService.nhMeasureByNhStatisticVos(nhMeasureVos1);

        // 更新通过id来进行更新
        int updateRow = 0;
        // 更新操作
        for (NhMeasure nhStatistic : updateNhMeasure) {
            updateRow += nhMeasureService.updateNhMeasure(nhStatistic);
        }

        int totalUpdateRow = insertRow + updateRow;
        if (totalUpdateRow == 0) {
            return AjaxResult.error("本次导入失败,本次失败了%s条",totalUpdateRow);
        }

        String msg = "本次导入成功,插入了%s条,更新了%s条,共计%s条";
        return AjaxResult.success( String.format(msg, insertRow, updateRow ,totalUpdateRow ));
    }

    // 下载模板方法
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<NhMeasureVo> util = new ExcelUtil<NhMeasureVo>(NhMeasureVo.class);
        util.importTemplateExcel(response, "能耗计量");
    }

    /**
     * 修改能耗统计
     */
    @PreAuthorize(hasPermi = "nh:Measure:edit")
    @Log(title = "能耗统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NhMeasure nhMeasure) {

        // 设备表中的设备可以重复
        // 但是计量表中不可以在相同的,日期,空间,设备类型,能耗类型不可以出现相同的设备
        List<NhMeasureVo> nhMeasureVos = nhMeasureService.existsEquipmentByStatistic(nhMeasure);
        // 根据 日期,空间,设备只能确定一条记录
        if (nhMeasureVos.size() > 0) {
            // 设备型号集合
            NhMeasureVo nhMeasureVo = nhMeasureVos.get(0);
            String msg = "你修改后的数据在[%s]下已存在设备型号[%s]能耗类型[%s]的记录了,禁止修改";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentFormatDate = sdf.format(nhMeasureVo.getCurrentDate());
            return AjaxResult.error(String.format(msg, currentFormatDate, nhMeasureVo.getTypeModel(), nhMeasureVo.getNhTypeModel()));
        }

        // 当前日期如果要修改所允许的最小总值
        List<BigDecimal> totalRange = nhMeasureService.selectTotalNumRange(nhMeasure);
        BigDecimal minRange = totalRange.get(0);
        BigDecimal maxRange = totalRange.get(1);
        // 如果修改的总值小于允许修改的最小值或者总值大于允许修改的最大值
        if(nhMeasure.getTotalNum().compareTo(minRange) < 0
                ||
                nhMeasure.getTotalNum().compareTo(maxRange) > 0
            ){
            return AjaxResult.error("你所新增记录的总值必须大于"+minRange+"且小于"+maxRange);
        }
        return toAjax(nhMeasureService.updateNhMeasure(nhMeasure));
    }

    /**
     * 删除能耗统计
     */
    @PreAuthorize(hasPermi = "nh:Measure:remove")
    @Log(title = "能耗统计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(nhMeasureService.deleteNhMeasureByIds(ids));
    }
}
