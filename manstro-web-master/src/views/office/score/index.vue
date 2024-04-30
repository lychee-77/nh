<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="treeFilterClassName"
            placeholder="请输入班级名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
<el-tree 
:data="gradeTreeData" 
:props="treeProps" 
:filter-node-method="filterNode"
@node-click="handleNodeClick" 
 default-expand-all
 highlight-current
 :expand-on-click-node="false"
 ref="tree"></el-tree>
     </div>
      </el-col>
     <el-col :span="20" :xs="24">
    <el-form :model="queryParams" ref="queryForm"  :inline="true" v-show="showSearch" label-width="68px">
      
      
      <el-form-item label="学生等级" >
         <el-select v-model="queryParams.isPass" placeholder="请选择查询方式" clearable  >
              <el-option v-for="item in this.selectData.isPassTypes"
              :key="item.id"
              :label="item.label"
              :value="item.id"></el-option>
            </el-select>
      
      </el-form-item>
      <el-form-item label="选择学生">
         <el-cascader
          v-model="queryParams.studentName"
          :key="classKeyValue"
          :options="studentNames"    
          separator="/"
           show-all-levels
          
        :props="stuCasProp"></el-cascader>
          
      </el-form-item>
     
      <el-form-item label="所属学期">
          <el-select v-model="queryParams.scoreSemName" placeholder="请输入成绩所属学期" filterable clearable  class="queryStudentNamne" 
          @change="changeQueryScoreSem">
              <el-option
                v-for="item in this.updateSemNames"
                :key="item.id"
                :label="item.studayYear"
                :value="item.id">
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
          v-hasPermi="['office:score:add']"
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
          v-hasPermi="['office:score:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="single"
          @click="handleDelete"
          v-hasPermi="['office:score:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          
          v-hasPermi="['office:score:export']"
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

    <el-table v-loading="loading" :data="scoreList" @selection-change="handleSelectionChange" style="width: 100%" :row-key="getRowId"
    @expand-change="expandRowGetScoreData"
    > 
          <el-table-column type="selection" width="55" align="center" />
           <el-table-column label="序号" align="center" prop="studentNum" show-overflow-tooltip sortable/>
      <el-table-column type="expand" >
      <template slot-scope="props">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item :label="item.courseName" v-for="item in props.row.courseScoreList" :key="item.courseId">
            <span>{{ item.score }}</span>
          </el-form-item>
          
        </el-form>
      </template>
    </el-table-column>

 
      <el-table-column label="学生姓名" align="center" prop="studentName" />
      
      <el-table-column label="成绩所属学年" align="center" prop="" :show-overflow-tooltip="true" >
        <template slot-scope="scope">
          {{ parseSemesterRange (scope.row.startYear,scope.row.endYear,"-")}}
        </template>
      </el-table-column> 
      <el-table-column label="当前所属班级" align="center" prop="className" />
      <el-table-column label="总分" align="center" prop="totalScore" sortable />
      <el-table-column label="平均分" align="center" prop="avgScore" sortable >
        <template slot-scope="scope">
          {{scope.row.avgScore | numFilter}}
        </template>
      </el-table-column>
      <el-table-column label="等级" align="center" prop="grade" >
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.grade == 4">
            A
          </el-tag>
          <el-tag  v-else-if="scope.row.grade == 3">
            B
          </el-tag>
          <el-tag type="warning" v-else-if="scope.row.grade == 2">
            C
          </el-tag>
          <el-tag type="danger" v-else-if="scope.row.grade == 1">
            D
          </el-tag>
        </template>
      </el-table-column>  

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['office:score:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['office:score:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
     </el-col>
    </el-row>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改分数对话框 -->
    <el-dialog :title="title" ref="updateDialog" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false"
     @open="renderCourseFOrmItem">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        
      
      <el-form-item label="学生名称" prop="updateStudentName">
        <el-cascader
          v-model="form.updateStudentName"
          :key="classKeyValue"
          :options="studentNames"    
          separator="/"
           show-all-levels
          @change="changeStudentCascader"
        :props="semCasProp"></el-cascader>
      
      </el-form-item>
     <el-form-item label="学期名称" prop="studayYear" v-if="!isUpdate">
            <el-select v-model="form.studayYear" placeholder="请选择所属学期" filterable  
              @change="changeUpdateSemester">
              <el-option
                v-for="item in this.updateSemNames"
                :key="item.id"
                :label="item.studayYear"
                :value="item.id">
              </el-option>
            </el-select>
      </el-form-item> 
  
      <el-form-item :label="course.couName"  v-for="course in selectData.courseNames" :key="course.id" :prop="course.note"  >
       <el-input v-model="form[course.note]" :placeholder="'请输入'+ course.couName + '分数'" type="template" />
      </el-form-item>    
        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
     <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
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
          <!-- <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据 -->
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
<style scoped>
 .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
   /* 解决出现模板问题 */
        [v-cloak] {
          display: none;
}
</style>
<script>
import { getToken } from "@/utils/auth";
import { listScore, getScore, delScore, addScore, updateScore,studentScoreInfo,studentScoreDetails } from "@/api/office/score";
import {getStudentAllName} from '@/api/office/student'
import {getClassAllName,getGradeAllName} from '@/api/office/class'
import {getCourseAllName} from '@/api/office/course'
import {treeGrade,allName} from '@/api/office/semester'
export default {
  name: "Score",
  components: {
  },
  data() {
    let checkStartTime = (rule, value, callback) => {
      if (value) {       
       let selectTimestamp = new Date(value).getTime()
       let timeStamp = new Date().getTime();
       if(selectTimestamp < timeStamp){
         callback();
       }else{
          callback(new Error("考试时间不能高于现在的日期"));
       }
     } else {
       callback(new Error("考试时间不能为空"))
     }
    };
     var  checkScore= (rule,value,callback) => { 
       let isNumber = /^1?[1-9]?\d([.]\d)?/.test(value);
      if(isNumber){
        let parseNumber = window.parseFloat(value);
        if(parseNumber >= 0 && parseNumber <= 100){
          callback();
        }else{
          callback(new Error("分数必须大于等于0且小于等于100"));
        }

      }else{
        callback(new Error("请输入合理的分数"));
      }
      
    };

   
    
    return {
  
      checkScoreRuleArr: [
          { required: true, message: "分数不能为空", trigger: "change" },
          { validator:
                checkScore,  trigger: "blur" },
        ],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 删除的提示文字
      nameData:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 分数表格数据
      scoreList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      gradeTreeData: [],
      treeProps: {
        "label": "name",
        "children": "children"
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startTime: null,
        couName: null,
        isPass: null,
        studentName: null,
        className: null,
        score: null,
        semName: null,
        scoreSemId:null,
        scoreSemName:null
      },
      // 表单参数
      form: {
        
      },
      formModelData:{},
      // 当前操作是否是更新操作，默认是新增操作,控制表单部分组件禁用
      isUpdate: false,
      selectData: {
        // 新增,修改下拉框选中值
        
        studentName: null,
        updateStudentName: null,
        courseNames: [],
        courseName: null,
        updateCourseName:null,
        updateClassName: null,
        semesterName: null,
        updateSemesterName:null,
        isPassType: 0,
        isPassTypes: [
          {
            id: 4,
            label: "A"
          },
          {
            id: 3,
            label: "B"
          },{
            id: 2,
            label: "C"
          },
          {
            id: 1,
            label: "D"
          }
        ],
        
      },
    studentNames: [],
    classKeyValue:0,
      // 表单校验
      rules: {
        studayYear: [
          { required: true, message: "所属班级不能为空", trigger: "change" }
        ],
        updateStudentName: [
          { required: true, message: "学生姓名不能为空", trigger: "change" }
        ],1:[
          { required: true, message: "分数不能为空", trigger: "change" },
          { validator:checkScore,  trigger: "blur" },
        ]
      },
      treeFilterClassName : null,
      semCasProp: {
            "label": "name",
            "children" : "children",
            "value": "id",
            expandTrigger: 'hover'
        },
       stuCasProp: {
            "label": "name",
            "children" : "children",
            "value": "name",
            expandTrigger: 'hover'
        },
        semName: null,
        updateSemName: null,
        updateSemKeyValue: 0,
        semKeyValue: 0,
        semNames: [],
        updateSemNames: [],
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
        url: process.env.VUE_APP_BASE_API + "/system/office/score/importData"
      }
        
    }
    
  },
  created() {
    this.getList();
  },
  mounted(){
    this.initSelector();
     this.$refs.updateDialog.rendered=true
  },
  methods: {
    
 changeUpdateCourse(courseId){
        this.form.courseId = courseId
    },
    changeUpdateStudent(studentId){
      this.form.stuId = studentId;
    },
    /** 查询分数列表 */
    getList() {
      this.loading = true;
      let stuNames = this.queryParams.studentName;
      if(stuNames){
         this.queryParams.studentName = stuNames[stuNames.length -1]
      }
      
      studentScoreInfo(this.queryParams).then(response => {
        let {rows} = response;
        this.scoreList = rows;
        this.total = response.total;
        this.loading = false;
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.resetForm("form");
      this.form = {
       
        studayYear: null,
        updateSemName:null,
        updateStudentName:null,
        updateCourseName:null,
         updateClassName: null,
           updateSemesterName:null,
        id: null,
        courseName: null,
        studentName: null,
        stuId:null,
        courseScoreList:[]
      };


      this.selectData = {
        studentNames: [],
        studentName: "",
        updateStudentName: null,
        courseNames: this.selectData.courseNames,
        courseName: null,
        updateCourseName:null,
         classNames: [],
        className: null,
        updateClassName: null,
        semesterName: null,
        semNames: [],
        
        updateSemesterName:null,
        isPassType: 0  ,
          isPassTypes: [
          {
            id: 0,
            label: "未通过"
          },{
            id: 1,
            label: "已通过"
          }
        ]
      };
      this.semName = null;
      this.semNames = null;
      this.queryParams.scoreSemName = null;
      this.queryParams.scoreSemId = null; 
      this.queryParams.semId = null;
      this.queryParams.studentName = null;
      this.queryParams.classId = null;
      this.queryParams.gradeName = null
      this.queryParams.couName= null;
      this.queryParams.isPass = null
      this.treeFilterClassName = null;


      
      this.$refs['queryForm'].resetFields()
      this.$refs['form'].resetFields();
      this.initSelector();
    // this.$refs['form'].resetFields()
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
      this.ids = selection.map(item => {
      return {"id" :item.id,"semId":item.semId}
      })
      this.nameData = selection.map(item => {
        return {"stuName":item.studentName,"studayYear":item.startYear + "-" + item.endYear}
      })
      
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.isUpdate = false;
      this.open = true;
      this.title = "添加分数";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.isUpdate = true;
      console.log(row)
      const id = row.id || this.ids[0].id
      const semId = row.semId || this.ids[0].semId;
      getScore(id,semId).then(response => {
        let {data} = response;
        this.form = data;
        let arr = []
        arr.push(data.classSemId)
        arr.push(data.classId)
        arr.push(id)
        this.form.updateStudentName = arr
        this.$forceUpdate();
        this.form.stuId = id;
        this.form.semId = semId;
        this.form.studayYear = this.form.schoolYear
        let courseScore = data.courseScoreList
        console.log(data)
        for(let item in courseScore){
            let cou = courseScore[item]
            this.$set(this.form,cou.note,cou.score)
            this.formModelData[cou.note] = cou.courseId
        }
        
        this.$forceUpdate();
        this.open = true;
        this.title = "修改分数";
      });
      // 获取到状态,并设置为禁用后,强制渲染视图 
      this.$forceUpdate();
    },
    /** 提交按钮 */
    submitForm() {
      // if(!this.isUpdate){
      //   this.form.stuId = this.form.updateStudentName;
      // }

      let courseList = this.formModelData;
      this.form.courseScoreList = []
      for(let model in courseList ){
        let course = {}
        
        course["courseId"] = courseList[model]
        course["score"] = this.form[model]
        this.form.courseScoreList.push(course)
      }
      
      this.$refs["form"].validate((valid,a) => {
         let stuName = this.form.updateStudentName
            if(stuName){
              this.form.stuId = stuName[stuName.length - 1]
            }
        if (valid) {
          if (this.isUpdate) {
           
            updateScore(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            })
          } else {
            addScore(this.form).then(response => {
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
      console.log(row)
      const ids = row.id || this.ids[0].id;
      const semIds = row.semId || this.ids[0].semId

      let removeName;
     if(row.startYear && row.endYear && row.studentName){
          removeName = row.startYear +"-"+ row.endYear + "学年" + row.studentName
      }else{
          removeName = this.nameData[0].studayYear +"学年" + this.nameData[0].stuName
      } 
     
      this.$confirm('是否确认删除"' + removeName  + '"的成绩项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delScore(ids,semIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('/system/office/score/export', {
        ...this.queryParams
      }, `office_score.xlsx`)
    },
    initSelector(){
        this.getGradeTree();
        this.getSemesterAllName();
        this.getStudentNamesSelector();
        this.getCourseNameSelector();
    },
    getGradeTree(){
      treeGrade().then(response => {
         let {rows} = response;
        let obj = {
          id: -1,
          name:"全部",
          children : rows
        }
        let treeArr = []
        treeArr.push(obj)

        this.gradeTreeData = treeArr;
      })
    },
    getSemesterAllName(){
      allName().then(response => {
        let {rows} = response;
        this.updateSemNames = rows;
      })
    },
    getStudentNamesSelector(){
        let queryStudentParams = {
          'className' : this.queryParams.className,
          "semName" : this.queryParams.semName,
          "gradeName" : this.queryParams.gradeName
        }
        getStudentAllName(queryStudentParams).then((response) => {
          let {rows}  = response;
          this.studentNames = rows;
          
        })
    },
    
    getCourseNameSelector(){
      
        getCourseAllName().then((response) => {
          let {rows}  = response;
          this.selectData.courseNames = rows;
          let couNames = this.selectData.courseNames
          for(let couName in couNames){
            let ruleKey = couNames[couName].note
            let ruleObj  =  this.checkScoreRuleArr;
          this.rules[ruleKey] = ruleObj
          // 赋值初始值放在了reset方法中
          }
          this.$refs["form"].clearValidate();
          this.$forceUpdate();
        })
    },
    getUpdateGradeNameSelector(semName){
        if(semName){
          let classQuery = {
          'semName' : semName
        }
         getGradeAllName(classQuery).then(response => {
          let {rows} = response;
           // semName 不为空 班级下拉框可以选择发送异步请求
        
          this.selectData.isUpdateSelectGrade = true;
          this.selectData.gradeNames = rows;
          this.$forceUpdate();     
      })
    } else{
            this.selectData.isUpdateSelectGrade = false;
            this.form.graId = null;
            this.selectData.gradeName = null;
            this.selectData.className = null;
            this.form.classId = null;
            this.selectData.isUpdateSelectClass = false;
            this.$forceUpdate();  
        }
    },
    getUpdateClassNameSelector(graId){
        if(graId){
          // semName 不为空 班级下拉框可以选择发送异步请求
          this.selectData.isUpdateSelectClass = true;
          let classQuery = {
          'semName' : this.selectData.semName,
          "graId" : graId
        }
      getClassAllName(classQuery).then(response => {
        let {rows} = response;
        this.selectData.classNames = rows;
          this.$forceUpdate();     
      })
        }else{
            this.selectData.isUpdateSelectClass = false;
            this.form.classId = null;
            this.$forceUpdate();  
        }
    },
    getGradeNameSelector(){
      
         getGradeAllName().then(response => {
          let {rows} = response;
           // semName 不为空 班级下拉框可以选择发送异步请求
          this.selectData.isSelectGrade = true;
          // this.selectData.isUpdateSelectGrade = true;
          this.selectData.gradeNames = rows;
          this.$forceUpdate();     
      })
    
    },
    getClassNameSelector(gradeName){
       this.queryParams.className = null;
        if(gradeName){
          // semName 不为空 班级下拉框可以选择发送异步请求
          this.selectData.isSelectClass = true;
          this.selectData.isUpdateSelectClass = true;
          let classQuery = {
          'semName' : this.queryParams.semName,
          "gradeName" : gradeName
        }
      getClassAllName(classQuery).then(response => {
        let {rows} = response;
        this.selectData.classNames = rows;
          this.$forceUpdate();     
      })
        }else{
            this.selectData.isSelectClass = false;
           
            this.$forceUpdate();  
        }
    },
     renderCourseFOrmItem(){
      
      //  this.$refs["form"].clearValidate();
    },
    
    // 设置
    changeUpdateSemester(id){
        this.form.semId = id;
        this.$forceUpdate();
    },
     // 筛选节点
    filterNode(value, data) {
    if (!value) return true;

        if(data.name){
          return data.name.indexOf(value) !== -1;
        }
    },
    // 节点单击事件
    handleNodeClick(data) {
      // 有子节点,说明选择的是年级
      
      if(data['children'])
      {

        if(data.id == -1){
            this.queryParams.startYear = null
            this.queryParams.endYear = null
            this.queryParams.classId =null;   
        }else{
            let schoolYear = data.name.split('-');
            this.queryParams.startYear = schoolYear[0]
            this.queryParams.endYear = schoolYear[1];
            this.queryParams.classId =null;
        
        }
        
      }else{
        
        // 没有children说明点击的是班级节点
        this.queryParams.classId = data.id;
        this.queryParams.startYear = null;
        this.queryParams.endYear = null;
      }
      
      this.getList();
    },
    getRowId(row){
        return row.scoreIds;
    },
    changeSemCasHandler(selectArray)  {
      this.queryParams.semId = selectArray[selectArray.length - 1];
      this.getList();
    },
    changeUpdatesemCasHandler(selectArray){
      
      this.form.semId = selectArray[selectArray.length - 1];
    },
    parseSemesterRange (startYear,endYear,separator){
      return new Date(startYear).getFullYear() + separator + new Date(endYear).getFullYear();
    },
    expandRowGetScoreData(row){
    },
    changeQueryScoreSem(id){
      this.queryParams.scoreSemId = id;
      this.getList();
    },
    changeStudentCascader(id){
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "班级导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/office/score/importTemplate', {
        ...this.queryParams
      }, `score${new Date().getTime()}.xlsx`)
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
    }

    

  },
  filters: {
      numFilter(value) {

  // 截取当前数据到小数点后两位
    
    let realVal = Number(value).toFixed(2)
    // num.toFixed(2)获取的是字符串

    return realVal

  }
  },
  watch: {
      treeFilterClassName(val) {
        this.$refs.tree.filter(val);
      },
      // 级联选择器必须监听该数据属性,否则当数据发生变化会抛异常
    semNames(value) {
      this.semKeyValue++ //只要监听到数据源发生变化 ，改变keyValue的值，达到重新渲染的效果
    },
    updateSemNames(value){
      this.updateSemKeyValue ++;
    },
      studentNames(value) {
      this.classKeyValue++ //只要监听到数据源发生变化 ，改变keyValue的值，达到重新渲染的效果
    }

    }
      
};
</script>