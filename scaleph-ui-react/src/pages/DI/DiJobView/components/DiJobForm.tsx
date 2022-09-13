import { TreeNode } from '@/app.d';
import { addJob, updateJob } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import { Form, Input, message, Modal, TreeSelect } from 'antd';
import { useIntl } from 'umi';

interface DiJobFormProps<DiJob> {
  data: DiJob;
  visible: boolean;
  treeData: TreeNode[];
  onVisibleChange: (visible: boolean, data: any) => void;
  onCancel: () => void;
}

const DiJobForm: React.FC<DiJobFormProps<DiJob>> = ({
  data,
  visible,
  treeData,
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
          intl.formatMessage({ id: 'pages.project.di.job' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
          intl.formatMessage({ id: 'pages.project.di.job' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let d: DiJob = {
            id: values.id,
            projectId: data.projectId,
            jobCode: values.jobCode,
            jobName: values.jobName,
            directory: { id: values.directory },
            jobType: data.jobType,
            remark: values.remark,
          };
          data.id
            ? updateJob({ ...d }).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                onVisibleChange(false, null);
              }
            })
            : addJob({ ...d }).then((d) => {
              if (d.success) {
                // message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                onVisibleChange(false, d.data);
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
          projectId: data.projectId,
          jobCode: data.jobCode,
          jobName: data.jobName,
          directory: data.directory?.id,
          jobType: data.jobType?.value,
          remark: data.remark,
        }}
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="jobCode"
          label={intl.formatMessage({ id: 'pages.project.di.jobCode' })}
          rules={[
            { required: true },
            { max: 120 },
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
            },
          ]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="jobName"
          label={intl.formatMessage({ id: 'pages.project.di.jobName' })}
          rules={[{ required: true }, { max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="directory"
          label={intl.formatMessage({ id: 'pages.project.di.directory' })}
          rules={[{ required: true }]}
        >
          <TreeSelect
            style={{ width: '100%' }}
            dropdownStyle={{ maxHeight: 480, overflow: 'auto' }}
            treeData={treeData}
            fieldNames={{ label: 'title', value: 'key', children: 'children' }}
            showSearch={true}
            treeLine={{ showLeafIcon: false }}
            treeDefaultExpandAll={true}
          ></TreeSelect>
        </Form.Item>
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.project.di.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default DiJobForm;
