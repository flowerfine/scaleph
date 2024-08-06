import {Form, message} from 'antd';
import {ModalForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import {DICT_TYPE} from "@/constants/dictType";
import {SysDictService} from "@/services/admin/system/sysDict.service";
import {RoleService} from "@/services/admin/security/role.service";
import {SecRole} from "@/services/admin/typings";
import {ModalFormProps} from '@/typings';

const RoleForm: React.FC<ModalFormProps<SecRole>> = ({data, visible, onVisibleChange, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <ModalForm
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.admin.role'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.admin.role'})
      }
      form={form}
      open={visible}
      onOpenChange={onVisibleChange}
      width={580}
      layout={"horizontal"}
      labelCol={{span: 6}}
      wrapperCol={{span: 16}}
      modalProps={{
        destroyOnClose: true,
        closeIcon: false,
      }}
      initialValues={{
        id: data.id,
        code: data.code,
        name: data.name,
        type: data.type?.value,
        status: data.status?.value,
        remark: data.remark
      }}
      onFinish={(values: Record<string, any>) => {
        return values.id
          ? RoleService.updateRole({...values}).then((response) => {
            if (response.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
              if (onVisibleChange) {
                onVisibleChange(false);
              }
            }
          })
          : RoleService.addRole({...values}).then((response) => {
            if (response.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
              if (onVisibleChange) {
                onVisibleChange(false);
              }
            }
          });
      }}
    >
      <ProFormDigit name="id" hidden/>
      <ProFormText
        name={"name"}
        label={intl.formatMessage({id: 'pages.admin.role.name'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={"code"}
        label={intl.formatMessage({id: 'pages.admin.role.code'})}
        rules={[{required: true}]}
      />
      <ProFormSelect
        name={"type"}
        label={intl.formatMessage({id: 'pages.admin.role.type'})}
        allowClear={false}
        rules={[{required: true}]}
        initialValue={"02"}
        disabled={true}
        request={() => SysDictService.listDictByDefinition(DICT_TYPE.roleType)}
      />
      <ProFormSelect
        name={"status"}
        label={intl.formatMessage({id: 'pages.admin.role.status'})}
        request={() => SysDictService.listDictByDefinition(DICT_TYPE.roleStatus)}
        allowClear={false}
        rules={[{required: true}]}
      />
      <ProFormTextArea
        name={"remark"}
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ModalForm>
  );

}

export default RoleForm;
