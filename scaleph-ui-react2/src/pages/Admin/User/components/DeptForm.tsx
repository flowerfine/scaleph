import { TreeNode } from '@/app.d';
import { DeptService } from '@/services/admin/dept.service';
import { SecDept } from '@/services/admin/typings';
import { Form, Input, message, Modal, TreeSelect } from 'antd';
import { useIntl } from 'umi';

interface DeptFormProps<SecDept> {
  data: SecDept;
  visible: boolean;
  isUpdate: boolean;
  treeData: TreeNode[];
  onVisibleChange: (visible: boolean) => void;
  onCancel: () => void;
}

const DeptForm: React.FC<DeptFormProps<SecDept>> = ({
  data,
  visible,
  isUpdate,
  treeData,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        isUpdate
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.admin.user.dept' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.admin.user.dept' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          isUpdate
            ? DeptService.updateDept({ ...values }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : DeptService.addDept({ ...values }).then((d) => {
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
        initialValues={data}
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="deptCode"
          label={intl.formatMessage({ id: 'pages.admin.user.dept.deptCode' })}
          rules={[
            { required: true },
            { max: 36 },
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
            },
          ]}
        >
          <Input disabled={isUpdate}></Input>
        </Form.Item>
        <Form.Item
          name="deptName"
          label={intl.formatMessage({ id: 'pages.admin.user.dept.deptName' })}
          rules={[{ required: true }, { max: 60 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item name="pid" label={intl.formatMessage({ id: 'pages.admin.user.dept.pid' })}>
          <TreeSelect
            style={{ width: '100%' }}
            dropdownStyle={{ maxHeight: 480, overflow: 'auto' }}
            treeData={treeData}
            fieldNames={{ label: 'title', value: 'key', children: 'children' }}
            allowClear={true}
            showSearch={true}
            treeLine={{ showLeafIcon: false }}
            disabled={!isUpdate && data.pid != undefined}
            treeDefaultExpandAll={true}
          ></TreeSelect>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default DeptForm;
