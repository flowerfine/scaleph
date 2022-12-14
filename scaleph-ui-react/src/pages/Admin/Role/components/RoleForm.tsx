import {useIntl} from 'umi';
import {Form, message, Modal} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from "@/constant";
import {SecRole} from "@/services/admin/typings";
import {DictDataService} from "@/services/admin/dictData.service";
import {RoleService} from "@/services/admin/role.service";

const RoleForm: React.FC<ModalFormProps<SecRole>> = ({
                                                       data,
                                                       visible,
                                                       onVisibleChange,
                                                       onCancel,
                                                     }) => {
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
          roleCode: data.roleCode,
          roleName: data.roleName,
          roleType: data.roleType?.value,
          roleStatus: data.roleStatus?.value,
          roleDesc: data.roleDesc
        }}
      >
        <ProFormDigit name="id" hidden/>
        <ProFormText
          name={"roleName"}
          label={intl.formatMessage({id: 'pages.admin.role.roleName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"roleCode"}
          label={intl.formatMessage({id: 'pages.admin.role.roleCode'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={"roleType"}
          label={intl.formatMessage({id: 'pages.admin.role.roleType'})}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.roleType)}
          allowClear={false}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={"roleStatus"}
          label={intl.formatMessage({id: 'pages.admin.role.roleStatus'})}
          request={() => DictDataService.listDictDataByType2(DICT_TYPE.roleStatus)}
          allowClear={false}
          rules={[{required: true}]}
        />
        <ProFormText
          name={"roleDesc"}
          label={intl.formatMessage({id: 'pages.admin.role.roleDesc'})}
        />
      </ProForm>
    </Modal>
  );

}

export default RoleForm;
