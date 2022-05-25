let api = {
  VIDEO_SECTIONS: ['get', 'clCms/api/videos/knowledge/lessons/lesson/sections'],
  VIDEO_LIKE: ['put', 'clCms/api/videos/knowledge/lessons/lesson/like'],
  LECTURER_INFO: ['get', 'clCms/api/lecturers/lecturer'],
  VIDEO_FIRST_CLASS: ['get', '/clCms/api/videos/knowledge/firstClassList'],
  VIDEO_LESSONS: ['get', '/clCms/api/videos/knowledge/lessons'],
  ARTICLE_SECTIONS: ['get', '/clCms/api/articles/knowledge/lessons/lesson/sections'],
  ARTICLE_FIRST_CLASS: ['get', '/clCms/api/articles/knowledge/firstClassList'],
  ARTICLE_LESSONS: ['get', '/clCms/api/articles/knowledge/lessons'],
  LECTURE_LIST: ['get', '/clCms/api/lectures/knowledge/lessons'],

  MEMBER_STYLES: ['get', '/clEnterprise/api/store/storeWechatSetup'],
  MEMBER_CARD: ['get', '/clCrm/api/crm/users/user/msCards'],
  MEMBER_LOGIN: ['post', '/clSystem/api/loginInfo/wechat/member'],
  MEMBER_GET_APPLY: ['get', '/clCrm/api/crm/users/user/msCards/msCard/application'],
  MEMBER_POST_APPLY: ['post', '/clCrm/api/crm/users/user/msCards/msCard/application'],
  SEND_MESSAGE: ['get', '/clSystem/api/captcha'],
  CHECK_MESSAGE: ['get', '/clSystem/api/captcha/check'],
  GET_PROFILE: ['get', '/clPatient/api/profiles/profile'],
  PUT_PROFILE: ['put', '/clPatient/api/profiles/profile'],
  CHANGE_PROFILE: ['put', '/clPatient/api/profiles/profile'],
  PURCHASE_RECORD: ['get', '/clDrug/api/drug/drugBoughtRecord/all/table/datas'],

  DIET_SCHEME: ['get', '/clPatient/api/nes/2/recipes'],
  GET_DEVICE_LIST: ['get', '/clDevice/api/device/personalDevicesAndUserDevices'],
  DELETE_DEVICE_LIST: ['delete', '/clDevice/api/device/userDevices/device'],
  POST_DEVICE_LIST: ['post', '/clDevice/api/device/userDevices/device'],

  POST_BIND_USER_WECHAT: ['post', '/clWechat/api/wechat/user/info'], // 用户和微信绑定
  GET_WECHAT_INFO: ['get', '/clWechat/api/wechat/user/info'], // 获取微信信息
  POST_WECHAT_SIGN: ['post', '/clWechat/api/wechat/sign'], // 获取微信sign
  GET_DEVICE_STATUS: ['get', '/clDevice/api/device/device/status'], // 扫描设备获取绑定状态
  POST_BIND_DEVICE: ['post', '/clDevice/api/device/userDevices/device'], // 设备绑定
  GET_DEVICES: ['get', '/clDevice/api/device/personalDevices'], // 获取可以绑定的设备列表
  GET_USER_FOLLOW: ['get', '/clPatient/api/user/follow/followed'], // 获取用户关注的用户列表
  POST_USER_FOLLOW_ADD: ['post', '/clPatient/api/user/follow/'], // 关注用户
  DELETE_USER_WECHAT_REL: ['delete', '/clWechat/api/wechat/user/info'], // 用户和微信解绑
  DELETE_USER_FOLLOW: ['delete', '/clPatient/api/user/follow/'], // 取消关注
  GET_ARTICLES: ['get', '/clCms/api/articles/pes'] // 获取个人用户文章列表
}

export default api
