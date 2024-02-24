import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";
import {WORKSPACE_CONF} from "@/constants/constant";
import {DataserviceConfig, DataserviceConfigSaveParam} from "@/services/dataservice/typings";
import DataserviceConfigBaseStep from "@/pages/Project/Workspace/DataService/Config/Steps/BaseStepForm";
import DataserviceConfigConfigStep from "@/pages/Project/Workspace/DataService/Config/Steps/ConfigStepForm";
import {DataserviceConfigService} from "@/services/dataservice/DataserviceConfigService";
import {history} from "@@/core/history";

const DataserviceConfigSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const localProjectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onBaseStepFinish = (values: Record<string, any>) => {
    const config: DataserviceConfigSaveParam = {
      id: values.id,
      projectId: localProjectId,
      name: values.name,
      path: values.path,
      method: values.method,
      contentType: values.contentType,
      remark: values.remark,
    }
    editDataserviceConfig(config)
    return Promise.resolve(true)
  }

  const onConfigStepFinish = (values: Record<string, any>) => {
    const config: DataserviceConfigSaveParam = {
      ...props.dataserviceConfigSteps.config,
      parameterMappings: values.parameterMappings,
      resultMappings: values.resultMappings,
      type: values.type,
      sql: values.sql
    }
    editDataserviceConfig(config)
    return Promise.resolve(true)
  }

  const onAllFinish = (values: Record<string, any>) => {
    const config: DataserviceConfigSaveParam = {
      ...values,
      projectId: localProjectId
    }
    if (config.id) {
      return DataserviceConfigService.update(config).then((response) => {
        if (response.success) {
          history.back()
        }
      })
    } else {
      return DataserviceConfigService.add(config).then((response) => {
        if (response.success) {
          history.back()
        }
      })
    }
  }

  const editDataserviceConfig = (config: DataserviceConfig) => {
    props.dispatch({
      type: 'dataserviceConfigSteps/editConfig',
      payload: config
    })
  }

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}
        onFinish={onAllFinish}
      >
        <StepsForm.StepForm
          name="base"
          title={intl.formatMessage({id: 'pages.project.dataservice.config.steps.base'})}
          style={{width: 1000}}
          onFinish={onBaseStepFinish}>
          <DataserviceConfigBaseStep/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="config"
          title={intl.formatMessage({id: 'pages.project.dataservice.config.steps.config'})}
          style={{width: 1000}}
          onFinish={onConfigStepFinish}>
          <DataserviceConfigConfigStep/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  )
}

const mapModelToProps = ({dataserviceConfigSteps}: any) => ({dataserviceConfigSteps})
export default connect(mapModelToProps)(DataserviceConfigSteps);
