import {ProCard, ProFormGroup, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

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
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}]}/>
        <ProFormSelect
          name="imagePullPolicy"
          label={(intl.formatMessage({id: 'pages.dev.clusterConfig.imagePullPolicy'}))}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}]}
          showSearch={false}
          allowClear={false}
          initialValue={"IfNotPresent"}
          request={() => DictDataService.listDictDataByType(DICT_TYPE.imagePullPolicy)}
        />
      </ProFormGroup>
    </ProCard>
  );
}

export default FlinkImageOptions;
