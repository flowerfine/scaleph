import {useIntl} from "umi";
import {ProCard, ProFormSelect} from "@ant-design/pro-components";
import {ResourceListParam} from "@/services/resource/typings";
import {ResourceService} from "@/services/resource/resource.service";

const AdvancedAdditionalDependencies: React.FC = () => {
  const intl = useIntl();

  return (<ProCard
    title={"Additional Dependencies"}
    headerBordered
    collapsible={true}
    defaultCollapsed>
    <ProFormSelect
      name="additionalDependencies"
      colProps={{span: 10, offset: 1}}
      fieldProps={{
        mode: "multiple"
      }}
      request={(params) => {
        const param: ResourceListParam = {
          resourceType: 'jar',
          name: params.keyWords,
        };

        return ResourceService.list(param).then((response) => {
          if (response.records) {
            return response.records.map((item) => {
              return {label: item.path, value: item.id, item: item}
            })
          }
          return Promise.any()
        })
      }}
    />
  </ProCard>);
}

export default AdvancedAdditionalDependencies;
