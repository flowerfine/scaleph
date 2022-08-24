import { ModalFormProps } from '@/app.d';
import { DiProject } from '@/services/di/typings';
import { addProject, updateProject } from '@/services/studio/project.service';
import { Form, Input, message, Modal } from 'antd';
import { useIntl } from 'umi';

const ProjectForm: React.FC<ModalFormProps<DiProject>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  return (
    <Modal
      visible={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.studio.project' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.studio.project' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let d: DiProject = {
            id: values.id,
            projectCode: values.projectCode,
            projectName: values.projectName,
            remark: values.remark,
          };
          data.id
            ? updateProject({ ...d }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : addProject({ ...d }).then((d) => {
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
          name="projectCode"
          label={intl.formatMessage({ id: 'pages.studio.project.projectCode' })}
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
          name="projectName"
          label={intl.formatMessage({ id: 'pages.studio.project.projectName' })}
          rules={[{ required: true }, { max: 60 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.studio.project.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default ProjectForm;
