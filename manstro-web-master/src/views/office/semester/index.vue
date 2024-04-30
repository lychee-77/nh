<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入学年份" prop="semName">
      <el-date-picker
      v-model="queryParams.startYear"
      type="year"
      placeholder="选择入学年份" 
      format="yyyy"
      value-format="yyyy"
      @change="selectYearMethod">
    </el-date-picker>
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
          v-hasPermi="['office:semester:add']"
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
          v-hasPermi="['office:semester:edit']"
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
          v-hasPermi="['office:semester:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['office:semester:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
          <el-button
        type="info"
        icon="el-icon-upload2"
        size="mini"
        @click="handleImport"
      >导入</el-button>
      </el-col>
    
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="semesterList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学年" align="center" prop="" >
        <template slot-scope="scope">
          {{ parseSemesterRange (scope.row.startYear,scope.row.endYear,"-")}}
        </template>
      </el-table-column> 

      
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['office:semester:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['office:semester:remove']"
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

    <!-- 添加或修改学期管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
    <el-form-item label="入学年份" prop="startYear">
        <el-date-picker
            v-model="form.startYear"
            type="year"
            format="yyyy"
            value-format="yyyy"
             placeholder="选择入学年份"
        >
        </el-date-picker>
        
      </el-form-item>

        
       
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
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
	  <!-- <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据 -->
	  <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
	</div>
	<div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
  </el-upload>
  <div slot="footer" class="dialog-footer">
	<el-button type="primary" @click="submitFileForm">确 定</el-button>
	<el-button @click="upload.open = false">取 消</el-button>
  </div>
</el-dialog>
  </div>
</template>

<script>
import { listSemester, getSemester, delSemester, addSemester, updateSemester } from "@/api/office/semester";
import { getToken } from "@/utils/auth";
// 导入模板接口importTemplate
import { importTemplate } from "@/api/office/semester";
export default {
  name: "Semester",
  components: {
  },
  computed : {
    

  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      names:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 学期管理表格数据
      semesterList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startYear: null,
        endYear: null
      },
       
      // 表单参数
      form: {
        yearRange: new Date().getFullYear() + '',
      },
      // 表单校验
      rules: {
        startYear: [
          { required: true, message: "学期名称不能为空", trigger: "blur" }
        ]
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/office/semester/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询学期管理列表 */
    getList() {
      this.loading = true;
      
      if(this.queryParams.startYear){
        this.queryParams.endYear = window.parseInt(this.queryParams.startYear) + 1;
      }else{
         this.queryParams.endYear = null;
      }
      listSemester(this.queryParams).then(response => {
        let {rows}  = response;
        this.semesterList = rows;
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
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        startYear: null
      };
      this.resetForm("form");
      // 查询条件不制空
      this.queryParams.startYear = null;
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
      this.names = selection.map(item => item.startYear + '-' +item.endYear)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.handleQuery();
      this.open = true;
      this.title = "添加学期管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSemester(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改学期管理";
      });
    },
  
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
   
        // 封装为学期
        if (valid) {
            this.form.endYear = window.parseInt(this.form.startYear) + 1;
            console.log(this.form)
          if (this.form.id != null) {
            updateSemester(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSemester(this.form).then(response => {
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
      const names = row.startYear + row.endYear || this.names.join(",")
      this.$confirm('是否确认删除学期名称为["' + names + '"]的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSemester(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('/system/office/semester/export', {
        ...this.queryParams
      }, `office_semester.xlsx`)
    }
    ,
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "学期导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
         this.download('system/office/semester/importTemplate', {
        ...this.queryParams
      }, `semester_${new Date().getTime()}.xlsx`)
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
       parseSemesterRange : function (startYear,endYear,separator){
      return new Date(startYear).getFullYear() + separator + new Date(endYear).getFullYear();
    }  ,
    selectYearMethod(vm){
      this.handleQuery();
    }
  },
 
};
</script>