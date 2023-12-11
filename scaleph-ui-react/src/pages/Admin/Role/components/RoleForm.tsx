import {useIntl} from 'umi';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from "@/constants/dictType";
import {SecRole} from "@/services/admin/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {RoleService} from "@/services/admin/role.service";

const RoleForm: React.FC<ModalFormProps<SecRole>> = ({data, visible, onVisibleChange, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.admin.role'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.admin.role'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          data.id
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
        });
      }}
    >
      <ProForm
        form={form}
        layout={"horizontal"}
        submitter={false}
        labelCol={{span: 6}}
        wrapperCol={{span: 16}}
        initialValues={{
          id: data.id,
          code: data.code,
          name: data.name,
          type: data.type?.value,
          status: data.status?.value,
          remark: data.remark
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
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.roleType)}
        />
        <ProFormSelect
          name={"status"}
          label={intl.formatMessage({id: 'pages.admin.role.status'})}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.roleStatus)}
          allowClear={false}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );

}

export default RoleForm;
