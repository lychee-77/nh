package com.dap.system.nh.controller;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import com.dap.system.nh.domain.vo.NhEquipmentVo;
import com.dap.system.nh.domain.vo.NhRoomVo;
import com.dap.system.nh.domain.vo.TreeNode;
import com.dap.system.nh.service.INhBuildService;
import com.dap.system.nh.service.INhEquipmentService;
import com.dap.system.nh.service.INhFloorService;
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
import com.dap.system.nh.domain.NhRoom;
import com.dap.system.nh.service.INhRoomService;
import com.dap.common.core.web.controller.BaseController;
import com.dap.common.core.web.domain.AjaxResult;
import com.dap.common.core.utils.poi.ExcelUtil;
import com.dap.common.core.web.page.TableDataInfo;

/**
 * 房间Controller
 *
 * @author dap
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/nh/room")
public class NhRoomController extends BaseController
{
    @Autowired
    private INhRoomService nhRoomService;

    @Autowired
    private INhBuildService nhBuildService;

    @Autowired
    private INhFloorService nhFloorService;

    @Autowired
    private INhEquipmentService nhEquipmentService;

    /**
     * 给前端显示时默认的分隔符
     */
    private static final String DEFAULT_NAME_DELIMITER = ",";


    /**
     * 查询房间列表
     */
    @PreAuthorize(hasPermi = "nh:room:list")
    @GetMapping("/list")
    public TableDataInfo list(NhRoomVo nhRoomVo)
    {
        List<NhRoomVo> list = nhRoomService.selectNhRoomListPage(nhRoomVo);
        return getDataTable(list);
    }

    /**
     * 查询房间列表
     */
    @PreAuthorize(hasPermi = "nh:room:list")
    @GetMapping("/treeList")
    public TableDataInfo treeList(NhRoomVo nhRoomVo)
    {
        // 封装build-floor-room三级信息
        List<TreeNode> list = nhRoomService.selectNhRoomTreeList();
        return getDataTable(list);
    }

    /**
     * 导出房间列表
     */
    @PreAuthorize(hasPermi = "nh:room:export")
    @Log(title = "房间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NhRoomVo nhRoomVo) throws IOException
    {
        List<NhRoomVo> list = nhRoomService.selectNhRoomList(nhRoomVo);
        ExcelUtil<NhRoomVo> util = new ExcelUtil<NhRoomVo>(NhRoomVo.class);
        util.exportExcel(response, list, "room");
    }

    /**
     * 获取房间详细信息
     */
    @PreAuthorize(hasPermi = "nh:room:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(nhRoomService.selectNhRoomById(id));
    }

    /**
     * 新增房间
     */
    @PreAuthorize(hasPermi = "nh:room:add")
    @Log(title = "房间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NhRoom nhRoom)
    {
        // 新增重复判断
        NhRoomVo nhRoomVo = nhRoomService.existsRoomByFloor(nhRoom);
        if (nhRoomVo != null) {
            return AjaxResult.error("你新增的房层[" + nhRoomVo.getRoomName() + "" +
                    "]已经在" + nhRoomVo.getBuildName() + "楼宇"+nhRoomVo.getFloorName() +
                    "楼层中存在,禁止插入!");
        }

        return toAjax(nhRoomService.insertNhRoom(nhRoom));
    }

    /**
     * 修改房间
     */
    @PreAuthorize(hasPermi = "nh:room:edit")
    @Log(title = "房间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NhRoom nhRoom)
    {


        // 修改的楼层名字重复
        NhRoomVo nhRoomVo = nhRoomService.existsRoomByFloor(nhRoom);
        if (nhRoomVo != null) {
            return AjaxResult.error("你修改的房层[" + nhRoomVo.getRoomName() + "" +
                    "]已经在" + nhRoomVo.getBuildName() + "楼宇"+nhRoomVo.getFloorName() +
                    "楼层中存在,禁止修改!");
        }

        return toAjax(nhRoomService.updateNhRoom(nhRoom));
    }

    /**
     * 删除房间
     */
    @PreAuthorize(hasPermi = "nh:room:remove")
    @Log(title = "房间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        // 设备绑定后该地域无法被删除
        List<Long> queryRoomIds = Arrays.asList(ids);
        // 根据地域id查询设备表中是否有设备正在使用
        List<NhEquipmentVo> nhEquipmentVos = nhEquipmentService.selectBatchNhEquipmentsBySpaceIds(queryRoomIds);
        // 有,更久地域id从设备表中查询设备型号,提示无法删除
        if(nhEquipmentVos.size() > 0) {
            // 根据地域id构成的集合 批量查询获得设备信息
            List<String> typeModelNames = nhEquipmentVos.stream()
                    .map(NhEquipmentVo::getTypeModel).collect(Collectors.toList());
            String appendNames = StringUtils.collectionToDelimitedString(typeModelNames, DEFAULT_NAME_DELIMITER);
            return AjaxResult.error("当前房间下存在[" + appendNames + "]设备,禁止删除");
       }
        // 没有成功删除
        return toAjax(nhRoomService.deleteNhRoomByIds(ids));
    }
}
