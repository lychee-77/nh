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
            :data="floorTreeData"
            :props="floorCasProp"
            :highlight-current="true"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            default-expand-all
            :expand-on-click-node="false"
            ref="tree"
          ></el-tree>
        </div>
      </el-col>
      <el-col :span="20" :xs="24">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    > 
      <el-form-item label="房间名" prop="roomName">
        <el-input
          v-model="queryParams.roomName"
          placeholder="请输入房间名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
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
          v-hasPermi="['nh:room:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['nh:room:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['nh:room:remove']"
          >删除</el-button
        >
      </el-col>

      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="roomList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column label="楼宇名称" align="center" prop="buildName" />
      <el-table-column label="楼层名称" align="center" prop="floorName" />
      <el-table-column label="房间名" align="center" prop="roomName" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['nh:room:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['nh:room:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
 </el-col>
    </el-row>
 
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改房间对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80x" label-position="top" >
        <el-form-item label="楼宇楼层" prop="cascaderFloorModel">
           <el-cascader
          v-model="form.cascaderFloorModel"
          :key="floorKeyValue"
          :options="floorSelectData"    
          separator="-"
           show-all-levels
          @change="changeFloorCasHandler"
        :props="floorCasProp"></el-cascader>
        </el-form-item>
        <el-form-item label="房间名称" prop="roomName">
          <el-input v-model="form.roomName" placeholder="请输入楼层名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark" >
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"/>
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
import { listRoom, getRoom, delRoom, addRoom, updateRoom } from "@/api/nh/room";
import { getFloroTreeList } from "@/api/nh/floor";
export default {
  name: "Room",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      removeNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 房间表格数据
      roomList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        floorId: null,
        roomName: null,
        buildId: null,
      },
      treeFilterRoomName: null,
      
      floorSelectData: [],
      floorTreeData: [],
      floorSelectFilterName: null,
      floorKeyValue: 0,
      // 表单参数
      form: {
         cascaderFloorData: null
      },
     
      // 表单校验
      rules: {
        cascaderFloorModel: [
          { required: true, message: "请选择所属楼层", trigger: "blur" },
        ],
        roomName: [
          { required: true, message: "房间名不能为空", trigger: "blur" },
        ],
      },
      floorCasProp: {
        label: "label",
        children: "children",
        value: "id",
        expandTrigger: "hover",
      },
    };
  },
  created() {
    this.getList();
  },
  // 页面渲染完毕,触发初始化
  mounted() {
    this.initSeledtor();
  },
  methods: {
    /** 查询房间列表 */
    getList() {
      this.loading = true;
      listRoom(this.queryParams).then((response) => {
        this.roomList = response.rows;
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
        roomName: null,
        remark: null,
        delFlag: null,
        createUser: null,
        createTime: null,
        updateUser: null,
        updateTime: null
      };

      this.treeFilterRoomName = null;
      this.floorTreeData = []
      this.floorSelectData = [];
      this.resetForm("form");  
      this.initSeledtor();
      this.$forceUpdate();
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
      this.ids = selection.map((item) => item.id);
      this.removeNames  = selection.map((item) => item.buildName + "楼宇-" + item.floorName +"楼层-" + item.roomName +"房屋")
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.getList();
      this.title = "添加房间";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getList();
      const id = row.id || this.ids;
     
      getRoom(id).then((response) => {
        let {data} = response;
        this.form = data;
        let floorCasArr = []
        floorCasArr.push(data.buildId)
        floorCasArr.push(data.floorId)
        this.form.cascaderFloorModel = floorCasArr
        this.open = true;
        this.title = "修改房间";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateRoom(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRoom(this.form).then((response) => {
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
      const ids = row.id || this.ids;
       let removeNames = null;
      if(row.buildName && row.floorName && row.roomName){
          removeNames = row.buildName + "楼宇-" + row.floorName +"楼层"  + row.roomName + "房屋"
      }else{
          removeNames = this.removeNames.join(",")
      }
      this.$confirm('是否确认删除["' + removeNames + '"]的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delRoom(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "nh/room/export",
        {
          ...this.queryParams,
        },
        `nh_room.xlsx`
      );
    },
    initSeledtor() {
      this.initFloorSelector();
    },
    initFloorSelector() {
      getFloroTreeList().then((response) => {
        let { rows } = response;
        this.floorSelectData = rows;
        let allQueryObj = {
          "id": -1,
          "label": '全部',
          "children" : rows
        }
        this.floorTreeData.push(allQueryObj);
      });
    },
    // 节点单击事件
    handleNodeClick(data) {
      // 有子节点,说明选择的是楼宇
      if(data.id == -1){
        this.queryParams.buildId = null;
        this.queryParams.floorId = null;
      }
      else if (data["level"] == 1) {
        // 根据楼层id查询
        this.queryParams.buildId = data.id;
        this.queryParams.floorId = null;
      } else if (data["level"] == 2) {
        this.queryParams.floorId = data.id;
        this.queryParams.buildId = null;
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
    changeFloorCasHandler(floorArr) {
      
        if(floorArr["length"]){
            this.form.floorId = floorArr[floorArr.length - 1];
        }
        this.$forceUpdate();
        this.handleQuery();
    },
  },
  watch: {
    treeFilterRoomName(val) {
      this.$refs.tree.filter(val);
    },
    floorSelectData(value) {
      this.floorKeyValue++; //只要监听到数据源发生变化 ，改变keyValue的值，达到重新渲染的效果
    },
  },
};
</script>