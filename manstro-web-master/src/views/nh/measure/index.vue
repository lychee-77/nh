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
        <el-form-item label="设备型号" prop="typeModel">
          <el-input
            v-model="queryParams.typeModel"
            placeholder="请输入设备型号"
            size="small"
          />
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

          <el-form-item label="能耗类型" prop="nhTypeModel">
            <el-select
              v-model="queryParams.nhTypeModel"
              placeholder="请选择能耗类型"
              filterable
              clearable
              @change="changeQueryParamsHandlerQuery"
            >
              <el-option
                v-for="item in this.selectData.types"
                :key="item.id"
                :label="item.label"
                :value="item.label"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="统计时间" prop="currentDate">
            <el-date-picker
              v-model="queryParams.currentDates"
              type="monthrange"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              unlink-panels
              format="yyyy-MM"
              editable
              clearable
            >
            </el-date-picker>
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
              v-hasPermi="['nh:statistics:add']"
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
              v-hasPermi="['nh:statistics:edit']"
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
              v-hasPermi="['nh:statistics:remove']"
              >删除</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleImport"
              v-hasPermi="['nh:statistics:export']"
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
          :data="statisticsList"
          @selection-change="handleSelectionChange"
          :default-sort = "{prop: 'consumeNum', order: 'descending'}"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="设备区域" align="center" prop="typeModel">
            <template slot-scope="scope">
              <span>{{ parseSpaceName(scope.row) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="设备型号" align="center" prop="typeModel" />
          <el-table-column
            label="统计日期"
            align="center"
            prop="currentDate"
            sortable
            width="180"
          >
            <template slot-scope="scope">
              <span>{{
                parseTime(scope.row.currentDate, "{y}-{m}-{d} ")
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="设备类型"
            align="center"
            prop="equipmentTypeModel"
          />
          <el-table-column label="能耗类型" align="center" prop="nhTypeModel" />
          <el-table-column label="总表值" align="center" prop="totalNum" sortable/>
          <el-table-column label="消耗值" align="center" prop="consumeNum" sortable />
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
                v-hasPermi="['nh:statistics:edit']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['nh:statistics:remove']"
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
      </el-col>
    </el-row>
  

    <!-- 添加或修改能耗统计对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="选择设备" prop="cascaderEquipmentModel">
          <el-cascader
            v-model="form.cascaderEquipmentModel"
            :key="equipmentKeyValue"
            :options="equipmentData"
            separator="-"
            show-all-levels
            @change="changeEquipmentCasHandler"
            :props="roomCasProp"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="统计日期" prop="currentDate">
          <el-date-picker
            clearable
            size="small"
            v-model="form.currentDate"
            type="date"
            
            value-format="yyyy-MM-dd"
            placeholder="选择统计日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="能耗类型" prop="nhTypeModel">
            <el-select
              v-model="form.nhTypeModel"
              placeholder="请选择能耗类型"
              filterable
              clearable
              @change="changeTypesHandlerUpdate"
            >
              <el-option
                v-for="item in this.selectData.types"
                :key="item.id"
                :label="item.label"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        <el-form-item label="总表值" prop="totalNum">
          <el-input v-model="form.totalNum" placeholder="请输入总表值" />
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
          <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据
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
import {
  listMeasure,
  getMeasure,
  delMeasure,
  addMeasure,
  updateMeasure,
} from "@/api/nh/measure";
import { getToken } from "@/utils/auth";
import { roomTreeList } from "@/api/nh/room";
import { getEquipTypeTreeList } from "@/api/nh/equipType";
import { getTypeTreeList } from "@/api/nh/type";
import { getEquipmentTreeList } from "@/api/nh/equipment";
export default {
  name: "Measure",
  components: {},
  data() {
    let checkCurrentDate = (rule, value, callback) => {
      if (value) {       
       let selectTimestamp = new Date(value).getTime()
       let timeStamp = new Date().getTime();
       if(selectTimestamp < timeStamp){
         callback();
       }else{
          callback(new Error("统计日期不能高于现在的日期"));
       }
     } else {
       callback(new Error("统计日期不能为空"))
     }
    };

       var  checkTotalNum = (rule,value,callback) => { 
       let isNumber = /^\d{1,}([\.]\d)?$/.test(value);
      if(isNumber){
          callback();
      }else{
        callback(new Error("请输入正确的值"));
      }
      
    }

     var  checkConsumerNum = (rule,value,callback) => { 
       let isNumber = /^\d{1,}([\.]\d)?$/.test(value);
      if(isNumber){
        let parseNumber = window.parseFloat(value);
        if(parseNumber <= this.form.totalNum){
          callback();
        }else{
          // 消耗值大于统计值
          callback(new Error("消耗值必须小于等于总值"));
        }

      }else{
        callback(new Error("请输入正确的值"));
      }
      
    }
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 能耗统计表格数据
      statisticsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipId: null,
        typeModel:null,
        currentDate: null,
        nhTypeId: null,
        totalNum: null,
        consumeNum: null,
        likeCurrentDate: [],
      },
      equipmentData: [],
      equipmentKeyValue: 0,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cascaderEquipmentModel: [
          { required: true, message: "设备不能为空", trigger: "blur" }
           
        ],
        currentDate: [
          { required: true, message: "统计日期不能为空", trigger: "blur" },
          { validator : checkCurrentDate ,trigger: "blur"}
        ],
        nhTypeModel: [
          { required: true, message: "能耗类型id不能为空", trigger: "blur" },
        ],
        totalNum: [
          { required: true, message: "总表值不能为空", trigger: "blur" },
          { validator : checkTotalNum ,trigger: "blur"}
        ],
        consumeNum: [
          { required: true, message: "消耗值不能为空", trigger: "blur" },
          { validator : checkConsumerNum ,trigger: "blur"}
        ],
      },
      roomTreeData: [],
      roomCasProp: {
        value: "id",
        label: "label",
        children: "children",
        expandTrigger: "hover",
      },
      treeFilterRoomName: null,
      selectData: {
        equipTypes: [],
        types: [],
      },
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
        url: process.env.VUE_APP_BASE_API + "system/nh/measure/importData"
      }
    };
  },
  created() {
    this.getList();
  }, // 页面渲染完毕,触发初始化
  mounted() {
    this.initSelector();
  },
  methods: {
    /** 查询能耗统计列表 */
    getList() {
      this.loading = true;
      listMeasure(this.queryParams).then((response) => {
        let { rows } = response;
        this.statisticsList = response.rows;
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
        // equipId: null,
        currentDate: null,
        nhTypeId: null,
        totalNum: null,
        consumeNum: null,
        remark: null,
        delFlag: null,
        createUser: null,
        createTime: null,
        updateUser: null,
        updateTime: null,
        likeCurrentDate: [],
      };
      this.roomTreeData = []
      this.queryParams.currentDates = [];
      this.equipmentData = [];
      //  this.queryParams.buildId = null;
      //   this.queryParams.floorId = null;
      //   this.queryParams.spaceId = null;
      this.initSelector();
      this.resetForm("form");
      this.queryParams.spaceId = null;
      this.queryParams.equipId = null;
      this.queryParams.floorId = null;
      this.queryParams.buildId = null;
      this.queryParams.typeModel = null;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      let timeStamp = this.queryParams.currentDates;
      if (timeStamp != null && timeStamp[0] && timeStamp[1]) {
        let startMonnt = this.getNowFormatDate(new Date(timeStamp[0]));
        let endMonnt = this.getNowFormatDate(new Date(timeStamp[1]));
        this.queryParams.likeCurrentDate.push(startMonnt);
        this.queryParams.likeCurrentDate.push(endMonnt);
      }

      this.queryParams.pageNum = 1;
      this.getList();
      this.queryParams.likeCurrentDate = [];
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
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加能耗统计";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getMeasure(id).then((response) => {
        let {data} = response;
        this.form = data;
        let arr = []
        arr.push(data.buildId)
        arr.push(data.floorId)
        arr.push(data.spaceId)
        arr.push(data.equipId)
        this.form.cascaderEquipmentModel = arr
        this.$forceUpdate();
        console.log(arr)
        this.open = true;
        this.title = "修改能耗统计";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          
          if (this.form.id != null) {
            updateMeasure(this.form).then((response) => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMeasure(this.form).then((response) => {
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
      this.$confirm(
        '是否确认删除能耗统计编号为"' + ids + '"的数据项?',
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(function () {
          return delMeasure(ids);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "nh/statistics/export",
        {
          ...this.queryParams,
        },
        `nh_statistics.xlsx`
      );
    },
    initSelector() {
      this.initRoomSelector();
      this.initEquuipTypesSelector();
      this.initTypesSelector();
      this.initEquipmentSelector();
    },
    initRoomSelector() {
      roomTreeList().then((response) => {
        let { rows } = response;
        this.roomCasData = rows;
        let allQueryObj = {
          id: -1,
          label: "全部区域",
          children: rows,
        };
        this.roomTreeData.push(allQueryObj);
      });
    },
    initEquuipTypesSelector() {
      getEquipTypeTreeList().then((response) => {
        let { rows } = response;
        this.selectData.equipTypes = rows;
      });
    },
    initTypesSelector() {
      getTypeTreeList().then((response) => {
        let { rows } = response;
        this.selectData.types = rows;
        console.log(this.selectData.types);
      });
    },
    initEquipmentSelector() {
      getEquipmentTreeList().then((response) => {
        let { rows } = response;
        this.equipmentData = rows;
        this.$forceUpdate();
      });
    },

    // 节点单击事件
    handleNodeClick(data) {
      // 有子节点,说明选择的是楼宇
      if (data.id == -1) {
        this.queryParams.buildId = null;
        this.queryParams.floorId = null;
        this.queryParams.spaceId = null;
      } else if (data["level"] == 1) {
        // 根据楼层id查询
        this.queryParams.buildId = data.id;
        this.queryParams.floorId = null;
        this.queryParams.spaceId = null;
      } else if (data["level"] == 2) {
        this.queryParams.floorId = data.id;
        this.queryParams.buildId = null;
        this.queryParams.spaceId = null;
      } else if (data["level"] == 3) {
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
    changeQueryParamsHandlerQuery() {
      this.handleQuery();
    },
    parseSpaceName(row) {
      let buildName = row.buildName;
      let floorName = row.floorName;
      let roomName = row.roomName;
      return buildName + "-" + floorName + "-" + roomName;
    },
    getNowFormatDate(date) {
      var seperator1 = "-";
      var year = date.getFullYear();
      var month = date.getMonth() + 1;

      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }

      var currentdate = year + seperator1 + month;
      return currentdate;
    },
    changeEquipmentCasHandler(equipmentArr) {
      if (equipmentArr["length"]) {
        this.form.equipId = equipmentArr[equipmentArr.length - 1];
      }
      console.log(this.form.equipId);
      this.$forceUpdate();
    },
    changeTypesHandlerUpdate(value){
      this.form.nhTypeId = value;
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "班级导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/nh/measure/importTemplate', {
        ...this.queryParams
      }, `能耗计量_${new Date().getTime()}.xlsx`)
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
  watch: {
    treeFilterRoomName(val) {
      this.$refs.tree.filter(val);
    },
    equipmentData(value) {
      this.equipmentKeyValue++; //只要监听到数据源发生变化 ，改变keyValue的值，达到重新渲染的效果
    },
  
  },
};
</script>