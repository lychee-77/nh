<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>智慧园区</h2>
        <p>
          智慧园区管理平台面向产业园区、企业园区等场景，聚焦园区管理、运营和办公，集成视频、AI、物联等能力，为物业、人事等部门提供场景。
          物联的智能应用，包括用户管理、角色管理、部门管理、园区管理、设备管理、能耗管理、系统设置等，实现园区整体智慧化和数字化。
          <br><br>智慧园区管理系统可以用于所有的Web应用程序，如网站管理后台、网站会员中心、CMS、CRM、OA等等，当然，您也可以对它进行深度定制，
          以做出更强系统。所有前端后台代码封装过后十分精简易上手，出错概率低,同时支持移动客户端访问,系统会陆续更新一些实用功能......
        </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>
        <p>
          <el-button
            size="mini"
            icon="el-icon-s-home"
            plain
            @click="goTarget('http://www.zhyq.com/')"
            >访问主页</el-button
          >
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>SpringCloud</li>
              <li>Nacos</li>
              <li>Sentinel</li>
              <li>Seata</li>
              <li>Minio</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue</li>
              <li>Vuex</li>
              <li>Element-ui</li>
              <li>Axios</li>
              <li>Echarts</li>
              <li>Quill</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> 官网：<el-link
                href="http://www.zhyq.com/"
                target="_blank"
                >http://www.zhyq.com/</el-link
              >
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>更新日志</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item title="v2.5.0 - 2024-02-02">
              <ol>
                <li>增加分布式事务seata支持</li>
                <li>代码生成模板支持主子表</li>
                <li>表格右侧工具栏组件支持显隐列</li>
                <li>图片组件添加预览&移除功能</li>
                <li>Excel注解支持Image图片导出</li>
                <li>操作按钮组调整为朴素按钮样式</li>
                <li>代码生成支持文件上传组件</li>
                <li>代码生成日期控件区分范围</li>
                <li>代码生成数据库文本类型生成表单文本域</li>
                <li>用户手机邮箱&菜单组件修改允许空字符串</li>
                <li>修复header获取username中文情况下乱码</li>
                <li>修复角色管理-编辑角色-功能权限显示异常</li>
                <li>修正操作日志删除接口路径</li>
                <li>修复IE11浏览器报错问题</li>
                <li>修复sentinel流量告警前端不响应</li>
                <li>修正侧边栏静态路由丢失问题</li>
                <li>修复导入数据为负浮点数时丢失精度问题</li>
                <li>修复Get请求参数特殊值无法正确的传参</li>
                <li>更换过期的共享配置属性</li>
                <li>添加启动执行脚本</li>
                <li>升级element-ui到最新版本2.15.0</li>
                <li>升级spring-boot到最新版本2.3.7</li>
                <li>升级spring-cloud到Hoxton.SR9</li>
                <li>升级spring-boot-alibaba到最新版2.2.5</li>
                <li>升级spring-boot-admin到最新版2.3.1</li>
                <li>升级druid到最新版本v1.2.4</li>
                <li>升级fastjson到最新版1.2.75</li>
                <li>其他细节优化</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v2.4.0 - 2023-12-22">
              <ol>
                <li>增加分布式文件Minio支持</li>
                <li>支持多数据源切换</li>
                <li>修复多级菜单之间切换无法缓存的问题</li>
                <li>三级菜单自动配置组件</li>
                <li>支持主题风格配置</li>
                <li>服务之间feign调用传递用户信息</li>
                <li>删除用户和角色解绑关联</li>
                <li>去除用户手机邮箱部门必填验证</li>
                <li>代码生成预览支持高亮显示</li>
                <li>获取请求token方法移至权限工具类</li>
                <li>代码生成预览提供滚动机制</li>
                <li>权限工具类增加管理员判断</li>
                <li>日志记录增加过滤多文件场景</li>
                <li>修改用户头像预览宽高</li>
                <li>Excel支持注解align对齐方式</li>
                <li>项目添加robots.txt 防止系统被搜索引擎收录</li>
                <li>移除path-to-regexp正则匹配插件</li>
                <li>修改Set可能导致嵌套的问题</li>
                <li>调整代码生成页列宽</li>
                <li>回显数据字典防止空值报错</li>
                <li>支持get请求映射params参数</li>
                <li>登录后push添加catch防止出现检查错误</li>
                <li>防止安全扫描YUI出现的风险提示</li>
                <li>代码生成删除多余的数字float类型</li>
                <li>Excel支持导入Boolean型数据</li>
                <li>修正转换字符串的目标字符集属性</li>
                <li>删除多余的依赖</li>
                <li>修改node-sass为dart-sass</li>
                <li>升级poi到最新版本4.1.2</li>
                <li>升级axios到最新版本0.21.0</li>
                <li>升级element-ui到最新版本2.14.1</li>
                <li>升级vue到最新版本2.6.12</li>
                <li>升级vuex到最新版本3.6.0</li>
                <li>升级vue-cli到版本4.5.9</li>
                <li>升级vue-router到最新版本3.4.9</li>
                <li>升级vue-cli到最新版本4.4.6</li>
                <li>升级vue-cropper到最新版本0.5.5</li>
                <li>升级clipboard到最新版本2.0.6</li>
                <li>升级core-js到最新版本3.8.1</li>
                <li>升级echarts到最新版本4.9.0</li>
                <li>升级file-saver到最新版本2.0.4</li>
                <li>升级fuse.js到最新版本6.4.3</li>
                <li>升级js-beautify到最新版本1.13.0</li>
                <li>升级js-cookie到最新版本2.2.1</li>
                <li>升级path-to-regexp到最新版本6.2.0</li>
                <li>升级quill到最新版本1.3.7</li>
                <li>升级screenfull到最新版本5.0.2</li>
                <li>升级sortablejs到最新版本1.10.2</li>
                <li>升级vuedraggable到最新版本2.24.3</li>
                <li>升级chalk到最新版本4.1.0</li>
                <li>升级eslint到最新版本7.15.0</li>
                <li>升级eslint-plugin-vue到最新版本7.2.0</li>
                <li>升级lint-staged到最新版本10.5.3</li>
                <li>升级runjs到最新版本4.4.2</li>
                <li>升级sass-loader到最新版本10.1.0</li>
                <li>升级script-ext-html-webpack-plugin到最新版本2.1.5</li>
                <li>升级svg-sprite-loader到最新版本5.1.1</li>
                <li>升级vue-template-compiler到最新版本2.6.12</li>
                <li>其他细节优化</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v2.3.0 - 2023-11-20">
              <ol>
                <li>新增文件服务应用（支持本地、FastDFS）</li>
                <li>优化头像样式，鼠标移入悬停遮罩</li>
                <li>AjaxResult重写put方法，以方便链式调用</li>
                <li>代码生成支持上传控件</li>
                <li>新增图片上传组件</li>
                <li>支持用户头像更新</li>
                <li>调整默认首页</li>
                <li>角色权限验证hasRole匹配改为equals</li>
                <li>修正数组权限为空判断</li>
                <li>修正注释选中节点和半选节点获取</li>
                <li>升级pagehelper到最新版1.3.0</li>
                <li>升级fastjson到最新版1.2.74</li>
                <li>修正定时任务执行一次权限标识</li>
                <li>修复页签关闭所有固定标签路由不刷新问题</li>
                <li>表单构建布局型组件新增按钮</li>
                <li>调整日志路径到模块目录</li>
                <li>修正菜单提示信息错误</li>
                <li>其他细节优化</li>
              </ol>
            </el-collapse-item>
            <el-collapse-item title="v1.0.0 - 2023-10-20">
              <ol>
                <li>智慧园区管理系统正式发布</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      // 版本号
      version: "2.5.0",
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

