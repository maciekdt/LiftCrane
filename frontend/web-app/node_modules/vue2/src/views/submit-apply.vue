<template>
  <div class="submit">
    <div v-if="ifShow" @touchstart="preventMouse($event)">
      <div class="bac"></div>
      <div class="toast" ref="testToast">
        <div><img src="../images/ic_finish.png"></div>
        <div>信息已提交</div>
        <div>3个工作日内完成审核</div>
      </div>
    </div>
    <div class="banner">
      <img :src="bgUrl">
    </div>
    <div class="item">
      <div class="tel">姓名</div>
      <div class="insertTel">
        <input placeholder="请输入您姓名" v-model="applyName">
      </div>
    </div>
    <div class="item1" v-bind:class="{'colorStyle': selecteSex !== '请选择您的性别'}">
      <div class="tel"></div>
      <div class="insertTel">
        <select v-model="selecteSex">
          <option>男</option>
          <option>女</option>
        </select>
      </div>
    </div>
    <div class="item">
      <div class="tel">性别</div>
      <div class="insertTel1" v-bind:class="{'opacityStyle': selecteSex !== '请选择您的性别'}">请选择您的性别
      </div>
    </div>
    <div class="item">
      <div class="tel">出生日期</div>
      <div class="insertTel" style="position: relative;">
        <div class="born" v-bind:class="{'colorStyle': selectValue !== '输入您的出生日期'}">{{selectValue}}</div>
        <input type="date" v-model="pickerValue" style="opacity: 0;position: absolute;z-index: 11;top:0;left: 0;">
      </div>
    </div>
    <div class="item">
      <div class="tel">开卡门店</div>
      <div class="insertTel">
        <input placeholder="选填" v-model="storeName">
      </div>
    </div>
    <div class="item">
      <div class="tel">开卡店员</div>
      <div class="insertTel">
        <input placeholder="选填" v-model="employeeName">
      </div>
    </div>
    <div class="tip" :style="{ 'color': newColor}">提交即可享受多重<a href="#/memberBenefits">《会员权益》</a></div>
    <div class="logBtn" @click="clickSubmit()" v-bind:class="{'toLogin': applyName && selecteSex !== '请选择您的性别' && selectValue !== '输入您的出生日期'}" :style="{ 'background-color': newColor}">提交申请</div>
    <!-- <mt-datetime-picker ref="picker" v-model="pickerValue" :startDate="setStart" type="date" year-format="{value} 年" month-format="{value} 月" date-format="{value} 日" @confirm="handleConfirm()"> -->
    <!-- </mt-datetime-picker> -->
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
import Vue from 'vue'
Vue.component(DatetimePicker.name, DatetimePicker)
import {
  DatetimePicker
} from 'mint-ui'
// 实例化vue
export default {
  components: {},

  data() {
    return {
      employeeName: '',
      storeName: '',
      selectValue: '输入您的出生日期',
      pickerValue: new Date(),
      setStart: new Date('1900-01-01'),
      selecteSex: '男',
      applyName: '',
      newColor: '',
      bgUrl: '',
      ifShow: false
    }
  },
  watch: {
    pickerValue: function() {
      this.handleConfirm()
    }
  },
  methods: {
    handleConfirm: function() {
      this.selectValue = this.pickerValue
    },
    // getNeedDate: function(date) {
    //   var y = date.getFullYear()
    //   var m = date.getMonth() + 1
    //   m = m < 10 ? '0' + m : m
    //   var d = date.getDate()
    //   d = d < 10 ? ('0' + d) : d
    //   this.paramDate = y.toString() + m.toString() + d.toString()
    //   return y + '-' + m + '-' + d
    // },
    openPicker: function() {
      this.$refs.picker.open()
    },
    preventMouse: function(event) {
      event.preventDefault()
    },
    clickSubmit: function() {
      var sex = ''
      if (this.selecteSex === '男') {
        sex = '1'
      } else if (this.selecteSex === '女') {
        sex = '2'
      }
      if (this.applyName && this.selecteSex !== '请选择您的性别' && this.selectValue !== '您输入您的出生日期') {
        Utils.post(Api.MEMBER_POST_APPLY, {
          'mainStoreId': this.Storage.get('mainStoreId'),
          'userName': this.$route.query.tel,
          'name': this.applyName,
          'gender': sex,
          'birthday': this.pickerValue.replace('-', '').replace('-', ''),
          'storeName': this.storeName,
          'employeeName': this.employeeName
        }, result => {
          if (result.success) {
            this.ifShow = true
            var self = this
            setTimeout(function() {
              var a = (document.body.scrollHeight - document.body.clientHeight) + (document.body.clientHeight / 3)
              self.$refs.testToast.style.marginTop = a + 'px'
            })
            setTimeout(function() {
              self.$router.push({
                name: 'information',
                params: {
                  checkOrMember: 'check'
                }
              })
            }, 2000)
          }
        })
      }
    }
  },

  created: function() {},

  mounted: function() {
    this.newColor = this.Storage.get('useInfo').color
    this.bgUrl = this.Storage.get('useInfo').imgUrlMap.logoBg
    // let rootHtml = document.getElementsByTagName('html')[0]
    // setTimeout(() => {
    //   let dataDpr = rootHtml.getAttribute('data-dpr')
    //   document.getElementsByClassName('mint-datetime')[0].style.zoom = dataDpr
    //   window.alert(dataDpr)
    // }, 500)
  }
}
</script>
<style>
.mint-datetime .picker-item, .mint-datetime-action{
  font-size: 0.5rem!important;
}
</style>
<style scoped lang="scss">
@import '../scss/common';
@import '../scss/vue-swipe.css';
.bac {
  width: 100%;
  height: 100%;
  background-color: black;
  opacity: 0.3;
  position: fixed;
  z-index: 99;
}

.submit {
  width: 100%;
  height: 100%;
  background-color: white;
}

a {
  color: #31c27c;
  text-decoration: underline;
}

.banner {
  width: 100%;
  height: 6.40625rem;
  margin-bottom: 0.15625rem;
}

.banner img {
  width: 100%;
  height: 100%;
}

.item {
  width: 100%;
  height: 1.875rem;
  line-height: 1.875rem;
  font-size: 0.5rem;
  border-bottom: 1px solid #E9E6E6;
}

.item1 {
  width: 100%;
  height: 1.875rem;
  line-height: 1.875rem;
  font-size: 0.5rem;
  z-index: 3;
  opacity: 0;
  position: absolute;
  border-bottom: 1px solid #E9E6E6;
}

.name {
  height: 100%;
  float: left;
  margin-left: 5%;
  margin-right: 11%;
}

.tel {
  width: 21%;
  height: 100%;
  float: left;
  margin-left: 5%;
  margin-right: 2%;
}

.insertTel {
  width: 70%;
  height: 50%;
  float: left;
}

.insertTel1 {
  width: 70%;
  height: 50%;
  float: left;
  color: #B3B3B3;
}

.insertTel input {
  width: 100%;
  height: 100%;
}

.born {
  color: #B3B3B3;
}

input::placeholder {
  color: #B3B3B3;
}

.insertTel select {
  width: 100%;
  appearance: none;
  padding-left: 0;
  border: none;
  color: #B3B3B3;
  font-size: 0.5rem;
  outline: none;
}

.sendCode {
  width: 20%;
  height: 40%;
  margin-top: 5%;
  text-align: center;
  float: left;
  font-size: 0.5rem;
  /* line-height: 1.7rem; */
  color: #31c27c;
  border: 0.015625rem solid #31c27c;
}

.tip {
  width: 100%;
  height: 1.5rem;
  line-height: 1.5rem;
  font-size: 0.5rem;
  text-align: center;
}

.logBtn {
  margin-top: 1.5rem;
  width: 90%;
  margin-left: 5%;
  opacity: 0.5;
  height: 1.25rem;
  line-height: 1.25rem;
  text-align: center;
  font-size: 0.5rem;
  color: white;
  margin-bottom: 3rem;
}

.toast {
  width: 70%;
  margin-left: 15%;
  margin-top: 70%;
  border-radius: 0.109375rem;
  height: 3.125rem;
  font-size: 0.4375rem;
  position: absolute;
  background-color: white;
  text-align: center;
  color: green;
  z-index: 100;
}

.toast img {
  width: 15%;
  height: 15%;
  margin: 0.15625rem 0;
}

.toLogin {
  opacity: 1;
}

.colorStyle select {
  color: black;
  opacity: 1 !important;
}

.colorStyle {
  color: black;
  opacity: 1 !important;
}

.opacityStyle {
  opacity: 0;
}
</style>
