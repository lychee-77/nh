<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="学期名称" prop="semName">
        <el-select
          v-model="queryParams.studayYear"
          placeholder="请选择所属学期"
          filterable
          clearable
          @change="changeSemesterQueryClass"
        >
          <el-option
            v-for="item in this.semNames"
            :key="item.id"
            :label="item.studayYear"
            :value="item.studayYear"
          >
          </el-option>
        </el-select>
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
          v-hasPermi="['office:class:add']"
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
          v-hasPermi="['office:class:edit']"
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
            v-hasPermi="['office:class:remove']"
            >删除</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['office:class:export']"
            >导出</el-button
          >
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="info"
            plain
            icon="el-icon-upload2"
            size="mini"
            @click="handleImport"
            v-hasPermi="['office:class:import']"
            >导入</el-button
          >
        </el-col>
        <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="classList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50" align="center">
      </el-table-column>
      <el-table-column label="所属学年" align="center" prop="">
        <template slot-scope="scope">
          {{ parseSemesterRange(scope.row.startYear, scope.row.endYear, "-") }}
        </template>
      </el-table-column>
      <el-table-column label="班级名称" align="center" prop="className" />

      <el-table-column label="创建用户" align="center" prop="createBy" />
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
            v-hasPermi="['office:class:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['office:class:remove']"
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

    <!-- 添加或修改班级对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="80px"
        label-position="top"
      >
        <el-form-item label="学期名称" prop="studayYear">
          <el-select
            v-model="form.studayYear"
            placeholder="请选择所属学期"
            filterable
            @change="changeSemesterSetId"
          >
            <el-option
              v-for="item in this.semNames"
              :key="item.id"
              :label="item.studayYear"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="所属班级" prop="className">
          <el-input v-model="form.className" placeholder="请输入所属班级" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 班级导入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
    >
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-link type="info" style="font-size: 12px" @click="importTemplate"
            >下载模板</el-link
          >
        </div>
        <div class="el-upload__tip" style="color: red" slot="tip">
          提示：仅允许导入“xls”或“xlsx”格式文件！
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import {
  listClass,
  getClass,
  delClass,
  addClass,
  updateClass,
} from "@/api/office/class";
import { allName } from "@/api/office/semester";
export default {
  name: "Class",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      names: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 班级表格数据
      classList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        className: null,
        studayYear: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
      },
      selectData: {
        studayYear: null,
        semNames: [],
        gradeName: null,
        gradeNames: [],
        isUpdateSelectGrade: false,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        className: [
          { required: true, message: "所属班级不能为空", trigger: "blur" },
        ],
        studayYear: [
          { required: true, message: "所属学年不能为空", trigger: "blur" },
        ],
      },
      semCasProp: {
        label: "name",
        children: "children",
        value: "id",
        expandTrigger: "hover",
      },
      semName: null,
      semKeyValue: 0,
      semNames: [],
      upload: {
        // 是否显示弹出层（数据导入）
        open: false,
        // 弹出层标题（数据导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/office/class/importData",
      },
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.initSelector();
  },
  methods: {
    /** 查询班级列表 */
    getList() {
      this.loading = true;
      let isStudayYear = this.queryParams.studayYear;
      if (isStudayYear) {
        let studayYearArr = isStudayYear.split("-");
        this.queryParams.startYear = studayYearArr[0];
        this.queryParams.endYear = studayYearArr[1];
      } else {
        this.queryParams.startYear = null;
        this.queryParams.endYear = null;
      }

      listClass(this.queryParams).then((response) => {
        this.classList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    changeSemesterQueryClass(semName) {
      this.getList();
    },
    changeSemesterSetId(id) {
      this.form.semId = id;
      this.$forceUpdate();
    },
    initSelector() {
      this.getSemesterNameSelector();
    },
    getSemesterNameSelector() {
      allName().then((response) => {
        let { rows } = response;
        this.semNames = rows;
        console.log(this.semNames);
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
        semName: null,
        gradeName: null,
        studayYear: null,
        semId: null,
        className: null,
      };

      this.semNames = null;
      this.queryParams.studayYear = null;
      this.queryParams.isPass = null;
      this.resetForm("form");
      this.initSelector();
      this.$forceUpdate();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.reset();
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      console.log(selection);
      this.ids = selection.map((item) => item.id);
      this.names = selection.map(
        (item) => item.startYear + "-" + item.endYear + "学年" + item.className
      );
      console.log(this.names);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.handleQuery();
      this.open = true;
      this.title = "添加班级";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;

      getClass(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.form.studayYear = response.data.semesterSelectorVo.studayYear;
        this.form.semId = response.data.semesterSelectorVo.id;
        this.title = "修改班级";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateClass(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.$refs.form.resetFields();
              this.getList();
            });
          } else {
            addClass(this.form).then((response) => {
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
      console.log(row);

      const ids = row.id || this.ids;
      let names;
      if (row.startYear && row.endYear && row.className) {
        names = row.startYear + "-" + row.endYear + "学年" + row.className;
      } else {
        names = this.names.join(",");
      }
      console.log(this.names.join(","));

      this.$confirm('是否确认删除"' + names + '"?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delClass(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "/system/office/class/export",
        {
          ...this.queryParams,
        },
        `office_class.xlsx`
      );
    },

    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "班级导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download(
        "system/office/class/importTemplate",
        {
          ...this.queryParams,
        },
        `class_${new Date().getTime()}.xlsx`
      );
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },

    getSemesterNamesSelector() {
      getSemesterNames1().then((response) => {
        let { rows } = response;
        this.semNames = rows;
      });
    },
    parseSemesterRange(startYear, endYear, separator) {
      return (
        new Date(startYear).getFullYear() +
        separator +
        new Date(endYear).getFullYear()
      );
    },
  },
  watch: {},
};
</script>