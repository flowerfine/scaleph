import { ModalFormProps } from '@/app.d';
import { CloseOutlined } from '@ant-design/icons';
import { Button, Drawer, Modal, Space, Tabs, Tooltip } from 'antd';
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
    <Modal
      title="新建作业"
      open={visible}
      onCancel={onCancel}
      onOk={onCancel}
      width={780}
      bodyStyle={{ overflowY: 'scroll', maxHeight: '640px', paddingTop: 8 }}
    >
      <Tabs
        tabPosition="top"
        items={[
          { label: '基础配置', key: 'standard', children: <>basic</> },
          { label: '高级配置', key: 'advanced', children: <>gaoji</> },
        ]}
      ></Tabs>
    </Modal>
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
