<template>
  <v-container fluid style="height: 100%;">
    <v-container fluid>
      <v-row>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>服务状态</v-alert>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>服务运行状态</v-alert>
        </v-col>
        <v-col>
          <v-alert
            border="right"
            color="red"
            dark
          >{{serviceStatus == null ? '未获取' : serviceStatus.data}}</v-alert>
        </v-col>
        <v-col>
          <v-btn large color="error" class="text_margin_top" @click="startTask">启动</v-btn>
          <v-btn
            style="margin-left:25px;"
            large
            color="error"
            class="text_margin_top"
            @click="cancelTask"
          >取消</v-btn>
        </v-col>
      </v-row>
    </v-container>
    <!-- 打包配置展示修改区域 -->
    <v-container fluid style="height: 100%;">
      <v-row>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>配置信息</v-alert>
        </v-col>
      </v-row>
      <v-row style="width: 100%;">
        <v-col class="config_hint">
          <v-alert border="right" color="blue-grey" dark>项目目录</v-alert>
        </v-col>
        <v-col class="config_input">
          <v-text-field :rules="rules" v-model="projectDir" dark></v-text-field>
        </v-col>
        <div class="config_btn">
          <v-btn large color="primary">修改</v-btn>
        </div>
      </v-row>
      <v-row style="width: 100%;">
        <v-col class="config_hint">
          <v-alert border="right" color="blue-grey" dark>项目分支</v-alert>
        </v-col>
        <v-col class="config_input">
          <v-text-field :rules="rules" v-model="buildBranch" dark></v-text-field>
        </v-col>
        <div class="config_btn">
          <v-btn large color="primary">修改</v-btn>
        </div>
      </v-row>
      <v-row style="width: 100%;">
        <v-col class="config_hint">
          <v-alert border="right" color="blue-grey" dark>输出文件保存目录</v-alert>
        </v-col>
        <v-col class="config_input">
          <v-text-field :rules="rules" dark v-model="outputDir"></v-text-field>
        </v-col>
        <div class="config_btn">
          <v-btn large color="primary">修改</v-btn>
        </div>
      </v-row>
      <v-row style="width: 100%;">
        <v-col>
          <v-alert border="right" color="blue-grey" dark>上传方式</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>AccessKey</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>SecretKey</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
      </v-row>
      <v-row style="width: 100%;">
        <v-col>
          <v-alert border="right" color="blue-grey" dark>BucketName</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>FileUrlPrfix</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <div class="btn_margin_top">
          <v-btn large color="primary">修改</v-btn>
        </div>
      </v-row>
      <v-row style="width: 100%;">
        <v-col>
          <v-alert border="right" color="blue-grey" dark>乐固SecretId</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>乐固SecretKey</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <div class="btn_margin_top">
          <v-btn large color="primary">修改</v-btn>
        </div>
      </v-row>
      <v-row style="width: 100%;">
        <v-col class="config_hint">
          <v-alert border="right" color="blue-grey" dark>签名工具路径</v-alert>
        </v-col>
        <v-col class="config_input">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <v-col class="config_btn">
          <v-btn large color="primary">修改</v-btn>
        </v-col>
      </v-row>
      <v-row style="width: 100%;">
        <v-col class="config_hint">
          <v-alert border="right" color="blue-grey" dark>签名文件路径</v-alert>
        </v-col>
        <v-col class="config_input">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <div class="config_btn">
          <v-btn large color="primary">修改</v-btn>
        </div>
      </v-row>
      <v-row style="width: 100%;">
        <v-col>
          <v-alert border="right" color="blue-grey" dark>KeyAlias</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>KeyPassword</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <v-col>
          <v-alert border="right" color="blue-grey" dark>StorePassword</v-alert>
        </v-col>
        <v-col class="text_margin_top">
          <v-text-field :rules="rules" dark></v-text-field>
        </v-col>
        <v-col class="config_btn">
          <v-btn large color="primary">修改</v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    serviceStatus: null,
    config: null,
    projectDir: "",
    buildBranch: "",
    outputDir: ""
  }),
  methods: {
    request() {
      this.getStatus();
    },
    getStatus() {
      this.$http.get("http://192.168.10.185:8099/task").then(
        response => {
          console.log("请求到的数据：" + response);
          this.serviceStatus = response.body;
        },
        error => {
          console.log("请求错误：" + error);
        }
      );
    },
    getConfig() {
      this.$http.get("http://192.168.10.185:8099/config").then(
        response => {
          console.log("请求到的数据：" + response);
          this.config = response.body;
          this.projectDir = this.config.data.dir.projectSavePath;
          this.buildBranch = this.config.data.dir.buildBranch;
          this.outputDir = this.config.data.dir.outputDir;
        },
        error => {
          console.log("请求错误：" + error);
        }
      );
    },
    startTask() {
      this.$http
        .post("http://192.168.10.185:8099/task", {
          action: 1
        })
        .then(
          response => {
            alert(response.body.msg);
          },
          error => {
            console.log("请求错误：" + error);
          }
        );
    },
    cancelTask() {
      this.$http
        .post("http://192.168.10.185:8099/task", {
          action: 2
        })
        .then(
          response => {
            alert(response.body.msg);
          },
          error => {
            console.log("请求错误：" + error);
          }
        );
    }
  },
  created() {
    this.getConfig();
    setInterval(this.request, 1000);
  }
};
</script>


<style scoped>
.config_hint {
  width: 10%;
}

.config_input {
  width: 80%;
  margin-top: 10px;
}

.config_btn {
  width: 10;
  margin-top: 15px;
}

.text_margin_top {
  margin-top: 10px;
}

.btn_margin_top {
  margin-top: 15px;
}
</style>