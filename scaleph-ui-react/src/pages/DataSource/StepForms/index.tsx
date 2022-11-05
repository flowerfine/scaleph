import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {history, useIntl, useModel} from "umi";
import {useRef} from "react";
import {Button} from "antd";
import DataSourceCategoryAndTypeWeb from "@/pages/DataSource/StepForms/CategoryAndType";
import DataSourceForm from "@/pages/DataSource/StepForms/Props/DataSourceForm";
import {DsInfoService} from "@/services/datasource/info.service";

const DataSourceStepForms: React.FC = () => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const {dsType} = useModel('dataSourceType', (model) => ({
    dsType: model.dsType
  }));

  const onTypeSelect = () => {
    formRef.current?.submit()
  }

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}
        submitter={{
          render: (props, dom) => {
            if (props.step == 0) {
              return (
                <Button type="default" onClick={() => history.back()}>
                  {intl.formatMessage({id: 'app.common.operate.cancel.label'})}
                </Button>
              );
            }
            return dom;
          }
        }}
        onFinish={(values) => {
          const dsInfo = {...values, type: dsType?.type.value}
          return DsInfoService.add(dsInfo).then((response) => {
            if (response.success) {
              history.back()
            }
          })
        }}
      >
        <StepsForm.StepForm
          name="type"
          title={(intl.formatMessage({id: 'pages.dataSource.step.type'}))}
          layout={'horizontal'}
          style={{width: 1500}}>
          <DataSourceCategoryAndTypeWeb onTypeSelect={onTypeSelect}/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="props"
          title={(intl.formatMessage({id: 'pages.dataSource.step.props'}))}
          labelCol={{span: 3}}
          wrapperCol={{span: 21}}
          layout={'horizontal'}
          style={{width: 1000}}>
          <DataSourceForm/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
}

export default DataSourceStepForms;
