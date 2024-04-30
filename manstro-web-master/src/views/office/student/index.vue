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
:data="classTreeData" 
:props="semCasProp" 
:highlight-current="true"
:filter-node-method="filterNode"
@node-click="handleNodeClick" 
 default-expand-all
 :expand-on-click-node="false"
 ref="tree"></el-tree>
     </div>
      </el-col>
    <el-col :span="20" :xs="24">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px"  >
      <el-form-item label="学号" prop="stuNum"  >
        <el-input
          v-model="queryParams.stuNum"
          placeholder="请输入学号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学生姓名" prop="stuName">
        <el-input
          v-model="queryParams.stuName"
          placeholder="请输入学生姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
       
            <el-form-item label="年龄" prop="age">
        <el-input
          v-model="queryParams.age"
          placeholder="请输入年龄"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

     
      <el-form-item label="身份证号" prop="stuIdNumber">
        <el-input
          v-model="queryParams.stuIdNumber"
          placeholder="请输入身份证号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="性别" prop="stuSex">
        <el-select v-model="queryParams.stuSex" placeholder="请选择性别" clearable size="small">
          <el-option
            v-for="dict in stuSex"
            :key="dict.value"
            :label="dict.name"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="民族" prop="nation">
    
          <el-select v-model="queryParams.nation" placeholder="请选择你的民族" filterable clearable>
            <el-option
              v-for="item in nation"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
      </el-form-item>
     
      <el-form-item label="出生日期" prop="stuBirthday" >
       
    <el-date-picker
      v-model="queryParams.birthdayRange"
      type="monthrange"
      range-separator="至"
      start-placeholder="开始月份"
      end-placeholder="结束月份" unlink-panels format="yyyy-MM" editable clearable >
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
          v-hasPermi="['office:student:add']"
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
          v-hasPermi="['office:student:edit']"
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
          v-hasPermi="['office:student:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['office:student:export']"
        >导出</el-button>
      </el-col>
     <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['office:class:import']"
        >导入</el-button>
    </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> 

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"  fixed="left" />
      <el-table-column label="学号" align="center" prop="stuNum" :show-overflow-tooltip="true"
      fixed="left"
 />
      <el-table-column label="照片" align="center" prop="photo" width="100">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.photo" ></el-avatar>
          <!-- <img :src="scope.row.photo"  width="70"/> -->
        </template>
      </el-table-column>
      <el-table-column label="学生姓名" align="center" prop="stuName" />
        
   
       <el-table-column label="学年" align="center" prop="" :show-overflow-tooltip="true" >
        <template slot-scope="scope">
          {{ parseSemesterRange (scope.row.startYear,scope.row.endYear,"-")}}
        </template>
      </el-table-column> 
      <el-table-column label="班级名称" align="center" prop="className" />
      
      <el-table-column label="出生日期" align="center" prop="stuBirthday" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.stuBirthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
     
      <el-table-column label="身份证号" align="center" prop="stuIdNumber" :show-overflow-tooltip="true"
 />
 <el-table-column label="出生日期" align="center" prop="stuBirthday" width="180" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.stuBirthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
  <el-table-column label="性别" align="center" prop="stuSexChar" :formatter="stuSexFormat" >
          <template slot-scope="scope">
            {{scope.row.stuSex == '1' ? '男' : '女'}}
            </template>
      </el-table-column>
      <el-table-column label="年龄" align="center" prop="age" sortable  />
      
    
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['office:student:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['office:student:remove']"
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

    <!-- 添加或修改学生对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="学号" prop="stuNum">
          <el-input v-model="form.stuNum" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="学生姓名" prop="stuName">
          <el-input v-model="form.stuName" placeholder="请输入学生姓名" />
        </el-form-item>
      
     
      <el-form-item label="班级名称" prop="classNameModel">
           <el-cascader
          v-model="form.classNameModel"
          :key="classKeyValue"
          :options="classCascaderData"    
          separator="学年"
           show-all-levels

          @change="changeSemCasHandler"
        :props="semCasProp"></el-cascader>
            
      </el-form-item>
       <el-form-item label="民族" prop="nation">
          <el-select v-model="form.nation" placeholder="请选择你的民族" filterable >
            <el-option
              v-for="item in nation"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
       
       <el-form-item label="身份证号" prop="stuIdNumber">
          <el-input v-model="form.stuIdNumber" placeholder="请输入身份证号" />
        </el-form-item>

 <el-form-item label="照片" >
              <imageUpload v-model="form.photo" @input="removeFile"/>
           </el-form-item>

             <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="出生日期" prop="stuBirthday">
          <el-date-picker clearable size="small"
            v-model="form.stuBirthday"
            type="date"
            value-format="yyyy-MM-dd"
            :disabled="true"
            placeholder="输入身份证号自动获取">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="性别" prop="stuSexChar">
          <el-select v-model="form.stuSexChar" placeholder="输入身份证号自动获取" @change="changeSex"
          :disabled="true">
            <el-option
              v-for="dict in stuSex"
              :key="dict.value"
              :label="dict.name"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
       
       
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age" placeholder="输入身份证号自动获取" :disabled="true" />
        </el-form-item>
            
      
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
        <!-- 班级导入对话框 -->
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

<script>
import { getToken } from "@/utils/auth";
import { listStudent, getStudent, delStudent, addStudent, updateStudent } from "@/api/office/student";
import {getClassAllName,getGradeAllName} from '@/api/office/class'
import ImageUpload from '@/components/ImageUpload'
import {getSemesterNames1,treeGrade} from '@/api/office/semester'
export default {
  name: "Student",
  components: {
    ImageUpload
  },
  data() {
    
    let checkBirthday = (rule, value, callback) => {
      if (value) {       
       let selectTimestamp = new Date(value).getTime()
       let timeStamp = new Date().getTime();
       if(selectTimestamp < timeStamp){
         callback();
       }else{
          callback(new Error("出生日期不能高于现在的日期"));
       }
     } else {
       callback(new Error("出生日期不能为空"))
     }
    };

    const checkIdNumber = (rule, value, callback) =>{
    // 1 "验证通过!", 0 //校验不通过
        let format = /^(([1][1-5])|([2][1-3])|([3][1-7])|([4][1-6])|([5][0-4])|([6][1-5])|([7][1])|([8][1-2]))\d{4}(([1][9]\d{2})|([2]\d{3}))(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))\d{3}[0-9xX]$/;
        //号码规则校验
        if(!format.test(value)){
            return callback(new Error("请输入正确的身份证号码"));
        }
        //区位码校验
        //出生年月日校验   前正则限制起始年份为1900;
        var year = value.substr(6,4),//身份证年
            month = value.substr(10,2),//身份证月
            date = value.substr(12,2),//身份证日
            time = Date.parse(month+'-'+date+'-'+year),//身份证日期时间戳date
            now_time = Date.parse(new Date()),//当前时间戳
            dates = (new Date(year,month,0)).getDate();//身份证当月天数
        if(time>now_time||date>dates){
            return callback(new Error("出生日期不合规"))
        }
          this.go(18);
        callback();
    }

    const checkAge = (rule, value, callback) => {
        var age = parseInt(value);
        let pattern = /^(([0-9]|[1-9][1-9]|1[0-7][0-9])(\\.[0-9]+)?|180)$/;
         if(pattern.test(age)){
            return callback(new Error("年龄输入不合法"))
         }
        if(value > 0 && value < 150 ){
          callback(new Error("年龄范围一般在0~150之间"));
          return callback();
        }

        return callback();
    };
    return {
      stuSex: [
        {
          name: '男',
          value: 1
        },{
          name: '女',
          value: 0
        }
      ],
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
      // 学生表格数据
      studentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 性别字典
      stuSexOptions: [],
      nation:  [
        "汉族","蒙古族","回族","藏族","维吾尔族","苗族","彝族","壮族","布依族","朝鲜族","满族","侗族","瑶族","白族","土家族",  
               "哈尼族","哈萨克族","傣族","黎族","傈僳族","佤族","畲族","高山族","拉祜族","水族","东乡族","纳西族","景颇族","柯尔克孜族",  
               "土族","达斡尔族","仫佬族","羌族","布朗族","撒拉族","毛南族","仡佬族","锡伯族","阿昌族","普米族","塔吉克族","怒族", "乌孜别克族",  
              "俄罗斯族","鄂温克族","德昂族","保安族","裕固族","京族","塔塔尔族","独龙族","鄂伦春族","赫哲族","门巴族","珞巴族","基诺族"],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stuNum: null,
        stuName: null,
        className: null,
        gradeName: null,
        stuBirthday: null,
        birthdayRange: null,
        stuIdNumber: null,
        semName: null,
        nation: null,
        age: null,
        photo: null,
        note: null,
      },
      // 表单参数
      form: {},  
      selectData: {
        semName: null,
        semNames : [],
        classNames: [],
        },
        classTreeData:[],
        classCascaderData:[],
      
      // 表单校验
      rules: {
        stuNum: [
          { required: true, message: "学号不能为空", trigger: "blur" },
          {validator:function(rule,value,callback){
	            if(/^\d{5,}$/.test(value) == false){
	                callback(new Error("请输入正确的5位学号"));
	            }else{
	                callback();
	            }
          }, trigger: 'blur'}
          ]
        ,
        stuName: [
          { required: true, message: "学生姓名不能为空", trigger: "blur" },
          { min: 2, max: 5, message: '姓名长度在 2 到 5 个字符', trigger: 'blur' }
        ],
        classNameModel: [
          { required: true, message: "班级名称不能为空", trigger: "blur" }
        ],
        stuIdNumber:[
          { required: true, message: "出生日期不能为空", trigger: "blur" },                               
          { validator : checkIdNumber ,trigger: "blur"}
        ],
     
        nation: [
          { required: true, message: "民族不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "出生日期不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建者不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "业务删除不能为空", trigger: "blur" }
        ],
        nation: [
          { required: true, message: "创建者不能为空", trigger: "blur" }
          ]
      },
       treeFilterClassName : null,
      semCasProp: {
            "label": "name",
            "children" : "children",
            "value": "id",
            expandTrigger: 'hover'
        },
        semName: null,
        classKeyValue: 0,
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
        url: process.env.VUE_APP_BASE_API + "/system/office/student/importData"
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_user_sex").then(response => {
      this.stuSexOptions = response.data;
    });
  },
  mounted(){
    this.initSelector();
  },
  methods: {
    /** 查询学生列表 */
    getList() {
      this.loading = true;
      let age = this.queryParams.age;
        if(age){
        let pattern = /^(?:[1-9][0-9]?|1[01][0-9]|150)$/;;
        console.log(pattern.test(age))
         if(!pattern.test(age)){
           this.msgError("年龄一般在0~150之间");
          this.loading = false;
          return false;
         }
      

        }
      listStudent(this.queryParams).then(response => {
        this.studentList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    

    getClassTree(){
      treeGrade().then(response => {
        let {rows} = response;
        let obj = {
          id: -1,
          name:"全部",
          children : rows
        }
        let treeArr = []
        treeArr.push(obj)
        /// 树形控件设置统一的父级
        this.classTreeData = treeArr;
        // 级联框不设统一的父级
        this.classCascaderData = rows;
      });
    },
    initSelector(){
        this.getClassTree();
    },
    getUpdateGradeNameSelector(semName){
      this.selectData.gradeName = null;
      this.form.classId = null;
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
            
            this.form.classId = null;
            this.selectData.isUpdateSelectClass = false;
            this.$forceUpdate();  
        }
    },
    getUpdateClassNameSelector(graId){
      this.form.classId = null;
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
            
            this.$forceUpdate();  
        }
    },
    getUpdateClassNameSelector(semName){
      this.queryParams.gradeName = null;
            this.queryParams.className = null;
        if(semName){
         
          let classQuery = {
          'semName' : semName
        }
         getGradeAllName(classQuery).then(response => {
          let {rows} = response;
           // semName 不为空 班级下拉框可以选择发送异步请求
          this.selectData.isSelectGrade = true;
          this.selectData.isUpdateSelectGrade = true;
          this.selectData.gradeNames = rows;
          this.$forceUpdate();     
      })
    } else{
            this.selectData.isSelectGrade = false;
            this.selectData.isUpdateSelectGrade = false;
            
            this.selectData.isSelectClass = false;
            this.selectData.isUpdateSelectClass = false;
            this.$forceUpdate();  
        }
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
    changeSex(id){
        this.form.stuSex = id;
    },
    // 性别字典翻译
    stuSexFormat(row, column) {
      return this.selectDictLabel(this.stuSexOptions, row.stuSex);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 实现自动生成生日，性别，年龄
    go(val) {
      
      let iden = this.form.stuIdNumber;
      let sex = null;
      let birth = null;
      let myDate = new Date();
      let month = myDate.getMonth() + 1;
      let day = myDate.getDate();
      let age = 0;
 
      if (val === 18) {
        age = myDate.getFullYear() - iden.substring(6, 10) - 1;
        sex = iden.substring(16, 17);
        birth =
          iden.substring(6, 10) +
          "-" +
          iden.substring(10, 12) +
          "-" +
          iden.substring(12, 14);
        if (
          iden.substring(10, 12) < month ||
          (iden.substring(10, 12) == month && iden.substring(12, 14) <= day)
        )
          age++;
      }
      
      this.form.stuSex = sex % 2 ; 
      this.form.stuSexChar = sex % 2 == 0 ? '女' : '男';                    
      this.form.age = age;
      this.form.stuBirthday = birth;
      // this.form.birthplace = this.area[iden.substring(0, 2)];
    },
 
    // 表单重置
    reset() {
      
      this.form = {
        id: null,
        stuNum: null,
        stuName: null,
        classId: null,
        stuBirthday: null,
        stuSex: null,
        stuIdNumber: null,
        nation: null,
        age: null,
        photo: null,
        note: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        delFlag: null
      };
      this.queryParams.birthdayRange = [],
      // this.queryParams.birthdayRange = null;
      this.selectData = {
        classNames: null
        }
      this.classTreeData = null
      this.classCascaderData = null
      this.queryParams.startBirthdayMonnt = null;
      this.queryParams.endBirthdayMonnt = null;
      this.queryParams.classId = null;
      this.queryParams.startYear = null;
        this.queryParams.endYear = null;
      this.resetForm("form");  
      this.initSelector();
      this.$forceUpdate();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      let timeStamp = this.queryParams.birthdayRange;
      if(timeStamp != null && timeStamp[0] && timeStamp[1]){
          this.queryParams.startBirthdayMonnt = this.getNowFormatDate(new Date(timeStamp[0]))
           this.queryParams.endBirthdayMonnt = this.getNowFormatDate(new Date(timeStamp[1]))
      }else{
        this.queryParams.startBirthdayMonnt = null;
        this.queryParams.endBirthdayMonnt = null;
      }
 
      this.queryParams.pageNum = 1;
      this.getList();
    },
      getNowFormatDate(date) {
       
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
       
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
       
        var currentdate = year + seperator1 + month  ;
        return currentdate;
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
      this.names = selection.map(item => item.stuName)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.handleQuery();
      this.open = true;
      this.title = "添加学生";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      console.log(row.id)
      const id = row.id || this.ids
      getStudent(id).then(response => {
        
        let {data} = response;
        this.form = data;
        let casArr = []
        casArr.push(undefined)
        casArr.push(data.classId)
        this.form.classNameModel = casArr
        console.log(casArr)
        
        this.selectData.isUpdateSelectGrade = false;
        this.selectData.isUpdateSelectClass = false;
        this.form.stuSexChar=data.stuSex % 2 == 1 ? '男' : '女'
        this.open = true;
        this.title = "修改学生";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStudent(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {

            addStudent(this.form).then(response => {
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
      const names = row.stuName || this.names.join(",")
      this.$confirm('是否确认删除学生名称为["' + names + '"]的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delStudent(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('/system/office/student/export', {
        ...this.queryParams
      }, `office_student.xlsx`)
    },    
/** 导入按钮操作 */
    handleImport() {
      this.upload.title = "班级导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/office/student/importTemplate', {
        ...this.queryParams
      }, `student_${new Date().getTime()}.xlsx`)
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
          // 点击的是全部学期
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
        return row.id;
    },
    changeSemCasHandler(selectArray){
      debugger
      this.form.classId = selectArray[selectArray.length - 1];
      this.getList();
    }
    , parseSemesterRange (startYear,endYear,separator){
      return new Date(startYear).getFullYear() + separator + new Date(endYear).getFullYear();
    },
    removeFile(fileUpload){
        this.form.photo = fileUpload;
    }
  },
  
  watch: {
      treeFilterClassName(val) {
        this.$refs.tree.filter(val);
      },
      classTreeData(value) {
      this.classKeyValue++ //只要监听到数据源发生变化 ，改变keyValue的值，达到重新渲染的效果
    }
    }
};
</script>