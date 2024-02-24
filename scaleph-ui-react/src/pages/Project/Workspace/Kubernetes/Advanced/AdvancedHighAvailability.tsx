import React from "react";
import {ProCard, ProFormDependency, ProFormGroup, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";

const AdvancedHighAvailability: React.FC = () => {
  return (<ProCard
    title={"High Availability"}
    headerBordered
    collapsible={true}
    defaultCollapsed>
    <ProFormGroup>
      <ProFormSelect
        name="ha"
        label={"high-availability"}
        colProps={{span: 10, offset: 1}}
        showSearch={true}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkHA)}
      />
      <ProFormText
        name="high-availability.storageDir"
        label={'high-availability.storageDir'}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormText
        name="high-availability.cluster-id"
        label={'high-availability.cluster-id'}
        colProps={{span: 10, offset: 1}}
      />

      <ProFormDependency name={['ha']}>
        {({ha}) => {
          if (ha == 'zookeeper') {
            return (<ProFormGroup>
              <ProFormText
                name="high-availability.zookeeper.path.root"
                label={'high-availability.zookeeper.path.root'}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name="high-availability.zookeeper.quorum"
                label={'high-availability.zookeeper.quorum'}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>)
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
    </ProFormGroup>
  </ProCard>);
}

export default AdvancedHighAvailability;
