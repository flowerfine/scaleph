import component from './zh-CN/component';
import globalHeader from './zh-CN/globalHeader';
import menu from './zh-CN/menu';
import pages from './zh-CN/pages/pages';
import pwa from './zh-CN/pwa';
import settingDrawer from './zh-CN/settingDrawer';
import settings from './zh-CN/settings';
import studio from './zh-CN/studio';

export default {
  'navBar.lang': '语言',
  'layout.user.link.help': '帮助',
  'layout.user.link.privacy': '隐私',
  'layout.user.link.terms': '条款',
  'app.copyright.produced': '蚂蚁集团体验技术部出品',
  'app.preview.down.block': '下载此页面到本地项目',
  'app.welcome.link.fetch-blocks': '获取全部区块',
  'app.welcome.link.block-list': '基于 block 开发，快速构建标准页面',
  ...pages,
  ...globalHeader,
  ...menu,
  ...settingDrawer,
  ...settings,
  ...pwa,
  ...component,
  ...studio,
  //scaleph app
  'app.title.short': 'Scaleph',
  'app.title.long': 'Scaleph',
  'app.common.operate.label': '操作',
  'app.common.operate.success': '操作成功',
  'app.common.operate.new.label': '新增',
  'app.common.operate.new.success': '新增成功',
  'app.common.operate.edit.label': '修改',
  'app.common.operate.edit.success': '修改成功',
  'app.common.operate.delete.label': '删除',
  'app.common.operate.delete.success': '删除成功',
  'app.common.operate.delete.confirm.title': '确认删除？',
  'app.common.operate.delete.confirm.content': '数据删除后不可恢复，请谨慎操作！',
  'app.common.operate.refresh.label': '刷新',
  'app.common.operate.query.label': '查询',
  'app.common.operate.reset.label': '重置',
  'app.common.operate.confirm.label': '确认',
  'app.common.operate.cancel.label': '取消',
  'app.common.operate.download.label': '下载',
  'app.common.operate.enable.label': '启用',
  'app.common.operate.grant.label': '授权',
  'app.common.operate.grant.title': '{name}授权',
  'app.common.error.label': '错误',
  'app.common.validate.characterWord': '只能输入字母数字、下划线',
  'app.common.validate.characterWord2': '只能输入字母数字、下划线和点号',
  'app.common.validate.number': '只能输入数字',
  'app.common.validate.sameUserName': '用户名已存在',
  'app.common.validate.sameEmail': '邮箱已存在',
  'app.common.validate.samePassword': '两次输入的密码不一致',
  'app.common.validate.patternPassword': '密码格式错误，请输入数字和字母组成的字符串',
  'app.common.validate.sameToOldPassword': '新密码不能与旧密码一致',
};
