import { DirectoryService } from '@/services/project/directory.service';
import { DiDirectory } from '@/services/project/typings';
import { Form, Input, message, Modal } from 'antd';
import { useIntl } from 'umi';

interface DirectoryFormProps<SecDept> {
  data: SecDept;
  visible: boolean;
  isUpdate: boolean;
  onVisibleChange: (visible: boolean) => void;
  onCancel: () => void;
}

const DirectoryForm: React.FC<DirectoryFormProps<DiDirectory>> = ({
  data,
  visible,
  isUpdate,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  return (
    <Modal
      visible={visible}
      title={
        isUpdate
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.project.dir' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.project.dir' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let d: DiDirectory = {
            id: values.id,
            directoryName: values.directoryName,
            projectId: data.projectId,
            pid: data.pid,
          };
          isUpdate
            ? DirectoryService.updateDir({ ...d }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : DirectoryService.addDir({ ...d }).then((d) => {
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
          name="directoryName"
          label={intl.formatMessage({ id: 'pages.project.dir.directoryName' })}
          rules={[{ required: true }, { max: 30 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default DirectoryForm;
