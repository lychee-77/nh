<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="楼宇名称" prop="buildName">
        <el-select
          v-model="queryParams.buildName"
          placeholder="请选择楼宇名称"
          filterable
          clearable
          @change="changeBuildQueryFloor"
        >
          <el-option
            v-for="item in this.selectData.builds"
            :key="item.id"
            :label="item.label"
            :value="item.label"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="楼层名称" prop="floorName">
        <el-input
          v-model="queryParams.floorName"
          placeholder="请输入楼层名称"
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
          v-hasPermi="['system:floor:add']"
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
          v-hasPermi="['system:floor:edit']"
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
          v-hasPermi="['system:floor:remove']"
          >删除</el-button
        >
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:floor:export']"
        >导出</el-button>
      </el-col> -->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="floorList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column label="楼宇名称" align="center" prop="buildName" />
      <el-table-column label="楼层名称" align="center" prop="floorName" />
            <el-table-column label="创建人" align="center" prop="createUser" />
      <el-table-column label="创建人" align="center" prop="updateUser" />
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
            v-hasPermi="['system:floor:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:floor:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改楼层对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="楼宇名称" prop="buildName">
          <el-select
            v-model="form.buildName"
            placeholder="请选择所属楼宇"
            filterable
            @change="changeBuildAddFloor"
          >
            <el-option
              v-for="item in this.selectData.builds"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="楼层名称" prop="floorName">
          <el-input v-model="form.floorName" placeholder="请输入楼层名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          />
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
import {
  listFloor,
  getFloor,
  delFloor,
  addFloor,
  updateFloor,
} from "@/api/nh/floor";
import { getBuildTreeList } from "@/api/nh/build";


export default {
  name: "Floor",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      remooveNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 楼层表格数据
      floorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        buildName: null,
        floorName: null,
      },
      selectData: {
        builds: [],
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        buildName: [
          { required: true, message: "楼宇id不能为空", trigger: "blur" },
        ],
        floorName: [
          { required: true, message: "楼层名称不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  // 页面渲染完毕,触发初始化
  mounted() {
    this.initData();
  },
  methods: {
    /** 查询楼层列表 */
    getList() {
      this.loading = true;
      listFloor(this.queryParams).then((response) => {
        this.floorList = response.rows;
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
        buildId: null,
        floorName: null,
        remark: null,
        delFlag: null,
        createUser: null,
        createTime: null,
        updateUser: null,
        updateTime: null,
      };
      this.selectData.builds = [];
      this.initData();
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.remooveNames = selection.map((item) =>  item.buildName + "楼宇-" + item.floorName +"楼层")
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加楼层";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getFloor(id).then((response) => {
        this.form = response.data;
        
        this.open = true;
        this.title = "修改楼层";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateFloor(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFloor(this.form).then((response) => {
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
      if(row.buildName && row.floorName){
          removeNames = row.buildName + "楼宇-" + row.floorName +"楼层" 
      }else{
          removeNames = this.remooveNames.join(",")
      }
       
      this.$confirm('是否确认删除为["' + removeNames + '"]的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delFloor(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "system/floor/export",
        {
          ...this.queryParams,
        },
        `system_floor.xlsx`
      );
    },
    // 初始化
    initData() {
      this.initBuildSelector();
    },
    // 初始化楼宇下拉框
    initBuildSelector() {
      getBuildTreeList().then((response) => {
        let { rows } = response;
        console.log(rows)
        this.selectData.builds = rows;
      });

      
    },
    // 楼宇下拉框选中触发搜索
    changeBuildQueryFloor(value) {
      this.handleQuery();
    },
    changeBuildAddFloor(value) {
      this.form.buildId = value;
    },
  },
};
</script>