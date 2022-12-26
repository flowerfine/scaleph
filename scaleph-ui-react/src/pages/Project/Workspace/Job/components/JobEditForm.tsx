import { ModalFormProps } from '@/app.d';
import { FlinkClusterConfigService } from '@/services/project/flinkClusterConfig.service';
import { FlinkJobInstanceService } from '@/services/project/FlinkJobInstanceService';
import { FlinkJobService } from '@/services/project/FlinkJobService';
import { WsFlinkJob } from '@/services/project/typings';
import { Form, Modal } from 'antd';
import { useIntl } from 'umi';
import Additional from '../../Cluster/Config/Options/components/Additional';
import FaultTolerance from '../../Cluster/Config/Options/components/FaultTolerance';
import HighAvailability from '../../Cluster/Config/Options/components/HA';
import Resource from '../../Cluster/Config/Options/components/Resource';
import State from '../../Cluster/Config/Options/components/State';

const JobEditForm: React.FC<ModalFormProps<WsFlinkJob>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  return (
    <Modal
      title={intl.formatMessage({ id: 'pages.project.job.edit' })}
      width={780}
      bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
      onCancel={onCancel}
      open={visible}
      destroyOnClose
    >
      <Form
        layout="vertical"
        initialValues={data.wsFlinkClusterConfig}
        onFinish={async (values) => {
          let job: WsFlinkJob = {
            ...data,
            flinkConfig: FlinkClusterConfigService.getData(values),
          };
          FlinkJobService.update(job);
        }}
      >
        <State />
        <FaultTolerance />
        <HighAvailability />
        <Resource />
        <Additional />
      </Form>
    </Modal>
  );
};

export default JobEditForm;
