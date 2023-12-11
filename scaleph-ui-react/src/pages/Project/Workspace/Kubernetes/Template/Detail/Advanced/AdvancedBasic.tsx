import {ProCard, ProFormGroup, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {DICT_TYPE} from "@/constants/dictType";
import {DictDataService} from "@/services/admin/dictData.service";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";

const AdvancedBasic: React.FC = () => {
  return (<ProCard
    title={'Basic'}
    headerBordered
    collapsible={true}>
    <ProFormGroup>
      <ProFormSelect
        name="flinkVersion"
        label={"flinkVersion"}
        colProps={{span: 10, offset: 1}}
        rules={[{required: true}]}
        showSearch={true}
        request={() => {
          return WsFlinkKubernetesTemplateService.getFlinkVersionOptions().then(response => {
            if (response.success) {
              return response.data
            }
          })
        }}
      />
      <ProFormText
        name="serviceAccount"
        label={'serviceAccount'}
        colProps={{span: 10, offset: 1}}
        initialValue={"flink"}
      />
      <ProFormSelect
        name="image"
        label={'image'}
        colProps={{span: 10, offset: 1}}
        rules={[{required: true}]}
        dependencies={['flinkVersion']}
        request={(params) => {
          if (params.flinkVersion) {
            return WsFlinkKubernetesTemplateService.getFlinkImageOptions(params.flinkVersion).then(response => {
              if (response.success) {
                return response.data
              }
            })
          }
        }}
      />
      <ProFormSelect
        name="imagePullPolicy"
        label={"imagePullPolicy"}
        colProps={{span: 10, offset: 1}}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.imagePullPolicy)}
        initialValue={"IfNotPresent"}
      />
    </ProFormGroup>
  </ProCard>);
}

export default AdvancedBasic;
