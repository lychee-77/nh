<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-card class="box-card" shadow="hover">
        <div slot="header" class="clearfix">
          <el-form
            :model="totalNumStatistics"
            :inline="true"
            label-width="68px"
          >
            <el-form-item label="设备类型" prop="equipmentTypeModel">
              <el-select
                v-model="totalNumStatistics.chartstatisticsType"
                placeholder="请选择能耗统计的类型"
                filterable
                @change="changeStatisticsType"
              >
                <el-option
                  v-for="item in this.totalNumStatisticsType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="时间范围" prop="statisticsDate" v-show="!isTimeStatistic">
              <el-date-picker
                v-model="totalNumStatistics.statisticsDate"
                @change="changeStatisticsDate"
                :clearable="false"
                
                type="month"
                placeholder="请选择统计月份"
              >
              </el-date-picker>
            </el-form-item>
          </el-form>
        </div>
        <el-row :gutter="20">
          <el-col :span="4" :xs="24">
            <div
              id="totalNumStatisticsChart"
              :style="{ width: '750px', height: '500px' }"
            ></div>
          </el-col>
          <el-col :span="4" :xs="24" :offset="9">
            <div
            v-show="!this.isTimeStatistic"
              id="rateStatisticsChart"
              :style="{ width: '750px', height: '500px' }"
            ></div>
          </el-col>
        </el-row>
        <el-row :gutter="20" type="flex">
          <el-col :span="4" :xs="24">
            <div
              id="rateStatisticsChart1"
              v-show="!this.isTimeStatistic"
              :style="{ width: '750px', height: '500px' }"
            ></div>
          </el-col>
        </el-row>
      </el-card>
    </el-row>
  </div>
</template>

<script>
// Echarts5 导入方式
import * as echarts from "echarts";
import { listStatistics, listRingRatio,listYearRatio } from "@/api/nh/statistics";
export default {
  name: "hello",
  data() {
    return {
      msg: "Welcome to Your Vue.js App",
      totalNumStatisticsType: [
        {
          label: "区域",
          value: 1,
        },
        {
          label: "设备类型",
          value: 2,
        },
        {
          label: "能耗类型",
          value: 3,
        },
        {
          label: "时间",
          value: 4,
        },
      ],

      totalNumStatistics: {
        // 默认按照第一个选项进行分类查询
        chartstatisticsType: 1,
        statisticsType: "区域",
        // 最大显示的条数
        showMaxRow: 5,
        dialogTableVisible: false,
        statisticsDate: '2016-03',
      },
      // 供渲染的data
      renderDatas: [],
      colors: ["#5470C6", "#91CC75", "#EE6666"],
      isTimeStatistic: false,
      intervalId:null,
      // y坐标预留出来的空间,防止顶到y轴定点,多显示0.1
      axiosRangeTimes: 0.15
    };
  },
  mounted() {
    this.drawLine2();
    this.drawRateLine();
    this.drawRateLine1();
  },
  methods: {
    getChartById(id) {
      return echarts.init(document.getElementById(id));
    },
    async requestGetResultData() {
      let { data } = await listStatistics(
        this.totalNumStatistics.chartstatisticsType
      );
      // 按照值的高低进行降序排列
      let datas = data.datas.sort((a, b) => new Date(a.xData) - new Date(b.xData));
      // 待渲染的数据

      // 最小值默认后端为0
      let yMin = data.minTotalNum;
      // y最大值为数据 数据的最大值 + 0.1最大值
      let yMax = data.maxTotalNum + 0.1 * data.maxTotalNum;
      return {
        datas,
        yMin,
        yMax,
      };
    },
    async requestRingRatioGetResultData(requestDataFunction) {
      let { data } = await requestDataFunction(
        this.totalNumStatistics.chartstatisticsType,
        {
          statisticsDate: this.getNowFormatDate(
            this.totalNumStatistics.statisticsDate
          ),
        }
      );

      // 按照值的高低进行降序排列
      let currentMonthItem = data.currentMonthItem;
    
      let lastMonthItem = data.lastMonthItem;

      let reteItem = data.rete;
      // 待渲染的数据

      let typeInfo = data.currentMonthItem.typeInfo;
      return {
        currentMonthItem,
        lastMonthItem,
        reteItem,
        typeInfo,
      };
    },
    // 基础的配置项
    chartBaseOption() {
      return {
        title: {
          text: "能耗类型统计图",
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            label: {
              backgroundColor: "#283b56",
            },
          },
        },
        legend: {},
        toolbox: {
          show: true,
          feature: {
            dataView: { readOnly: false },
            restore: {},
            saveAsImage: {},
          },
        },
        dataZoom: {
          show: false,
          start: 0,
          end: 100,
        },
        xAxis: [
          {
            type: "category",
            boundaryGap: true,
            data: [],
          },
        ],
        yAxis: [
          {
            type: "value",
            scale: true,
            name: "总耗能值",
            max: 1000,
            min: 0,
            boundaryGap: [0.2, 0.2],
          },
        ],
        series: [
          {
            name: "",
            type: "line",
            xAxisIndex: 0,
            yAxisIndex: 0,
            data: [],
          },
        ],
      };
    },

    // 环比参数配置
    statisticsChartBaseOption() {
      let colors = this.colors
      return {
        title: {
          text: "能耗类型统计图",
        },
        color: colors,
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
          },
        },
        grid: {
          right: "20%",
        },
        toolbox: {
          feature: {
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        legend: {
          data: ["Evaporation", "Precipitation", "Temperature"],
        },
        xAxis: [
          {
            type: "category",
            axisTick: {
              alignWithLabel: true,
            },
            // prettier-ignore
            data: [],
          },
        ],
        yAxis: [
          {
            type: "value",
            name: "耗能",
            min: 0,
            max: 250,
            position: "right",
            axisLine: {
              show: true,
              lineStyle: {
                color: colors[1],
              },
            },
            axisLabel: {
             formatter:  function (value, index) {  
                let x = String(value).indexOf('.') + 1; //小数点的位置
                let y = String(value).length - x; //小数的位数
                let decimalsLength = y;
                if(parseInt(value) || decimalsLength < 3) {
                    return value;
                }
                 return value.toFixed(3);
                    },
            },
          },
          {
            type: "value",
            name: "",
            min: 0,
            max: 100,
            position: "left",
            axisLine: {
              show: true,
              lineStyle: {
                color: colors[0],
              },
            },
            axisLabel: {
              formatter:  function (value, index) {  
                let x = String(value).indexOf('.') + 1; //小数点的位置
                let y = String(value).length - x; //小数的位数
                let decimalsLength = y;
                 if(parseInt(value) || decimalsLength < 3) {
                    return value;
                }
                 return value.toFixed(3);
                    },
            },
          },
        ],
        series: [
          {
            name: "本月",
            type: "bar",
            color: colors[0],
            data: [],
          },
          {
            name: "上月",
            type: "bar",
            yAxisIndex: 0,
            xAxisIndex: 0,
            color: colors[1],
            data: [],
          },
          {
            name: "本月",
            type: "line",
            yAxisIndex: 1,
            xAxisIndex: 0,
            color: colors[2],
            data: [159, 100, 90, 26],
          },
        ],
      };
    },

    async drawLine2() {
      let totalNumStatisticsChart = this.getChartById(
        "totalNumStatisticsChart"
      );
      // 获取带渲染的数据

      let chartData = await this.requestGetResultData();
      // 获取基础配置项
      let baseOption = this.chartBaseOption();
      // 根据数据修改配置项
      baseOption.yAxis[0].max = chartData.yMax;
      baseOption.yAxis[0].min = chartData.yMin ? chartData.yMin : 0;
      baseOption.title.text = this.totalNumStatistics.statisticsType + "统计图";

      // 按量取出数据进行渲染
      baseOption.series[0].data = chartData.yDatas;
      let renderDatas = (this.renderDatas = chartData.datas);

      // x轴数据
      let xDatas = renderDatas.map((item) => item.xData);
      // y轴数据
      let yDatas = renderDatas.map((item) => item.yData);
      let maxRow = this.totalNumStatistics.showMaxRow;
      // 带渲染的数组长度不高于10直接渲染
      if (renderDatas.length < maxRow + 1) {
        baseOption.series[0].data = yDatas;
        baseOption.xAxis[0].data = xDatas;
        totalNumStatisticsChart.setOption(baseOption);
      } else {
        // 根据数据批量渲染,暂时只渲染前10条数据
        baseOption.series[0].data = yDatas.slice(0, maxRow);
        baseOption.xAxis[0].data = xDatas.slice(0, maxRow);
        totalNumStatisticsChart.setOption(baseOption);
        // 当前位于第最大显示次数
        let count = maxRow + 1;
        this.intervalId = setInterval(() => {
          // 去掉y轴最后一项
          if (count == renderDatas.length) {
            clearInterval(intervalId);
            return;
          }
          let yData = yDatas.slice(count, count + 1);
          baseOption.series[0].data.shift();
          // 必须是Number类型
          baseOption.series[0].data.push(Number(yData));
          // 删除x轴第一位数据
          baseOption.xAxis[0].data.shift();
          // push新的数据
          baseOption.xAxis[0].data.push(xDatas.slice(count, count + 1));
          totalNumStatisticsChart.setOption(baseOption);
          count++;
        }, 1500);
      }
    },
    async drawRateLine() {
      let rateStatisticsChart = this.getChartById("rateStatisticsChart");
      // 获取带渲染的数据
      let chartData = await this.requestRingRatioGetResultData(listRingRatio);
      let baseOption = this.statisticsChartBaseOption();
      // 根据数据修改配置项
      baseOption.title.text =
        this.totalNumStatistics.statisticsType + "环比统计图";
      let setDataBaseOption = this.renderChartData(baseOption,chartData);
      rateStatisticsChart.setOption(setDataBaseOption);
    },
    renderChartData(baseOption,chartData){
            
      // 按量取出数据进行渲染
      baseOption.series[0].data = chartData.yDatas;
      let currentMonthItem = chartData.currentMonthItem;
      let lastMonthItem = chartData.lastMonthItem
      let reteItem = chartData.reteItem
      // 当前x轴数据
      console.log(reteItem)
      let xDatas = currentMonthItem.datas.map((item) => item.xData);
      // 当前月y轴数据
      let currentMonthYData = currentMonthItem.datas.map((item) => item.yData);
      // 环比数据
      let lastMonthYData = lastMonthItem.datas.map((item) => item.yData);
      let rateYData =  reteItem.datas.map((item) => item.yData);
      // 带渲染的数组长度不高于10直接渲染
        baseOption.series[0].data = lastMonthYData;
        baseOption.series[0].name = lastMonthItem.typeInfo;
        baseOption.series[1].data = currentMonthYData;
        baseOption.series[1].name = currentMonthItem.typeInfo;
        baseOption.series[2].data = rateYData;
        baseOption.series[2].name = reteItem.typeInfo;
        baseOption.xAxis[0].data = xDatas;
        baseOption.yAxis[0].min = this.getAxiosMinRange(currentMonthItem.minTotalNum);
        baseOption.yAxis[0].max = this.getAxiosMaxRange(currentMonthItem.maxTotalNum);
        baseOption.yAxis[1].min = this.getAxiosMinRange(reteItem.minTotalNum);
        baseOption.yAxis[1].max = this.getAxiosMaxRange(reteItem.maxTotalNum);
        return baseOption;
    },
    // 计算坐标轴范围 最大值
    getAxiosMaxRange(num,min){
      // 扩大100倍
      let axiosRangeTimes = this.axiosRangeTimes
      let beforeReturnNum;
      // 如果是负数
          // 如果是小数+10
          if(Math.abs(num) < 3){
            num += 3;
          }
      beforeReturnNum = num + num * axiosRangeTimes;
      return beforeReturnNum.toFixed(2)
    },
    // 计算坐标轴范围 最小值
    getAxiosMinRange(num){
      // 扩大100倍
      let axiosRangeTimes = this.axiosRangeTimes
      let beforeReturnNum;
      // 如果是负数
          // 如果是小数+10
          if(Math.abs(num) < 3){
            num -= 3;
          }
      beforeReturnNum = num - Math.abs(num) * axiosRangeTimes;
      return beforeReturnNum.toFixed(2)
    },
    async drawRateLine1() {
       let rateStatisticsChart = this.getChartById("rateStatisticsChart1");
      // 获取带渲染的数据
      let chartData = await this.requestRingRatioGetResultData(listYearRatio);
      let baseOption = this.statisticsChartBaseOption();
      // 根据数据修改配置项
      baseOption.title.text =
        this.totalNumStatistics.statisticsType + "同比统计图";
      let setDataBaseOption = this.renderChartData(baseOption,chartData);
      rateStatisticsChart.setOption(setDataBaseOption);
    },
    changeStatisticsType(value) {
      let typeName = this.totalNumStatisticsType.filter(
        (item) => item.value == value
      )[0].label;
      this.totalNumStatistics.statisticsType = typeName;
      this.totalNumStatistics.chartstatisticsType = value;
      if(value == 4){
        this.isTimeStatistic = true;
      }else{
         this.isTimeStatistic = false;
      }
      this.$forceUpdate();
      
   this.drawLine2();
   // 清除之前的定时任务
      clearInterval(this.intervalId);
      this.drawRateLine();
      this.drawRateLine1();
    },
    changeStatisticsDate() {
      this.drawLine2();
      this.drawRateLine();
      this.drawRateLine1();
    },
    getNowFormatDate(date) {
      var seperator1 = "-";
      if(!date["getFullYear"]){
          return date;
      }
      var year = date.getFullYear();
      var month = date.getMonth() + 1;

      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }

      var currentdate = year + seperator1 + month;
      return currentdate;
    },
  },
};
</script>