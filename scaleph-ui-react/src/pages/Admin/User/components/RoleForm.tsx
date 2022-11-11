import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { RoleService } from '@/services/admin/role.service';
import { SecRole } from '@/services/admin/typings';
import { Form, Input, message, Modal, Select } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const RoleForm: React.FC<ModalFormProps<SecRole>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [roleStatusList, setRoleStatusList] = useState<Dict[]>([]);

  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.roleStatus).then((d) => {
      setRoleStatusList(d);
    });
  }, []);

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.admin.user.role' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.admin.user.role' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let role: SecRole = {
            id: values.id,
            roleCode: values.roleCode,
            roleName: values.roleName,
            roleStatus: { value: values.roleStatus },
            roleDesc: values.roleDesc,
          };
          data.id
            ? RoleService.updateRole({ ...role }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : RoleService.addRole({ ...role }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                  onVisibleChange(false);
                }
              });
        });
      }}
    >
      <Form
        form={form}
        layout="horizontal"
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 16 }}
        initialValues={{
          id: data.id,
          roleCode: data.roleCode,
          roleName: data.roleName,
          roleStatus: data.roleStatus?.value,
          roleDesc: data.roleDesc,
        }}
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="roleCode"
          label={intl.formatMessage({ id: 'pages.admin.user.role.roleCode' })}
          rules={[
            { required: true },
            { max: 30 },
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
            },
          ]}
        >
          <Input disabled={data.id ? true : false}></Input>
        </Form.Item>
        <Form.Item
          name="roleName"
          label={intl.formatMessage({ id: 'pages.admin.user.role.roleName' })}
          rules={[{ required: true }, { max: 60 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="roleStatus"
          label={intl.formatMessage({ id: 'pages.admin.user.role.roleStatus' })}
          rules={[{ required: true }]}
        >
          <Select
            showSearch={true}
            allowClear={false}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {roleStatusList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        </Form.Item>
        <Form.Item
          name="roleDesc"
          label={intl.formatMessage({ id: 'pages.admin.user.role.roleDesc' })}
          rules={[{ max: 100 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default RoleForm;
