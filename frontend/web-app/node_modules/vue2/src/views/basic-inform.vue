<template>
  <div class="basicInform">
    <div class="item1">
      <div class="tel"></div>
      <div class="insertTel">
        <select v-model="selecteSex">
          <option></option>
          <option>男</option>
          <option>女</option>
        </select>
      </div>
    </div>
    <div class="item">
      <div class="tel">性别</div>
      <div class="insertTel">{{userSex}}
      </div>
    </div>
    <div class="item">
      <div class="tel">生日</div>
      <div class="insertTel"style="position: relative;">
        <div class="born">{{selectValue}}</div>
        <input type="date" v-model="pickerValue" style="opacity: 0;position: absolute;z-index: 11;top:0;left: 0;">
      </div>
    </div>
    <div class="item">
      <div class="tel">体重（公斤）</div>
      <input class="insertTel" @click="changeInfo()" v-model="weight" type="tel">
    </div>
    <div class="item">
      <div class="tel">身高（厘米）</div>
      <input class="insertTel" @click="changeInfo()" v-model="height" type="tel">
    </div>
    <div class="logBtn" :style="{ 'background-color': newColor}" v-bind:class="{'toLogin': ifChanged}" @click="saveChange()">保存修改</div>
    <!-- <mt-datetime-picker ref="picker" v-model="pickerValue" :startDate="setStart" type="date" year-format="{value} 年" month-format="{value} 月" date-format="{value} 日" @confirm="handleConfirm">
    </mt-datetime-picker> -->
  </div>
</template>
<script>
// 引入组件以及公共文件
import Utils from '../utils'
import Api from '../api'
import {
  Toast
} from 'mint-ui'
import {
  Swipe,
  SwipeItem
} from 'vue-swipe'
// 实例化vue
export default {
  components: {
    Swipe,
    SwipeItem
  },

  data() {
    return {
      setStart: new Date('1900-01-01'),
      paramDate: '',
      pickerValue: new Date(),
      selectValue: '',
      selecteSex: '',
      userSex: '',
      initHeight: '',
      initWeight: '',
      ifChanged: false,
      weight: '',
      height: '',
      newColor: '',
      informList: [{
        name: '性别',
        detail: this.sex
      }, {
        name: '年龄',
        detail: '22岁/1992年1月1日'
      }]
    }
  },
  watch: {
    selecteSex: function(val, oldVal) {
      // console.log('new: %s, old: %s', val, oldVal)
      this.userSex = val
      if (this.height && this.weight && this.paramDate) {
        this.ifChanged = true
      }
    },
    weight: function(val, oldVal) {
      // console.log('new: %s, old: %s', val, oldVal)
      if ((val !== oldVal) && this.weight && this.height) {
        this.ifChanged = true
      }
      if (!val) {
        this.ifChanged = false
      }
      if (this.weight === this.initWeight && this.height === this.initHeight) {
        this.ifChanged = false
      }
    },
    height: function(val, oldVal) {
      console.log('new: %s, old: %s', val, oldVal)
      if ((val !== oldVal) && this.height && this.weight) {
        this.ifChanged = true
      }
      if (!val) {
        this.ifChanged = false
      }
      if (this.weight === this.initWeight && this.height === this.initHeight) {
        this.ifChanged = false
      }
    },
    pickerValue: function() {
      // this.selectValue = this.pickerValue
      this.handleConfirm()
    }
  },

  methods: {
    handleConfirm: function() {
      // this.selectValue = (2017 - this.getNeedDate(this.pickerValue).substring(0, 4)) + '岁/' + this.getNeedDate(this.pickerValue)
      this.selectValue = (2017 - parseInt(this.pickerValue.substring(0, 4))) + '岁/' + this.pickerValue
      if (this.height && this.weight && this.paramDate) {
        this.ifChanged = true
      }
    },
    getNeedDate: function(date) {
      var y = date.getFullYear()
      var m = date.getMonth() + 1
      m = m < 10 ? '0' + m : m
      var d = date.getDate()
      d = d < 10 ? ('0' + d) : d
      this.paramDate = y + m + d
      return y + '-' + m + '-' + d
    },
    getNeedDate1: function(date) {
      var y = date.getFullYear()
      var m = date.getMonth() + 1
      m = m < 10 ? '0' + m : m
      var d = date.getDate()
      d = d < 10 ? ('0' + d) : d
      return y + m + d
    },
    openPicker: function() {
      this.$refs.picker.open()
    },
    changeInfo: function() {},
    saveChange: function() {
      var sexId
      if (this.userSex === '男') {
        sexId = 1
      } else {
        sexId = 2
      }
      if (this.ifChanged === true) {
        Utils.post(Api.CHANGE_PROFILE, {
          'height': this.height,
          'weight': this.weight,
          'birthday': this.paramDate,
          'pId': this.Storage.get('patientId'),
          'sex': sexId
        }, result => {
          Toast({
            message: '修改成功！',
            position: 'top',
            duration: 2000
          })
        })
      }
      if (!this.selectValue) {
        Toast({
          message: '请输入您的出生日期！',
          position: 'top',
          duration: 2000
        })
      } else if (!this.weight) {
        Toast({
          message: '请输入体重！',
          position: 'top',
          duration: 2000
        })
      } else if (!this.height) {
        Toast({
          message: '请输入身高！',
          position: 'top',
          duration: 2000
        })
      }
    }
  },

  created: function() {},

  mounted: function() {
    this.newColor = this.Storage.get('useInfo').color
    Utils.post(Api.GET_PROFILE, {
      'pId': this.Storage.get('patientId')
    }, result => {
      if (result.healthProfile.sex === 1) {
        this.userSex = '男'
      } else {
        this.userSex = '女'
      }
      if (result.healthProfile.birthday) {
        this.informList[1].detail = result.healthProfile.birthday
        this.informList[1].detail = (2017 - this.informList[1].detail.substring(0, 4)) + '岁/' + this.informList[1].detail
        this.selectValue = this.informList[1].detail
        this.initHeight = result.healthProfile.height
        this.height = this.initHeight
        this.initWeight = result.healthProfile.weight
        this.weight = this.initWeight
        this.paramDate = result.healthProfile.birthday
        console.log(this.getNeedDate1(new Date(this.paramDate)))
      }
    })
    // .picker-datetime
    let rootHtml = document.getElementsByTagName('html')[0]
    let dataDpr = rootHtml.getAttribute('data-dpr')
    document.getElementsByClassName('mint-datetime')[0].style.zoom = dataDpr
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
.item {
  width: 100%;
  height: 1.75rem;
  line-height: 1.75rem;
  font-size: 0.5rem;
  border-bottom: 0.015625rem solid #E9E6E6;
}

.item1 {
  width: 100%;
  height: 1.75rem;
  line-height: 1.75rem;
  font-size: 0.5rem;
  z-index: 3;
  opacity: 0;
  position: absolute;
  border-bottom: 1px solid #E9E6E6;
}

.tel {
  height: 100%;
  float: left;
  margin-left: 5%;
  margin-right: 2%;
}

.insertTel {
  height: 100%;
  float: right;
  text-align: right;
  margin-right: 5%;
}

.insertTel input {
  width: 100%;
  height: 100%;
}

.insertTel select {
  width: 100%;
  appearance: none;
  padding-left: 0;
  border: none;
  color: #B3B3B3;
  font-size: 1rem;
  outline: none;
}

.insertTel1 {
  height: 50%;
  // margin-right: 5%;
  float: right;
  color: #B3B3B3;
}

::-webkit-input-placeholder {
  /* WebKit browsers */
  color: black;
}

:-moz-placeholder {
  /* Mozilla Firefox 4 to 18 */
  color: black;
}

::-moz-placeholder {
  /* Mozilla Firefox 19+ */
  color: black;
}

:-ms-input-placeholder {
  /* Internet Explorer 10+ */
  color: black;
}

.logBtn {
  margin-top: 3.5rem;
  width: 90%;
  margin-left: 5%;
  opacity: 0.5;
  padding: 0.46875rem 0;
  text-align: center;
  font-size: 0.5rem;
  color: white;
  /* margin-bottom: 3rem; */
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

.toLogin {
  opacity: 1;
}
</style>
