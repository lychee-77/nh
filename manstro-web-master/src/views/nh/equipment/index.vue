<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="treeFilterRoomName"
            placeholder="请输入名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="roomTreeData"
            :props="roomCasProp"
            :highlight-current="true"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            default-expand-all
            
            ref="tree"
          ></el-tree>
          <!-- :expand-on-click-node="false" -->
        </div>
      </el-col>
       <el-col :span="20" :xs="24">

    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="型号" prop="typeModel">
        <el-input
          v-model="queryParams.typeModel"
          placeholder="请输入型号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="厂家名称" prop="venderName">
         <el-select
          v-model="queryParams.venderName"
          placeholder="请选择厂家名称"
          filterable
          clearable
          @change="changeQueryParamsHandlerQuery"
        >
          <el-option
            v-for="item in this.selectData.venderNames"
            :key="item.id"
            :label="item.label"
            :value="item.label"
          >
          </el-option>
        </el-select>
        
      </el-form-item>
      <el-form-item label="设备类型" prop="equipmentTypeModel">
        <el-select
          v-model="queryParams.equipmentTypeModel"
          placeholder="请选择设备类型"
          filterable
          clearable
          @change="changeQueryParamsHandlerQuery"
        >
          <el-option
            v-for="item in this.selectData.equipTypes"
            :key="item.id"
            :label="item.label"
            :value="item.label"
          >
          </el-option>
        </el-select>
      </el-form-item>
    
    
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['nh:equipment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['nh:equipment:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['nh:equipment:remove']"
        >删除</el-button>
      </el-col>
   
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="equipmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备型号" align="center" prop="typeModel" />
      <el-table-column label="所在位置" align="center" prop="spaceId" >
        <template slot-scope="scope">
            {{parseSpaceName(scope.row)}}
        </template>
      </el-table-column>
      <el-table-column label="厂家名称" align="center" prop="venderName" />
      <el-table-column label="厂家地址" align="center" prop="locationName" />
      <el-table-column label="设备类型" align="center" prop="equipmentTypeModel" />
      
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['nh:equipment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['nh:equipment:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
      <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
       </el-col>
        </el-row>
  

    <!-- 添加或修改设备表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="型号" prop="typeModel">
          <el-input v-model="form.typeModel" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="厂家" prop="baseinfoId">
         <el-select
          v-model="form.baseinfoId"
          placeholder="请选择厂家"
          filterable
          clearable
          @change="changeVenderUpdateForm"
        >
          <el-option
            v-for="item in this.selectData.venderNames"
            :key="item.id"
            :label="item.label"
            :value="item.id"
          >
          </el-option>
        </el-select>
        </el-form-item>
      <el-form-item label="设备类型" prop="typeId">
         <el-select
          v-model="form.typeId"
          placeholder="请选择设备类型"
          filterable
          clearable
          @change="changeEquipemntTypeUpdateForm"
        >
          <el-option
            v-for="item in this.selectData.equipTypes"
            :key="item.id"
            :label="item.label"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
        <el-form-item label="安放地点" prop="cascaderFloorModel">
         <el-cascader
          v-model="form.cascaderFloorModel"
          :key="floorKeyValue"
          :options="roomCasData"    
          separator="-"
           show-all-levels
          @change="changeRoomCasHandler"
        :props="roomCasProp"></el-cascader>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEquipment, getEquipment, delEquipment, addEquipment, updateEquipment } from "@/api/nh/equipment";
import { roomTreeList } from "@/api/nh/room";
import { getBaseinfoTreeList } from "@/api/nh/baseinfo";
import { getEquipTypeTreeList } from "@/api/nh/equipType";
export default {
  name: "Equipment",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      idNames:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 设备表表格数据
      equipmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        typeModel: null,
        baseinfoId: null,
        typeId: null,
        venderName:null,
        spaceId: null,
        equipmentTypeModel:null,
        venderName:null,
      },
      // 表单参数
      form: {
         id: null,
        typeModel: null,
        baseinfoId: null,
        typeId: null,
        remark: null,
        delFlag: null,
        venderName:null,
        equipmentTypeModel:null
      },
      // 表单校验
      rules: {
        typeModel: [
          { required: true, message: "型号不能为空", trigger: "blur" }
        ],
        baseinfoId: [
          { required: true, message: "基本信息id不能为空", trigger: "blur" }
        ],
        typeId:[
          { required: true, message: "设备类型不能为空", trigger: "blur" }
        ],
        cascaderFloorModel:
        [
          { required: true, message: "安放地点不能为空", trigger: "blur" }
        ]
      },
     roomTreeData:[],
      roomCasProp :{
        value:"id",
        label: "label",
        children: "children",
        expandTrigger: "hover",
      },
      treeFilterRoomName:null,
      roomCasData:[],
      floorKeyValue:0,
      selectData: {
        venderNames: [],
        equipTypes: [],
        equipType: []
      }
    };
  },
  created() {
    this.handleQuery();
  },
   // 页面渲染完毕,触发初始化
  mounted() {
    this.initSelector();
  },
  
  methods: {
    /** 查询设备表列表 */
    getList() {
      this.loading = true;
      listEquipment(this.queryParams).then(response => {
        this.equipmentList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        typeModel: null,
        baseinfoId: null,
        typeId: null,
        remark: null,
        delFlag: null,
        venderName:null,
        equipmentTypeModel:null
      };
      
      // 清空级联框的数据源否则会报错
      this.roomCasData= []
      this.roomTreeData = []
      this.queryParams.buildId = null;
      this.queryParams.floorId = null;
      this.queryParams.spaceId = null;
      this.resetForm("form");
      this.initSelector();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.reset();
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.idNames = selection.map(item => item.buildName+"楼宇"+item.floorName+"楼层"+item.roomName+"房屋"+item.typeModel+"型号")
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEquipment(id).then(response => {
        let {data} = response;
        this.form = data;
        this.open = true;
         let floorCasArr = []
        floorCasArr.push(data.buildId)
        floorCasArr.push(data.floorId)
        floorCasArr.push(data.spaceId)
        this.form.cascaderFloorModel = floorCasArr
        console.log(this.form)
        this.title = "修改设备表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      console.log(this.form)
      this.$refs["form"].validate(valid => {
        
        if (valid) {
          console.log(this.form)
          if (this.form.id != null) {
            updateEquipment(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEquipment(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let removeNames;
      if(row.buildName && row.floorName && row.roomName && row.typeModel){
          removeNames = row.buildName + "楼宇-" + row.floorName +"楼层"  + row.roomName + "房屋" + row.typeModel + "型号"
      }else{
          removeNames = this.removeNames.join(",")
      }
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除设备["' + removeNames + '"]的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delEquipment(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('nh/equipment/export', {
        ...this.queryParams
      }, `nh_equipment.xlsx`)
    },
    parseSpaceName(row){
      let buildName = row.buildName
      let floorName = row.floorName
      let roomName = row.roomName
      return buildName + "-" + floorName + "-" + roomName
    },
    initSelector(){
      this.initRoomSelector();
      this.initVenderNamesSelector();
      this.initEquuipTypesSelector();
    },
    initRoomSelector(){
          roomTreeList().then((response) => {
           let { rows } = response;
           this.roomCasData = rows;
            let allQueryObj = {
          "id": -1,
          "label": '全部',
          "children" : rows
          }
          console.log(this.roomCasData)
        this.roomTreeData.push(allQueryObj);
      });
    },
      initVenderNamesSelector(){
          getBaseinfoTreeList().then((response) => {
            let { rows } = response;
            this.selectData.venderNames = rows;
      });
    },
    initEquuipTypesSelector(){
          getEquipTypeTreeList().then((response) => {
            let { rows } = response;
            this.selectData.equipTypes = rows;
      });
    },

     // 节点单击事件
    handleNodeClick(data) {
      // 有子节点,说明选择的是楼宇
      if(data.id == -1){
        this.queryParams.buildId = null;
        this.queryParams.floorId = null;
        this.queryParams.spaceId = null;
      }
      else if (data["level"] == 1) {
        // 根据楼层id查询
        this.queryParams.buildId = data.id;
        this.queryParams.floorId = null;
        this.queryParams.spaceId = null;
      } else if (data["level"] == 2) {
        this.queryParams.floorId = data.id;
        this.queryParams.buildId = null;
        this.queryParams.spaceId = null;
      }else if (data["level"] == 3) {
        this.queryParams.floorId = null;
        this.queryParams.buildId = null;
        // 空间,房间id
        this.queryParams.spaceId = data.id;
      }
      this.handleQuery();
    },
     // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      if (data["label"]) {
        return data.label.indexOf(value) !== -1;
      }
    },

    changeRoomCasHandler(floorArr) {
        if(floorArr["length"]){
            this.form.spaceId = floorArr[floorArr.length - 1];
        }
        console.log(this.form.spaceId)
        this.$forceUpdate();
    },
    changeQueryParamsHandlerQuery(){
        this.handleQuery();
    },
    changeVenderUpdateForm(id){
        this.form.spaceId = id;
    },
    changeEquipemntTypeUpdateForm(id){
      this.form.equipmentTypeModel = id;
    }
  
  
  },
  watch: {
    treeFilterRoomName(val) {
      this.$refs.tree.filter(val);
    },
    roomCasData(value) {
      this.floorKeyValue++; //只要监听到数据源发生变化 ，改变keyValue的值，达到重新渲染的效果
    }
    }
};  
</script>