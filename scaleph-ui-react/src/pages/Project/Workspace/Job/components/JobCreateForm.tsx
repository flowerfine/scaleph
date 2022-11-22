import { ModalFormProps } from '@/app.d';
import { CloseOutlined } from '@ant-design/icons';
import { StepsForm } from '@ant-design/pro-components';
import { Button, Drawer, Form, Input, Modal, Radio, Space, Tabs, Tooltip } from 'antd';
import { useAccess, useIntl } from 'umi';

const JobCreateForm: React.FC<ModalFormProps<any>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const access = useAccess();
  return (
    <StepsForm
      onFinish={async (values) => {
        console.log(values);
      }}
      stepsProps={{ size: 'small', labelPlacement: 'vertical' }}
      stepsFormRender={(dom, submitter) => {
        return (
          <Modal
            title="创建作业"
            width={780}
            bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
            onCancel={onCancel}
            open={visible}
            destroyOnClose
            footer={submitter}
          >
            {dom}
          </Modal>
        );
      }}
    >
      <StepsForm.StepForm
        name="step1"
        title="选择作业"
        layout="horizontal"
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 20 }}
      >
        <Form.Item name="jobType" label="作业类型">
          <Radio.Group onChange={() => {}}>
            <Radio value="0">SeaTunnel</Radio>
            <Radio value="1">Jar</Radio>
            <Radio value="2">Sql</Radio>
          </Radio.Group>
        </Form.Item>
        <Form.Item name="job" label="作业">
          <Input></Input>
        </Form.Item>
      </StepsForm.StepForm>
      <StepsForm.StepForm
        name="step2"
        title="集群&资源"
        layout="horizontal"
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 20 }}
      >
        <Form.Item name="jars" label="依赖资源">
          <Input></Input>
        </Form.Item>
        <Form.Item name="cluster" label="集群">
          <Input></Input>
        </Form.Item>
      </StepsForm.StepForm>
      <StepsForm.StepForm
        name="step3"
        title="参数设置"
        layout="horizontal"
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 20 }}
      >
        <Form.Item name="jobType" label="任务类型">
          <Input></Input>
        </Form.Item>
      </StepsForm.StepForm>
    </StepsForm>
    // <Modal
    //   title="新建作业"
    //   open={visible}
    //   onCancel={onCancel}
    //   onOk={onCancel}
    //   width={780}
    //   bodyStyle={{ overflowY: 'scroll', maxHeight: '640px', paddingTop: 8 }}
    // >
    //   <Tabs
    //     tabPosition="top"
    //     items={[
    //       { label: '基础配置', key: 'standard', children: <>basic</> },
    //       { label: '高级配置', key: 'advanced', children: <>gaoji</> },
    //     ]}
    //   ></Tabs>
    // </Modal>
    // <Drawer
    // //   title="新建作业"
    //   placement="right"
    //   width={'50%'}
    //   bodyStyle={{ paddingTop: 6 }}
    //   onClose={onCancel}
    //   open={visible}
    //   closable={false}
    //   extra={
    //     <Space>
    //       <Tooltip title={intl.formatMessage({ id: 'app.common.operate.close.label' })}></Tooltip>
    //       <Button shape="default" type="text" icon={<CloseOutlined />} onClick={onCancel}></Button>
    //     </Space>
    //   }
    //   footer={
    //     <div style={{ textAlign: 'right' }}>
    //       <Space>
    //         <Button onClick={onCancel}>关闭</Button>
    //         <Button type="primary" onClick={onCancel}>
    //           确认
    //         </Button>
    //       </Space>
    //     </div>
    //   }
    // >
    //   <Tabs
    //     tabPosition="top"
    //     items={[
    //       { label: '基础配置', key: 'standard', children: <>basic</> },
    //       { label: '高级配置', key: 'advanced', children: <>gaoji</> },
    //     ]}
    //   ></Tabs>
    // </Drawer>
  );
};
export default JobCreateForm;
