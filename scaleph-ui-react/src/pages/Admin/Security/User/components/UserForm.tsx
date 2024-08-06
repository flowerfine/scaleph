import React from "react";
import {Form, message, Modal} from 'antd';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormSelect,
  ProFormText,
  ProFormTextArea
} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import {DICT_TYPE} from '@/constants/dictType';
import {SysDictService} from "@/services/admin/system/sysDict.service";
import {SecUser} from '@/services/admin/typings';
import {UserService} from '@/services/admin/security/user.service';
import {ModalFormProps} from '@/typings';

const UserForm: React.FC<ModalFormProps<SecUser>> = ({data, visible, onOK, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.admin.user'})
          : intl.formatMessage({id: 'app.common.operate.new.label'}) +
          intl.formatMessage({id: 'pages.admin.user'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let user: SecUser = {
            id: values.id,
            type: values.type,
            userName: values.userName,
            password: values.password,
            nickName: values.nickName,
            avatar: values.avatar,
            email: values.email,
            phone: values.phone,
            status: values.status,
            order: values.order,
            remark: values.remark,
          };
          data.id
            ? UserService.updateUser({...user}).then((resp) => {
              if (resp.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                if (onOK) {
                  onOK(values);
                }
              }
            })
            : UserService.add({...user}).then((resp) => {
              if (resp.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                if (onOK) {
                  onOK(values);
                }
              }
            });
        });
      }}
    >
      <ProForm
        form={form}
        layout="horizontal"
        submitter={false}
        labelCol={{span: 6}}
        wrapperCol={{span: 16}}
        initialValues={{
          id: data.id,
          type: data.type?.value,
          userName: data.userName,
          nickName: data.nickName,
          avatar: data.avatar,
          email: data.email,
          phone: data.phone,
          status: data.status?.value,
          order: data.order,
          remark: data.remark,
        }}
      >
        <ProFormText name="id" hidden/>
        <ProFormSelect
          name="type"
          label={intl.formatMessage({id: 'pages.admin.user.type'})}
          rules={[{required: true}]}
          disabled={data.id ? true : false}
          request={() => {
            return SysDictService.listDictByDefinition(DICT_TYPE.carpSecUserType)
          }}
        />
        <ProFormText
          name="userName"
          label={intl.formatMessage({id: 'pages.admin.user.userName'})}
          disabled={data.id ? true : false}
          rules={[
            {required: true},
            {max: 30},
            {min: 5},
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({id: 'app.common.validate.characterWord'}),
            }
          ]}
        />
        <ProFormDependency name={["id"]}>
          {({id}) => {
            if (id) {
              return (<></>)
            }
            return <ProFormText.Password
              name="password"
              label={intl.formatMessage({id: 'pages.admin.user.password'})}
              rules={[{required: true}, {max: 50}]}
            />;
          }}
        </ProFormDependency>
        <ProFormText
          name="nickName"
          label={intl.formatMessage({id: 'pages.admin.user.nickName'})}
          rules={[{required: true}, {max: 50}]}
        />
        <ProFormText
          name="email"
          label={intl.formatMessage({id: 'pages.admin.user.email'})}
          rules={[
            {max: 100},
            {type: 'email'}
          ]}
        />
        <ProFormText
          name="phone"
          label={intl.formatMessage({id: 'pages.admin.user.phone'})}
          rules={[{max: 30}]}
        />
        <ProFormSelect
          name="status"
          label={intl.formatMessage({id: 'pages.admin.user.status'})}
          rules={[{required: true}]}
          request={() => {
            return SysDictService.listDictByDefinition(DICT_TYPE.carpSecUserStatus)
          }}
        />
        <ProFormDigit
          name="order"
          label={intl.formatMessage({id: 'pages.admin.user.order'})}
          initialValue={0}
          min={0}
        />
        <ProFormTextArea
          name={"remark"}
          label={intl.formatMessage({id: 'app.common.data.remark'})}
        />
      </ProForm>
    </Modal>
  );
};

export default UserForm;
