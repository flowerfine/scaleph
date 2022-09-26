import {ProForm} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {Form} from "antd";
import State from "@/pages/DEV/ClusterConfigOptions/components/State";
import FaultTolerance from "@/pages/DEV/ClusterConfigOptions/components/FaultTolerance";
import HighAvailability from "@/pages/DEV/ClusterConfigOptions/components/HA";
import Resource from "@/pages/DEV/ClusterConfigOptions/components/Resource";
import Additional from "@/pages/DEV/ClusterConfigOptions/components/Additional";
import {FlinkClusterConfig, FlinkClusterInstance} from "@/services/dev/typings";
import {FlinkClusterConfigService} from "@/services/dev/flinkClusterConfig.service";

const JobConfigurationWeb: React.FC<{
  clusterConfig: FlinkClusterConfig,
  clusterInstance: FlinkClusterInstance,
  flinkConfig?: { [key: string]: any }
}> = ({clusterConfig, clusterInstance, flinkConfig}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  const configOptions = FlinkClusterConfigService.setData(
    new Map(Object.entries(flinkConfig ? flinkConfig : {})),
  );

  return (<ProForm
    form={form}
    layout={'horizontal'}
    initialValues={configOptions}
    grid={true}
    rowProps={{gutter: [16, 8]}}
    submitter={false}>
    <State/>
    <FaultTolerance/>
    <HighAvailability/>
    <Resource/>
    <Additional/>
  </ProForm>);
}

export default JobConfigurationWeb;
