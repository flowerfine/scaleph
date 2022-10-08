import {ProCard, ProFormGroup, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";

const FlinkImageOptions: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard title={'Flink Image'} headerBordered collapsible={true}>
      <ProFormGroup>
        <ProFormText
          name="registry"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.registry'}))}
          colProps={{span: 10, offset: 1}}/>
        <ProFormText
          name="repository"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.repository'}))}
          colProps={{span: 10, offset: 1}}/>
        <ProFormText
          name="image"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.image'}))}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}/>
      </ProFormGroup>
    </ProCard>
  );
}

export default FlinkImageOptions;
