<template>
  <div class="healthDocument">
    <div class="header">
      <div class="logo">
        <img :src="logoUrl">
      </div>
      <div class="name">{{healthProfile.patientName}}</div>
      <div class="tips">为定制专属您的健康管理，请完善信息</div>
      <!--       <div class="progress">
        <div class="percentage">70%</div>
        <div class="percentage1"></div>
      </div> -->
      <div class="topImg">
        <img src="http://api.carelinker.com/web/wechat-personal/image/bg_jkda.png">
      </div>
    </div>
    <div class="list" v-for="(item,index) in healthList">
      <div class="first" @click="showDetail(index)">
        <div class="listName">{{item.name}}</div>
        <div class="listName1">
          <img :src="upUrl" class="listNameImg" v-if="!upOrDown || currentIndex != index">
          <img :src="downUrl" class="listNameImg" v-if="upOrDown && currentIndex == index">
        </div>
      </div>
      <div class="detail" v-for="(more,moreIndex) in item.detail" v-if="currentIndex == index" @click="goDetail(index,moreIndex)" @touchstart="preventMouse($event,index,moreIndex)">
        <div class="detailStyle">{{more.name}}</div>
        <input v-if="index===0&&moreIndex===2" type="number" v-model="watchWeight" />
        <input v-if="index===0&&moreIndex===3" type="number" v-model="watchHeight" />
        <select v-if="index===0&&moreIndex===0" v-model="selectSex">
          <option>男</option>
          <option>女</option>
        </select>
        <div class="detailStyle1" v-if="index===0&&moreIndex===1"style="position: relative;">
        <!-- {{more.result}} -->
          {{pickerValue1}}
         <input type="date" v-model="pickerValue" style="opacity: 0;position: absolute;z-index: 11;top:0;left: -47px;">
        </div>
        <div class="detailStyle1" v-if="more.result.length>6 && index!==0">{{more.result.substring(0,6)+'...'}}</div>
        <div class="detailStyle1" v-if="more.result.length<7 && index!==0">{{more.result}}</div>
      </div>
    </div>
    <div class="bottomBtn" v-if="isSelf === 'SELF'">
      <div class="save" v-bind:class="{'newSave':isModified}" @click="clickSave()">保存修改</div>
    </div>
  </div>
</template>
<script>
import Utils from '../utils'
import Api from '../api'
import {
  Toast,
  Indicator
} from 'mint-ui'
const SymptomList = {
  isHavingSymInsomnia: '失眠',
  isHavingSymConstipation: '便秘',
  isHavingSymW: '乏力',
  isHavingSymPda: '多饮',
  isHavingSymPha: '多食',
  isHavingSymPu: '多尿',
  isHavingSymLw: '体重下降',
  isHavingSymB: '视物模糊',
  isHavingSymHfc: '手足发凉',
  isHavingSymHfn: '手足麻木',
  isHavingSymP: '皮肤瘙痒',
  isHavingSymDia: '腹泻',
  isHavingSymDis: '头晕',
  isHavingSymHa: '头痛',
  isHavingSymNs: '颈项板紧',
  isHavingSymPa: '心悸'
}
const TakeDrugsList = {
  isTakingDds: '利尿剂',
  isTakingNsaids: '非甾体抗炎药',
  isTakingAhds: '抗高血压药',
  isTakingApds: '抗凝药',
  isTakingLlds: '降低脂质药物',
  isTakingAads: '抗心律失常药物',
  isTakingBabds: 'β肾上腺素受体阻断剂',
  isTakingAdds: '抗糖尿病药',
  isTakingPids: '正性肌力药物',
  isTakingAgds: '抗心绞痛药',
  isTakingAtds: '抗血栓药'
}
const HaveDmList = {
  isHavingDmFhod: '糖尿病家族史',
  isHavingDm1: '1型糖尿病',
  isHavingDm2: '2型糖尿病',
  isHavingDmG: '妊娠期糖尿病',
  isHavingDmS: '特殊类型糖尿病'
}
const HaveGdcList = {
  isHavingGdcDn: '糖尿病肾病',
  isHavingGdcDr: '糖尿病性视网膜病变/糖尿病性白内障',
  isHavingGdcDf: '糖尿病足',
  isHavingGdcDci: '糖尿病合并感染:呼吸道/泌尿道/皮肤/口腔',
  isHavingGdcPsd: '糖尿病并发神经末梢感觉障碍'
}
const HaveHtnList = {
  isHavingHtnFfoh: '高血压家族史',
  isHavingHtnP: '原发性高血压',
  isHavingHtnS: '继发性高血压'
}
const HaveHtncList = {
  isHavingHtncHe: '高血压脑病',
  isHavingHtncAaad: '高血压引起动脉硬化、主动脉夹层',
  isHavingHtncHrd: '高血压性肾损害',
  isHavingHtncRaafc: '高血压引起视网膜动脉硬化、眼底改变',
  isHavingHtncLvh: '高血压引起左心室肥厚'
}
const HaveCdList = {
  isHavingCdTia: '心绞痛',
  isHavingCdI: '短暂性缺血性发作',
  isHavingCdHf: '心力衰竭',
  isHavingCdMi: '心肌梗死',
  isHavingCdOhd: '其他心脏病',
  isHavingCdFhoa: '心血管疾病家族史',
  isHavingCdHs: '出血性脑卒中',
  isHavingCdCah: '缺血性脑卒中',
  isHavingCdFhos: '脑卒中家族史',
  isHavingCdChd: '冠心病',
  isHavingCdIm: '心肌缺血',
  isHavingCdAf: '心房颤动'
}
const HaveCtdList = {
  isHavingCtdAlf: '肝功能异常',
  isHavingCtdRdf: '肾功能异常',
  isHavingCtdCdf: '心功能异常',
  isHavingCtdApf: '肺功能异常',
  isHavingCtdE: '水肿',
  isHavingCtdGr: '胃肠道反应'
}
const HaveMahList = {
  isHavingMahPcs: '青霉素类过敏史',
  isHavingMahCps: '头孢类过敏史',
  isHavingMahSfs: '磺胺类过敏史',
  isHavingMahAs: '阿司匹林过敏史',
  isHavingMahAls: '别嘌呤过敏史'
}
import DEFAULT_IMG from '../images/pic_tx.png'
export default {
  computed: {},
  data() {
    return {
      pickerValue1: '',
      ifLoad: true,
      selectSex: '',
      watchWeight: '',
      watchHeight: '',
      weight: '',
      height: '',
      paramDate: '',
      isModified: false,
      healthProfile: '',
      pickerValue: '1980-01-01',
      testData: '',
      value: '',
      logoUrl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495278050272&di=d822cde42e2d62d86c58ce3ce4c4d972&imgtype=0&src=http%3A%2F%2Fp9.pccoo.cn%2Fapp_cover%2F20170503%2F2017050313300693525176_1239_1242.jpg',
      upUrl: 'http://api.carelinker.com/web/wechat-personal/image/ic_up.png',
      downUrl: 'http://api.carelinker.com/web/wechat-personal/image/ic_down.png',
      upOrDown: false,
      currentIndex: -1,
      healthList: [{
        name: '基本信息',
        detail: [{
          name: '性别',
          result: '男'
        }, {
          name: '年龄',
          result: '18岁/1999年1月1日'
        }, {
          name: '体重（公斤）',
          result: ''
        }, {
          name: '身高（厘米）',
          result: ''
        }]
      }, {
        name: '健康信息',
        detail: [{
          name: '习惯',
          result: '无',
          type: 'DrinkSmokeList'
        }, {
          name: '劳动强度',
          result: '',
          type: 'labourLevelList'
        }, {
          name: '症状',
          result: '无',
          type: 'SymptomList'
        }]
      }, {
        name: '用药信息',
        detail: [{
          name: '我的用药信息',
          result: '无用药史',
          type: 'TakeDrugsList'
        }, {
          name: '用药过敏史',
          result: '无',
          type: 'HaveMahList'
        }, {
          name: '用药禁忌症',
          result: '无禁忌症',
          type: 'HaveCtdList'
        }]
      }, {
        name: '体检信息',
        detail: [{
          name: '高血压',
          result: '无高血压',
          type: 'HaveHtnList'
        }, {
          name: '高血压并发症',
          result: '无高血压并发症',
          type: 'HaveHtncList'
        }, {
          name: '糖尿病',
          result: '无糖尿病',
          type: 'HaveDmList'
        }, {
          name: '糖尿病并发症',
          result: '无糖尿病并发症',
          type: 'HaveGdcList'
        }, {
          name: '高血脂',
          result: '无血脂异常',
          type: 'HavingAbl'
        }, {
          name: '心脑血管疾病',
          result: '无心脑血管疾病',
          type: 'HaveCdList'
        }]
      }],
      patientId: '',
      isSelf: ''
    }
  },
  watch: {
    pickerValue: function(val, oldVal) {
      // let value = (2017 - this.healthProfile.birthday.substring(0, 4)) + '岁/' + this.healthProfile.birthday
      if (this.pickerValue !== '2100-01-01') {
        console.log('======')
        this.pickerValue1 = (2017 - this.pickerValue.substring(0, 4)) + '岁/' + this.pickerValue
        this.isModified = true
      }
    },
    watchHeight: function(val, oldVal) {
      // console.log('new: %s, old: %s', val, oldVal)
      if (val !== this.healthProfile.height) {
        this.watchHeight = val
        this.isModified = true
      }
    },
    watchWeight: function(val, oldVal) {
      // console.log('new: %s, old: %s', val, oldVal)
      if (val !== this.healthProfile.weight) {
        this.watchWeight = val
        this.isModified = true
      }
    },
    selectSex: function(val, oldVal) {
      // console.log('new: %s, old: %s', val, oldVal)
      var tempSex = (this.healthProfile.sex === 1 ? '男' : '女')
      if (val !== tempSex) {
        this.isModified = true
      }
    }
  },
  created() {
    let query = this.$route.query
    this.patientId = query.patientId
    this.isSelf = query.isSelf
    this.logoUrl = query.headImgUrl || DEFAULT_IMG
  },
  methods: {
    preventMouse: function(event, index, moreIndex) {
      if (this.isSelf !== 'SELF' && index === 0) {
        console.log('==============')
        event.preventDefault()
      }
    },
    clickSave: function() {
      if (this.isModified) {
        Indicator.open()
        Utils.post(Api.PUT_PROFILE, {
          'pId': this.patientId,
          'height': this.watchHeight,
          'weight': this.watchWeight,
          'birthday': this.pickerValue.replace('-', '').replace('-', ''),
          'sex': this.selectSex === '男' ? 1 : 2
        }, result => {
          if (result.success) {
            Indicator.close()
            this.isModified = false
            Toast({
              message: '修改成功！',
              position: 'middle',
              duration: 2000
            })
          }
        })
      }
    },
    getNeedDate: function(date) {
      var y = date.getFullYear()
      var m = date.getMonth() + 1
      m = m < 10 ? '0' + m : m
      var d = date.getDate()
      d = d < 10 ? ('0' + d) : d
      this.paramDate = y.toString() + m.toString() + d.toString()
      return y + '-' + m + '-' + d
    },
    openPicker: function() {
      this.$refs.picker.open()
      document.getElementsByClassName('mint-datetime-action')[0].style.fontSize = '0.390625rem !important'
      var element = document.getElementsByClassName('picker-slot')
      if (document.getElementsByTagName('body')[0].style.fontSize === '36px') {
        for (let i = 0; i < element.length; i++) {
          element[i].style.fontSize = '0.390625rem'
        }
      }
    },
    goDetail: function(index, moreIndex) {
      if (index > 0) {
        this.$router.push({
          name: 'detail',
          query: {
            index: index,
            moreIndex: moreIndex,
            detailType: this.healthList[index].detail[moreIndex].type,
            list: this.healthList[index].detail[moreIndex].result,
            isSelf: this.isSelf,
            patientId: this.patientId
          }
        })
      }
    },
    showDetail: function(index) {
      if (this.currentIndex === index) {
        this.currentIndex = -1
        this.upOrDown = false
      } else {
        this.currentIndex = index
        this.upOrDown = true
      }
    }
  },
  mounted: function() {
    Utils.setTitle('健康档案')
    Indicator.open()
    Utils.post(Api.GET_PROFILE, {
      'pId': this.patientId
    }, result => {
      this.healthProfile = result.healthProfile
      if (this.healthProfile.birthday) {
        this.healthList[0].detail[1].result = (2017 - this.healthProfile.birthday.substring(0, 4)) + '岁/' + this.healthProfile.birthday
        this.paramDate = this.healthProfile.birthday.replace('-', '').replace('-', '')
        this.pickerValue1 = (2017 - this.healthProfile.birthday.substring(0, 4)) + '岁/' + this.healthProfile.birthday
      }
      this.watchWeight = this.healthProfile.weight
      this.watchHeight = this.healthProfile.height
      this.selectSex = this.healthProfile.sex === 1 ? '男' : '女'
      if (this.healthProfile.labourLevel === 1) {
        this.healthList[1].detail[1].result = '卧床休息'
      } else if (this.healthProfile.labourLevel === 2) {
        this.healthList[1].detail[1].result = '轻体力'
      } else if (this.healthProfile.labourLevel === 3) {
        this.healthList[1].detail[1].result = '中体力'
      } else if (this.healthProfile.labourLevel === 4) {
        this.healthList[1].detail[1].result = '重体力'
      }
      if (this.healthProfile.neverDrinkSmoke === 0) {
        if (this.healthProfile.isSmoke === 1 && this.healthProfile.isDrink === 1) {
          this.healthList[1].detail[0].result = '吸烟,饮酒'
        } else {
          if (this.healthProfile.isSmoke === 1) {
            this.healthList[1].detail[0].result = '吸烟'
          }
          if (this.healthProfile.isDrink === 1) {
            this.healthList[1].detail[0].result = '饮酒'
          }
        }
      }
      if (this.healthProfile.neverHaveSymptom === 0) {
        var symptom = []
        for (let i in SymptomList) {
          if (this.healthProfile[i] !== 0) {
            symptom.push(SymptomList[i])
          }
        }
        this.healthList[1].detail[2].result = symptom.toString()
      }
      if (this.healthProfile.neverTakeDrugs === 0) {
        var drugs = []
        for (let i in TakeDrugsList) {
          if (this.healthProfile[i] !== 0) {
            drugs.push(TakeDrugsList[i])
          }
        }
        this.healthList[2].detail[0].result = drugs.toString()
      }
      if (this.healthProfile.neverHaveDm === 0) {
        var haveDw = []
        for (let i in HaveDmList) {
          if (this.healthProfile[i] !== 0) {
            haveDw.push(HaveDmList[i])
          }
        }
        this.healthList[3].detail[2].result = haveDw.toString()
      }
      if (this.healthProfile.neverHaveGdc === 0) {
        var HaveGdc = []
        for (let i in HaveGdcList) {
          if (this.healthProfile[i] !== 0) {
            HaveGdc.push(HaveGdcList[i])
          }
        }
        this.healthList[3].detail[3].result = HaveGdc.toString()
      }
      if (this.healthProfile.neverHaveHtn === 0) {
        var haveHtn = []
        for (let i in HaveHtnList) {
          if (this.healthProfile[i] !== 0) {
            haveHtn.push(HaveHtnList[i])
          }
        }
        this.healthList[3].detail[0].result = haveHtn.toString()
      }
      if (this.healthProfile.neverHaveHtnc === 0) {
        var haveHtnc = []
        for (let i in HaveHtncList) {
          if (this.healthProfile[i] !== 0) {
            haveHtnc.push(HaveHtncList[i])
          }
        }
        this.healthList[3].detail[1].result = haveHtnc.toString()
      }
      if (this.healthProfile.neverHaveCd === 0) {
        var haveCd = []
        for (let i in HaveCdList) {
          if (this.healthProfile[i] !== 0) {
            haveCd.push(HaveCdList[i])
          }
        }
        this.healthList[3].detail[5].result = haveCd.toString()
      }
      if (this.healthProfile.neverHaveCtd === 0) {
        var haveCtd = []
        for (let i in HaveCtdList) {
          if (this.healthProfile[i] !== 0) {
            haveCtd.push(HaveCtdList[i])
          }
        }
        this.healthList[2].detail[2].result = haveCtd.toString()
      }
      if (this.healthProfile.neverHaveMah === 0) {
        var haveMah = []
        for (let i in HaveMahList) {
          if (this.healthProfile[i] !== 0) {
            haveMah.push(HaveMahList[i])
          }
        }
        this.healthList[2].detail[1].result = haveMah.toString()
      }
      if (this.healthProfile.neverHaveAbl === 0) {
        if (this.healthProfile.isHavingAbl === 1) {
          this.healthList[3].detail[4].result = '高血脂'
        }
      }
      Indicator.close()
      if (this.$route.query.index) {
        // this.healthList[this.$route.query.index].detail[this.$route.query.moreIndex].result = this.$route.query.newList
        this.isModified = true
        this.currentIndex = this.$route.query.index
      }
    })
  }
}
</script>
<style>
.mint-spinner-snake {
  height: 0.78125rem !important;
  width: 0.78125rem !important;
  border: 0.125rem solid transparent;
}

.mint-indicator-wrapper {
  padding: 0.46875rem !important;
}

.mint-datetime-action {
  font-size: 0.3125rem;
}

@media screen and (max-width:800px) and (min-width:320px) {
  .mint-datetime-action {
    font-size: 0.234375rem;
  }
}

.picker-slot {
  font-size: 0.28125rem;
}

.mint-toast-text {
  font-size: 0.46875rem;
}
</style>
<style scoped lang="scss">
.healthDocument {
  width: 100%;
  // height: 100%;
  overflow: hidden;
  .header {
    width: 100%;
    height: 6.40625rem;
    background-color: #2ac4fb;
    .logo {
      width: 2.03125rem;
      height: 2.03125rem;
      margin: 0 auto;
      padding-top: 0.625rem;
      border-radius: 1.015625rem;
      img {
        width: 100%;
        height: 100%;
        border-radius: 1.015625rem;
      }
    }
    .name {
      width: 100%;
      text-align: center;
      font-size: 0.46875rem;
      margin-top: 0.3125rem;
      color: white;
      opacity: 0.5;
    }
    .tips {
      width: 100%;
      text-align: center;
      color: white;
      font-size: 0.3125rem;
      margin-top: 0.3125rem;
    }
    .progress {
      width: 4.0625rem;
      margin: 0 auto;
      height: 0.3125rem;
      border-radius: 2px;
      margin-top: 20px;
      .percentage {
        width: 3.125rem;
        height: 0.3125rem;
        background-color: white;
        font-size: 0.28125rem;
        color: #2ac4fb;
        text-align: center;
        float: left;
      }
      .percentage1 {
        width: 0.9375rem;
        height: 0.3125rem;
        line-height: 0.3125rem;
        background-color: white;
        opacity: 0.5;
        font-size: 18px;
        color: #2ac4fb;
        text-align: center;
        float: left;
      }
    }
    .topImg {
      width: 100%;
      position: absolute;
      top: 5.109375rem;
      img {
        width: 100%;
      }
    }
  }
  .first {
    width: 100%;
    height: 1.40625rem;
    line-height: 1.40625rem;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    border-bottom: 0.015625rem solid #d2d3d5;
    .listName {
      font-size: 0.46875rem;
      padding-left: 0.46875rem;
    }
    .listName1 {
      padding-right: 0.46875rem;
      .listNameImg {
        width: 0.28125rem;
        height: 0.15rem;
        margin-top: 0.46875rem;
      }
    }
  }
  .detail {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    height: 1.40625rem;
    line-height: 1.40625rem;
    border-bottom: 0.015625rem solid #d2d3d5;
    font-size: 0.46875rem;
    select {
      border-width: 0;
      appearance: none;
      padding-right: 0.46875rem;
      font-size: 0.46875rem;
    }
    input {
      text-align: right;
      padding-right: 0.46875rem;
    }
    .detailStyle {
      color: grey;
      padding-left: 0.46875rem;
    }
    .detailStyle1 {
      padding-right: 0.46875rem;
    }
  }
  .bottomBtn {
    height: 1.25rem;
    line-height: 1.25rem;
    margin-top: 0.78125rem;
    margin-bottom: 40px;
    font-size: 0.46875rem;
    .save {
      margin-left: 0.46875rem;
      margin-right: 0.46875rem;
      border-radius: 0.03125rem;
      text-align: center;
      opacity: 0.5;
      color: white;
      background-color: #2ac4fb;
    }
    .newSave {
      opacity: 1;
    }
  }
}
</style>
