import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {history, useIntl} from "umi";
import {useRef, useState} from "react";
import {Button} from "antd";
import DataSourceCategoryAndTypeWeb from "@/pages/DataSource/StepForms/CategoryAndType";
import MySQLForm from "@/pages/DataSource/StepForms/Props/MySQL";

const DataSourceStepForms: React.FC = () => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const [dsTypeId, setDsTypeId] = useState<number>(0)

  const onTypeSelect = (dsTypeId: number) => {
    setDsTypeId(dsTypeId)
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
          console.log('values', values)
          return Promise.resolve(true);
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
          <MySQLForm dsTypeId={dsTypeId}/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  );
}

export default DataSourceStepForms;
