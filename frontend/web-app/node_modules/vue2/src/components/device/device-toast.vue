<template>
  <div class="device-toast" v-if="showToast" @click.prevent="hideToastFun">
  <!-- 未绑定 -->
  <div class="toastBac" v-if="status === 'confirm'">
    <div class="bac"></div>
    <div class="toast">
      <div class="title">提示</div>
      <div class="notBind">{{confirmText}}</div>
      <div class="btn borderRight" @click="cancle">取消</div>
      <div class="btn" @click="submit">{{submitBtnText}}</div>
    </div>
  </div>
    <!-- 未绑定 -->
    <div class="toastBac" v-if="status === 'NOT_BOUND'">
      <div class="bac"></div>
      <div class="toast">
        <div class="notBind">该设备未与任何账号绑定，请绑定。</div>
        <div class="btn borderRight" @click="cancle">取消</div>
        <div class="btn" @click="submit">{{submitBtnText}}</div>
      </div>
    </div>
    <!-- success -->
    <div class="toastBac" v-if="status === 'success'">
      <div class="bac"></div>
      <div class="toast">
        <div class="toastImg"><img src="../../images/ic_finish.png"></div>
        <div class="success">{{text}}</div>
      </div>
    </div>
    <!-- warning -->
    <div class="toastBac" v-if="status === 'warning'">
      <div class="bac"></div>
      <div class="toast">
        <div class="toastImg"><img src="../../images/waring.png"></div>
        <div class="fail">{{text}}</div>
      </div>
    </div>
    <!-- fail -->
    <div class="toastBac" v-if="status === 'fail'">
      <div class="bac"></div>
      <div class="toast">
        <div class="toastImg"><img src="../../images/waring.png"></div>
        <div class="fail">绑定失败，请重新绑定
          <br/>如有问题请拨打：400 6190069</div>
        <div class="btn borderRight" @click="cancle">取消</div>
        <div class="btn" @click="submit">立即拨打</div>
      </div>
    </div>
    <!-- WRONG_DEVICE -->
    <div class="toastBac" v-if="status === 'WRONG_DEVICE'">
      <div class="bac"></div>
      <div class="toast">
        <div class="toastImg"><img src="../../images/waring.png"></div>
        <div class="fail">设备号错误</div>
        <div class="btn borderRight" @click="cancle">取消</div>
        <div class="btn" @click="submit">确认</div>
      </div>
    </div>
    <!--  -->
    <div class="toastBac" v-if="status === 'BOUND'">
      <div class="bac"></div>
      <div class="toast">
        <div class="toastImg1"><img src="../../images/pic_sb.png"></div>
        <!-- <div class="bindTime">绑定时间：{{boundDateTime}}</div> -->
        <div class="bind" v-if="!isSelf">该设备已经与 {{userName}} 完成绑定，想要关注他的健康数据？</div>
        <div class="bind" v-else>您已与该设备完成绑定</div>
        <!-- <div class="bind" v-else>该设备已经与 {{userName}} 绑定，请确认并登录</div> -->
        <div class="btn borderRight" @click="cancle" v-if="!isSelf">取消</div>
        <div class="btn" @click="submit" v-if="!isSelf">{{submitBtnText}}</div>
        <div class="btn1" @click="submit" v-if="isSelf">我知道了</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'device-toast',
  data() {
    return {
      status: '',
      boundDateTime: '',
      userName: '',
      submitBtnText: '',
      isSelf: false,
      options: {},
      text: '',
      showToast: false,
      confirmText: ''
    }
  },
  mounted: function() {
  },
  methods: {
    show: function(options) {
      this.options = options
      this.status = options.status
      this.text = options.text
      this.submitBtnText = options.submitBtnText || '去绑定'
      this.confirmText = options.confirmText || ''
      this.userName = options.userName || ''
      this.boundDateTime = options.boundDateTime || ''
      this.showToast = true
      this.isSelf = options.isSelf
    },
    submit: function() {
      this.showToast = false
      if (this.options.submit) {
        this.options.submit()
      }
    },
    cancle: function() {
      this.showToast = false
      if (this.options.showCancle) {
        this.options.cancle()
      }
    },
    hideToastFun: function() {
      this.showToast = false
      if (this.options.hide) {
        this.options.hide()
      }
    }
  }
}
</script>
<style scoped lang="scss">
.device-toast {
  width: 100%;
  height: 100%;
  position: absolute;
  /* background-color: #212852; */
  top: 0;
  left: 0;
  .toastBac {
    width: 100%;
    .bac {
      width: 100%;
      height: 100%;
      background-color: black;
      opacity: 0.3;
      position: fixed;
      z-index: 99;
    }
    .toast {
      width: 8.125rem;
      margin-left: 0.96875rem;
      margin-top: 47%;
      border-radius: 0.125rem;
      position: absolute;
      background-color: white;
      text-align: center;
      z-index: 100;
      font-size: 0.46875rem;
      .title{
        margin-top: 0.3125rem;
      }
      .title +{
        padding-top: 0.5rem!important;
      }
      .toastImg img {
        width: 15%;
        height: 15%;
        margin-top: 0.625rem;
        margin-bottom: 0.3125rem;
      }
      .toastImg1 {
        width: 2.5rem;
        height: 2.5rem;
        border-radius: 1.015625rem;
        margin: 0 auto;
        margin-top: 0.625rem;
        margin-bottom: 0.3125rem;
        img {
          width: 100%;
          height: 100%;
          border-radius: 1.25rem;
        }
      }
      .success {
        color: green;
        margin-bottom: 0.625rem;
      }
      .fail {
        color: #ed5564;
        padding-bottom: 0.3125rem;
        border-bottom: 0.015625rem solid #d2d3d5;
      }
      .bindTime {
        font-size: 0.375rem;
        color: grey;
        margin-bottom: 0.3125rem;
      }
      .bind {
        padding-left: 0.46875rem;
        text-align: left;
        padding-bottom: 0.3125rem;
        border-bottom: 0.015625rem solid #d2d3d5;
      }
      .notBind {
        padding-left: 0.46875rem;
        text-align: left;
        padding-top: 1rem;
        padding-bottom: 1rem;
        border-bottom: 0.015625rem solid #d2d3d5;
      }
      .btn {
        width: -webkit-calc(50% - 0.015625rem);
        font-size: 0.5rem;
        float: left;
        padding: 0.35rem 0;
      }
      .btn1 {
        width: 100%;
        font-size: 0.5rem;
        float: left;
        padding: 0.35rem 0;
        text-align: center;
      }
      .borderRight {
        border-right: 0.015625rem solid #d2d3d5;
      }
    }
  }
  .pic {
    width: 7.8125rem;
    height: 7.8125rem;
    padding-top: 1.953125rem;
    margin: 0 auto;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .tips {
    width: 100%;
    text-align: center;
    margin-top: 0.78125rem;
    font-size: 0.46875rem;
    color: #2ac4fb;
    margin-bottom: 0.59375rem;
  }
  .scanBtn {
    width: 9.0625rem;
    height: 1.25rem;
    line-height: 1.25rem;
    text-align: center;
    font-size: 0.5rem;
    margin: 0 auto;
    background-color: #2ac4fb;
    color: white;
    border-radius: 0.03125rem;
  }
  .hand {
    width: 100%;
    text-align: center;
    .handText {
      margin-top: 1.171875rem;
      color: white;
      opacity: 0.3;
      font-size: 0.40625rem;
      text-decoration: underline;
    }
  }
}
</style>
