import {ProCard, ProFormGroup, ProFormList, ProFormSelect} from "@ant-design/pro-components";
import {useIntl} from "umi";
import {ResourceService} from "@/services/resource/resource.service";
import {ResourceListParam} from "@/services/resource/typings";
import {RESOURCE_TYPE} from "@/constant";

const JarResourceOptions: React.FC = () => {
  const intl = useIntl();

  return (<div>
    <ProCard
      title={intl.formatMessage({id: 'pages.resource.jar'})}
      headerBordered
      collapsible={true}
      style={{width: 1000}}>
      <ProFormList
        name="jars"
        copyIconProps={false}
        creatorButtonProps={{
          creatorButtonText: intl.formatMessage({id: 'pages.resource.jar'}),
          type: 'text',
        }}
      >
        <ProFormGroup>
          <ProFormSelect
            name="jar"
            label={intl.formatMessage({id: 'pages.resource.jar'})}
            colProps={{span: 21, offset: 1}}
            showSearch={true}
            request={(params) => {
              const param: ResourceListParam = {
                resourceType: RESOURCE_TYPE.jar,
                name: params.keyWords
              }
              return ResourceService.list(param).then((response) => {
                return response.records.map((data) => {
                  return {value: data.id, label: data.fileName, item: data}
                })
              });
            }}
          />
        </ProFormGroup>
      </ProFormList>
    </ProCard>
  </div>);
}

export default JarResourceOptions;
