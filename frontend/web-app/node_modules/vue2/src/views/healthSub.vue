<template>
  <div class="healthSub">
    <div class="list" v-for="(item,index) in healthList">
      <div class="first" @click="clickItem(index,$event)">
        <div class="listName">{{item.name}}</div>
        <div class="listName1">
          <img :src="logoUrl" v-if="item.isSelected == 1">
        </div>
      </div>
    </div>
    <div class="bottomBtn" v-if="isSelf === 'SELF'">
      <div class="save" v-bind:class="{'newSave':isModified}" ref="testref" @click="clickBack()">保存修改</div>
    </div>
  </div>
</template>
<script>
import Utils from '../utils'
import Api from '../api'
import {
  Indicator
} from 'mint-ui'
var SymptomList = [{
  name: '失眠',
  isSelected: 0,
  key: 'isHavingSymInsomnia'
}, {
  name: '便秘',
  isSelected: 0,
  key: 'isHavingSymConstipation'
}, {
  name: '乏力',
  isSelected: 0,
  key: 'isHavingSymW'
}, {
  name: '多饮',
  isSelected: 0,
  key: 'isHavingSymPda'
}, {
  name: '多食',
  isSelected: 0,
  key: 'isHavingSymPha'
}, {
  name: '多尿',
  isSelected: 0,
  key: 'isHavingSymPu'
}, {
  name: '体重下降',
  isSelected: 0,
  key: 'isHavingSymLw'
}, {
  name: '视物模糊',
  isSelected: 0,
  key: 'isHavingSymB'
}, {
  name: '手足发凉',
  isSelected: 0,
  key: 'isHavingSymHfc'
}, {
  name: '手足麻木',
  isSelected: 0,
  key: 'isHavingSymHfn'
}, {
  name: '皮肤瘙痒',
  isSelected: 0,
  key: 'isHavingSymP'
}, {
  name: '腹泻',
  isSelected: 0,
  key: 'isHavingSymDia'
}, {
  name: '头晕',
  isSelected: 0,
  key: 'isHavingSymDis'
}, {
  name: '头痛',
  isSelected: 0,
  key: 'isHavingSymHa'
}, {
  name: '颈项板紧',
  isSelected: 0,
  key: 'isHavingSymNs'
}, {
  name: '心悸',
  isSelected: 0,
  key: 'isHavingSymPa'
}, {
  name: '无',
  isSelected: 0,
  key: 'neverHaveSymptom'
}]
var TakeDrugsList = [{
  name: '利尿剂',
  isSelected: 0,
  key: 'isTakingDds'
}, {
  name: '非甾体抗炎药',
  isSelected: 0,
  key: 'isTakingNsaids'
}, {
  name: '抗高血压药',
  isSelected: 0,
  key: 'isTakingAhds'
}, {
  name: '抗凝药',
  isSelected: 0,
  key: 'isTakingApds'
}, {
  name: '降低脂质药物',
  isSelected: 0,
  key: 'isTakingLlds'
}, {
  name: '抗心律失常药物',
  isSelected: 0,
  key: 'isTakingAads'
}, {
  name: 'β肾上腺素受体阻断剂',
  isSelected: 0,
  key: 'isTakingBabds'
}, {
  name: '抗糖尿病药',
  isSelected: 0,
  key: 'isTakingAdds'
}, {
  name: '正性肌力药物',
  isSelected: 0,
  key: 'isTakingPids'
}, {
  name: '抗心绞痛药',
  isSelected: 0,
  key: 'isTakingAgds'
}, {
  name: '抗血栓药',
  isSelected: 0,
  key: 'isTakingAtds'
}, {
  name: '无用药史',
  isSelected: 0,
  key: 'neverTakeDrugs'
}]
var HaveDmList = [{
  name: '糖尿病家族史',
  isSelected: 0,
  key: 'isHavingDmFhod'
}, {
  name: '1型糖尿病',
  isSelected: 0,
  key: 'isHavingDm1'
}, {
  name: '2型糖尿病',
  isSelected: 0,
  key: 'isHavingDm2'
}, {
  name: '妊娠期糖尿病',
  isSelected: 0,
  key: 'isHavingDmG'
}, {
  name: '特殊类型糖尿病',
  isSelected: 0,
  key: 'isHavingDmS'
}, {
  name: '无糖尿病',
  isSelected: 0,
  key: 'neverHaveDm'
}]
var HaveGdcList = [{
  name: '糖尿病肾病',
  isSelected: 0,
  key: 'isHavingGdcDn'
}, {
  name: '糖尿病性视网膜病变/糖尿病性白内障',
  isSelected: 0,
  key: 'isHavingGdcDr'
}, {
  name: '糖尿病足',
  isSelected: 0,
  key: 'isHavingGdcDf'
}, {
  name: '糖尿病合并感染:呼吸道/泌尿道/皮肤/口腔',
  isSelected: 0,
  key: 'isHavingGdcDci'
}, {
  name: '糖尿病并发神经末梢感觉障碍',
  isSelected: 0,
  key: 'isHavingGdcPsd'
}, {
  name: '无糖尿病并发症',
  isSelected: 0,
  key: 'neverHaveGdc'
}]
var HaveHtnList = [{
  name: '高血压家族史',
  isSelected: 0,
  key: 'isHavingHtnFfoh'
}, {
  name: '原发性高血压',
  isSelected: 0,
  key: 'isHavingHtnP'
}, {
  name: '继发性高血压',
  isSelected: 0,
  key: 'isHavingHtnS'
}, {
  name: '无高血压',
  isSelected: 0,
  key: 'neverHaveHtn'
}]
var HaveHtncList = [{
  name: '高血压脑病',
  isSelected: 0,
  key: 'isHavingHtncHe'
}, {
  name: '高血压引起动脉硬化、主动脉夹层',
  isSelected: 0,
  key: 'isHavingHtncAaad'
}, {
  name: '高血压性肾损害',
  isSelected: 0,
  key: 'isHavingHtncHrd'
}, {
  name: '高血压引起视网膜动脉硬化、眼底改变',
  isSelected: 0,
  key: 'isHavingHtncRaafc'
}, {
  name: '高血压引起左心室肥厚',
  isSelected: 0,
  key: 'isHavingHtncLvh'
}, {
  name: '无高血压并发症',
  isSelected: 0,
  key: 'neverHaveHtnc'
}]
var HaveCdList = [{
  name: '心绞痛',
  isSelected: 0,
  key: 'isHavingCdTia'
}, {
  name: '短暂性缺血性发作',
  isSelected: 0,
  key: 'isHavingCdI'
}, {
  name: '心力衰竭',
  isSelected: 0,
  key: 'isHavingCdHf'
}, {
  name: '心肌梗死',
  isSelected: 0,
  key: 'isHavingCdMi'
}, {
  name: '其他心脏病',
  isSelected: 0,
  key: 'isHavingCdOhd'
}, {
  name: '心血管疾病家族史',
  isSelected: 0,
  key: 'isHavingCdFhoa'
}, {
  name: '出血性脑卒中',
  isSelected: 0,
  key: 'isHavingCdHs'
}, {
  name: '缺血性脑卒中',
  isSelected: 0,
  key: 'isHavingCdCah'
}, {
  name: '脑卒中家族史',
  isSelected: 0,
  key: 'isHavingCdFhos'
}, {
  name: '冠心病',
  isSelected: 0,
  key: 'isHavingCdChd'
}, {
  name: '心肌缺血',
  isSelected: 0,
  key: 'isHavingCdIm'
}, {
  name: '心房颤动',
  isSelected: 0,
  key: 'isHavingCdAf'
}, {
  name: '无心脑血管疾病',
  isSelected: 0,
  key: 'neverHaveCd'
}]
var HaveCtdList = [{
  name: '肝功能异常',
  isSelected: 0,
  key: 'isHavingCtdAlf'
}, {
  name: '肾功能异常',
  isSelected: 0,
  key: 'isHavingCtdRdf'
}, {
  name: '心功能异常',
  isSelected: 0,
  key: 'isHavingCtdCdf'
}, {
  name: '肺功能异常',
  isSelected: 0,
  key: 'isHavingCtdApf'
}, {
  name: '水肿',
  isSelected: 0,
  key: 'isHavingCtdE'
}, {
  name: '胃肠道反应',
  isSelected: 0,
  key: 'isHavingCtdGr'
}, {
  name: '无禁忌症',
  isSelected: 0,
  key: 'neverHaveCtd'
}]
var HaveMahList = [{
  name: '青霉素类过敏史',
  isSelected: 0,
  key: 'isHavingMahPcs'
}, {
  name: '头孢类过敏史',
  isSelected: 0,
  key: 'isHavingMahCps'
}, {
  name: '磺胺类过敏史',
  isSelected: 0,
  key: 'isHavingMahSfs'
}, {
  name: '阿司匹林过敏史',
  isSelected: 0,
  key: 'isHavingMahAs'
}, {
  name: '别嘌呤过敏史',
  isSelected: 0,
  key: 'isHavingMahAls'
}, {
  name: '无过敏史',
  isSelected: 0,
  key: 'neverHaveMah'
}]
var labourLevelList = [{
  name: '卧床休息',
  isSelected: 0,
  key: 'labourLevel'
}, {
  name: '轻体力',
  isSelected: 0,
  key: 'labourLevel'
}, {
  name: '中体力',
  isSelected: 0,
  key: 'labourLevel'
}, {
  name: '重体力',
  isSelected: 0,
  key: 'labourLevel'
}]
var DrinkSmokeList = [{
  name: '吸烟',
  isSelected: 0,
  key: 'isSmoke'
}, {
  name: '饮酒',
  isSelected: 0,
  key: 'isDrink'
}, {
  name: '无',
  isSelected: 0,
  key: 'neverDrinkSmoke'
}]
var HavingAbl = [{
  name: '高血脂',
  isSelected: 0,
  key: 'isHavingAbl'
}, {
  name: '无血脂异常',
  isSelected: 0,
  key: 'neverHaveAbl'
}]
export default {
  data() {
    return {
      patientId: '',
      isSelf: '',
      currentIndex: -1,
      // newSelectList: [],
      isModified: false,
      logoUrl: 'http://api.carelinker.com/web/wechat-personal/image/ic_right.png',
      healthList: []
    }
  },
  created() {
    let query = this.$route.query
    this.isSelf = query.isSelf
    this.patientId = query.patientId
  },
  mounted: function() {
    var selectList = this.$route.query.list.split(',')
    if (this.$route.query.detailType === 'SymptomList') {
      this.healthList = SymptomList
    } else if (this.$route.query.detailType === 'TakeDrugsList') {
      this.healthList = TakeDrugsList
    } else if (this.$route.query.detailType === 'HaveDmList') {
      this.healthList = HaveDmList
    } else if (this.$route.query.detailType === 'HaveGdcList') {
      this.healthList = HaveGdcList
    } else if (this.$route.query.detailType === 'HaveHtnList') {
      this.healthList = HaveHtnList
    } else if (this.$route.query.detailType === 'HaveHtncList') {
      this.healthList = HaveHtncList
    } else if (this.$route.query.detailType === 'HaveCdList') {
      this.healthList = HaveCdList
    } else if (this.$route.query.detailType === 'HaveCtdList') {
      this.healthList = HaveCtdList
    } else if (this.$route.query.detailType === 'HaveMahList') {
      this.healthList = HaveMahList
    } else if (this.$route.query.detailType === 'labourLevelList') {
      this.healthList = labourLevelList
    } else if (this.$route.query.detailType === 'DrinkSmokeList') {
      this.healthList = DrinkSmokeList
    } else if (this.$route.query.detailType === 'HavingAbl') {
      this.healthList = HavingAbl
    }
    for (let i = 0; i < selectList.length; i++) {
      for (let j = 0; j < this.healthList.length; j++) {
        if (selectList[i] === this.healthList[j].name) {
          this.healthList[j].isSelected = 1
          this.currentIndex = j
        }
      }
    }
  },
  methods: {
    clickItem: function(index, event) {
      if (this.isSelf !== 'SELF') {
        event.preventDefault()
      } else {
        this.isModified = true
        if (this.$route.query.detailType === 'labourLevelList' || index === this.healthList.length - 1) {
          for (var i = 0; i < this.healthList.length; i++) {
            this.healthList[i].isSelected = 0
          }
          this.healthList[index].isSelected = 1
          this.currentIndex = index
        } else {
          if (this.healthList[index].isSelected === 0) {
            this.healthList[index].isSelected = 1
            this.healthList[this.healthList.length - 1].isSelected = 0
          } else {
            this.healthList[index].isSelected = 0
          }
        }
      }
    },
    clickBack: function() {
      var modifyList = {}
      for (let i = 0; i < this.healthList.length; i++) {
        if (this.healthList[i].isSelected === 1) {
          // this.newSelectList.push(this.healthList[i].name)
          if (this.healthList[i].key === 'labourLevel') {
            modifyList[this.healthList[i].key] = this.currentIndex + 1
          } else {
            modifyList[this.healthList[i].key] = 1
          }
        } else {
          if (this.healthList[i].key === 'labourLevel') {
            modifyList[this.healthList[i].key] = this.currentIndex + 1
          } else {
            modifyList[this.healthList[i].key] = 0
          }
        }
      }
      // modifyList['employeeId'] = 1154
      // modifyList['euId'] = '1154'
      // modifyList['stId'] = '0000001'
      modifyList['pId'] = this.patientId
      console.log(modifyList)
      Indicator.open()
      Utils.post(Api.PUT_PROFILE, modifyList, result => {
        if (result.success) {
          Indicator.close()
          this.$router.push({
            name: 'document',
            query: {
              index: this.$route.query.index,
              moreIndex: this.$route.query.moreIndex,
              isSelf: this.isSelf,
              patientId: this.patientId
            }
          })
        }
      })
    }
  }
}
</script>
<style scoped lang="scss">
.healthSub {
  width: 100%;
  height: 100%;
  .first {
    display: flex;
    height: 1.40625rem;
    line-height: 1.40625rem;
    flex-direction: row;
    justify-content: space-between;
    border-bottom: 0.015625rem solid #d2d3d5;
    .listName {
      padding-left: 0.46875rem;
      font-size: 0.46875rem;
    }
    .listName1 {
      padding-right: 0.46875rem;
      img {
        width: 0.46875rem;
        height: 0.46875rem;
        margin-top: 0.46875rem;
      }
    }
  }
  .bottomBtn {
    height: 1.25rem;
    line-height: 1.25rem;
    margin-top: 70px;
    align-items: center;
    margin-bottom: 40px;
    font-size: 0.46875rem;
  }
  .save {
    margin-right: 0.46875rem;
    margin-left: 0.46875rem;
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
</style>
