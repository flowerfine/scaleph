import { Dict } from '@/app.d';
import { DICT_TYPE, RESOURCE_TYPE } from '@/constant';
import Additional from '@/pages/DEV/ClusterConfigOptions/components/Additional';
import FaultTolerance from '@/pages/DEV/ClusterConfigOptions/components/FaultTolerance';
import HighAvailability from '@/pages/DEV/ClusterConfigOptions/components/HA';
import Resource from '@/pages/DEV/ClusterConfigOptions/components/Resource';
import State from '@/pages/DEV/ClusterConfigOptions/components/State';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkClusterConfigService } from '@/services/dev/flinkClusterConfig.service';
import { FlinkClusterConfig } from '@/services/dev/typings';
import { ResourceService } from '@/services/resource/resource.service';
import { ResourceListParam } from '@/services/resource/typings';
import {
  FooterToolbar,
  ProCard,
  ProForm, ProFormInstance,
  ProFormSelect,
  ProFormText, StepsForm,
} from '@ant-design/pro-components';
import { Col, Form, message, Row } from 'antd';
import { history, useIntl, useLocation } from 'umi';
import {FlinkJob} from "@/pages/DEV/Job/typings";
import {FlinkJobService} from "@/pages/DEV/Job/FlinkJobService";
import JobBase from "@/pages/DEV/JobConfig/components/JobBase";
import JobJar from "@/pages/DEV/JobConfig/components/JobJar";
import JobClusterConfigOptions from "@/pages/DEV/JobConfig/components/ClusterConfigOptions";
import {useRef} from "react";
import KubernetesStep from "@/pages/DEV/ClusterConfigOptions/Kubernetes/components/KubernetesStep";

const ClusterConfigOptionsSteps: React.FC = () => {
  const urlParams = useLocation();
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const params = urlParams.state as FlinkClusterConfig;

  return (
    <div>
      <ProCard className={'step-form-submitter'}>
        <StepsForm
          formRef={formRef}
          formProps={{
            grid: true,
            rowProps: { gutter: [16, 8] }
          }}
          onFinish={async (values) => {

          }}>
          <KubernetesStep/>
        </StepsForm>
      </ProCard>
    </div>
  );
};

export default ClusterConfigOptionsSteps;
