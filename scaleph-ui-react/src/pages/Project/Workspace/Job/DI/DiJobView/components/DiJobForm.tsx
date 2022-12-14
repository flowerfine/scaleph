import { WsDiJobService } from '@/services/project/WsDiJob.service';
import { WsDiJob } from '@/services/project/typings';
import { Form, Input, Modal } from 'antd';
import { useIntl } from 'umi';

interface DiJobFormProps<DiJob> {
  data: DiJob;
  visible: boolean;
  onVisibleChange: (visible: boolean, data: any) => void;
  onCancel: () => void;
}

const DiJobForm: React.FC<DiJobFormProps<WsDiJob>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  const submit = () => {
    form.validateFields().then((values) => {
      const job: WsDiJob = {
        projectId: data.projectId,
        jobName: values.jobName,
        jobType: data.jobType?.value,
        remark: values.remark,
      };
      data.id
        ? WsDiJobService.updateJob({ ...job, id: data.id }).then((response) => {
            if (response.success) {
              onVisibleChange(false, response.data);
            }
          })
        : WsDiJobService.addJob(job).then((response) => {
            if (response.success) {
              onVisibleChange(false, response.data);
            }
          });
    });
  };

  return (
    <Modal
      open={visible}
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
      onOk={submit}
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
          jobType: data.jobType?.value,
          remark: data.remark,
        }}
      >
        <Form.Item name="id" hidden>
          <Input />
        </Form.Item>
        <Form.Item
          name="jobName"
          label={intl.formatMessage({ id: 'pages.project.di.jobName' })}
          rules={[{ required: true }, { max: 200 }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.project.di.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default DiJobForm;
