import React from "react";
import {ProFormGroup, ProFormRadio, ProFormSwitch} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";

const FlinkKubernetesJobDeployStateStepForm: React.FC = () => {
  const intl = useIntl();

  return (
    <ProFormGroup>
      <ProFormRadio.Group
        name={"upgradeMode"}
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.state.upgradeMode'})}
        rules={[{required: true}]}
        request={() => {
          return DictDataService.listDictDataByType2(DICT_TYPE.upgradeMode)
        }}
      />
      <ProFormSwitch
        name={"allowNonRestoredState"}
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.deploy.state.allowNonRestoredState'})}
        rules={[{required: true}]}
        initialValue={false}
      />

    </ProFormGroup>
  );
}

export default FlinkKubernetesJobDeployStateStepForm;
