import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from '@/constants/dictType';
import {DictDataService} from '@/services/admin/dictData.service';
import {SecUser} from '@/services/admin/typings';
import {UserService} from '@/services/admin/user.service';
import {Form, message, Modal} from 'antd';
import {useIntl} from 'umi';
import {ProForm, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";

const UserForm: React.FC<ModalFormProps<SecUser>> = ({data, visible, onVisibleChange, onCancel}) => {
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
            nickName: values.nickName,
            avater: values.avater,
            email: values.email,
            phone: values.phone,
            gender: values.gender,
            status: values.status,
            address: values.address,
            summary: values.summary,
          };
          data.id
            ? UserService.updateUser({...user}).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}));
                onVisibleChange(false);
              }
            })
            : UserService.addUser({...user}).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.new.success'}));
                onVisibleChange(false);
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
          gender: data.gender?.value,
          address: data.address,
          summary: data.summary,
        }}
      >
        <ProFormText name="id" hidden/>
        <ProFormSelect
          name="type"
          label={intl.formatMessage({id: 'pages.admin.user.type'})}
          rules={[{required: true}]}
          disabled={data.id ? true : false}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.userType)
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
            },
            {
              validator: (rule, value, callback) => {
                data.id
                  ? callback()
                  : UserService.isUserExists(value).then((resp) => {
                    if (resp) {
                      callback();
                    } else {
                      callback(
                        intl.formatMessage({id: 'app.common.validate.sameUserName'}),
                      );
                    }
                  });
              },
            },
          ]}
        />
        <ProFormText
          name="nickName"
          label={intl.formatMessage({id: 'pages.admin.user.nickName'})}
          rules={[{max: 50}]}
        />
        <ProFormText
          name="email"
          label={intl.formatMessage({id: 'pages.admin.user.email'})}
          rules={[
            {required: true},
            {max: 100},
            {type: 'email'},
            {
              validator: (rule, value, callback) => {
                data.id
                  ? callback()
                  : UserService.isEmailExists(value).then((resp) => {
                    if (resp) {
                      callback();
                    } else {
                      callback(intl.formatMessage({id: 'app.common.validate.sameEmail'}));
                    }
                  });
              },
            },
          ]}
        />
        <ProFormText
          name="phone"
          label={intl.formatMessage({id: 'pages.admin.user.phone'})}
          rules={[{max: 30}]}
        />
        <ProFormSelect
          name="gender"
          label={intl.formatMessage({id: 'pages.admin.user.gender'})}
          rules={[{max: 30}]}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.gender)
          }}
        />
        <ProFormTextArea
          name="summary"
          label={intl.formatMessage({id: 'pages.admin.user.summary'})}
          rules={[{max: 500}]}
        />
      </ProForm>
    </Modal>
  );
};

export default UserForm;
