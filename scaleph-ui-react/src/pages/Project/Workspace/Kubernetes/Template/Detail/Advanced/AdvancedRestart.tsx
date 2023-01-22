import {ProCard, ProFormDigit, ProFormGroup, ProFormSwitch, ProFormText} from "@ant-design/pro-components";

const AdvancedRestart: React.FC = () => {
  return (<ProCard
    title={'Restart'}
    headerBordered
    collapsible={true}
    defaultCollapsed>
    <ProFormGroup>
      <ProFormSwitch
        name="kubernetes.operator.cluster.health-check.enabled"
        label={'kubernetes.operator.cluster.health-check.enabled'}
        colProps={{span: 10, offset: 1}}
        help={"Whether to enable health check for clusters."}
      />
      <ProFormText
        name="kubernetes.operator.cluster.health-check.restarts.window"
        label={'kubernetes.operator.cluster.health-check.restarts.window'}
        colProps={{span: 10, offset: 1}}
        help={"The duration of the time window where job restart count measured."}
      />
      <ProFormDigit
        name="kubernetes.operator.cluster.health-check.restarts.threshold"
        label={'kubernetes.operator.cluster.health-check.restarts.threshold'}
        colProps={{span: 10, offset: 1}}
        initialValue={64}
        help={"The threshold which is checked against job restart count within a configured window. If the restart count is reaching the threshold then full cluster restart is initiated."}
      />
    </ProFormGroup>
  </ProCard>);
}

export default AdvancedRestart;
