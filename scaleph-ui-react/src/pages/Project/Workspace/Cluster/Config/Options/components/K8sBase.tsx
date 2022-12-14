import {useIntl} from "umi";
import {ProCard, ProFormGroup, ProFormText} from "@ant-design/pro-components";

const K8sBaseOptions: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard title={'Kubernetes Base'} headerBordered collapsible={true}>
      <ProFormGroup>
        <ProFormText
          name="namespace"
          label={(intl.formatMessage({id: 'page.project.cluster.config.namespace'}))}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          initialValue={"default"}
        />
        <ProFormText
          name="serviceAccount"
          label={(intl.formatMessage({id: 'page.project.cluster.config.serviceAccount'}))}
          colProps={{span: 21, offset: 1}}
          initialValue={"flink"}
          disabled
        />
        <ProFormText
          name="context"
          label={(intl.formatMessage({id: 'page.project.cluster.config.context'}))}
          colProps={{span: 21, offset: 1}}/>
      </ProFormGroup>
    </ProCard>
  );
}

export default K8sBaseOptions;
