import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {HttpParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SourceHttpStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <DrawerForm
        title={data.data.label}
        form={form}
        initialValues={data.data.attrs}
        open={visible}
        onOpenChange={onVisibleChange}
        grid={true}
        width={780}
        drawerProps={{
          styles: {body: {overflowY: 'scroll'}},
          closeIcon: null,
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
            StepSchemaService.formatSchema(values);
            StepSchemaService.formatHeader(values);
            StepSchemaService.formatParam(values);
            StepSchemaService.formatJsonField(values);
            StepSchemaService.formatPaging(values);
            onOK(values)
            return Promise.resolve(true)
          }
          return Promise.resolve(false)
        }}
      >
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
        />
        <DataSourceItem dataSource={'Http'}/>
        <ProFormDigit
          name={HttpParams.socketTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.http.socketTimeoutMs'})}
          colProps={{span: 12}}
          initialValue={60000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={HttpParams.connectTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.http.connectTimeoutMs'})}
          colProps={{span: 12}}
          initialValue={12000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormList
          name={HttpParams.headerArray}
          label={intl.formatMessage({id: 'pages.project.di.step.http.headers'})}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.http.headers'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText
              name={HttpParams.header}
              label={intl.formatMessage({id: 'pages.project.di.step.http.header'})}
              colProps={{span: 10, offset: 1}}
            />
            <ProFormText
              name={HttpParams.headerValue}
              label={intl.formatMessage({id: 'pages.project.di.step.http.value'})}
              colProps={{span: 10, offset: 1}}
            />
          </ProFormGroup>
        </ProFormList>
        <ProFormList
          name={HttpParams.paramArray}
          label={intl.formatMessage({id: 'pages.project.di.step.http.params'})}
          copyIconProps={false}
          creatorButtonProps={{
            creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.http.params'}),
            type: 'text',
          }}
        >
          <ProFormGroup>
            <ProFormText
              name={HttpParams.param}
              label={intl.formatMessage({id: 'pages.project.di.step.http.param'})}
              colProps={{span: 10, offset: 1}}
            />
            <ProFormText
              name={HttpParams.paramValue}
              label={intl.formatMessage({id: 'pages.project.di.step.http.value'})}
              colProps={{span: 10, offset: 1}}
            />
          </ProFormGroup>
        </ProFormList>
        <ProFormTextArea
          name={HttpParams.body}
          label={intl.formatMessage({id: 'pages.project.di.step.http.body'})}
        />
        <ProFormText
          name={HttpParams.pagingPageField}
          label={intl.formatMessage({id: 'pages.project.di.step.http.pagingPageField'})}
          colProps={{span: 8}}
        />
        <ProFormDigit
          name={HttpParams.pagingTotalPageSize}
          label={intl.formatMessage({id: 'pages.project.di.step.http.pagingTotalPageSize'})}
          colProps={{span: 8}}
          fieldProps={{
            min: 1
          }}
        />
        <ProFormDigit
          name={HttpParams.pagingBatchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.http.pagingBatchSize'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.http.pagingBatchSize.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 8}}
          fieldProps={{
            min: 1
          }}
        />
        <ProFormSelect
          name={'format'}
          label={intl.formatMessage({id: 'pages.project.di.step.http.format'})}
          rules={[{required: true}]}
          allowClear={false}
          initialValue={'json'}
          valueEnum={{
            json: 'json',
            text: 'text',
          }}
        />
        <ProFormDependency name={['format']}>
          {({format}) => {
            if (format == 'json') {
              return <FieldItem/>;
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSwitch
          name={HttpParams.enableMultiLines}
          label={intl.formatMessage({id: 'pages.project.di.step.http.enableMultiLines'})}
          initialValue={false}
        />
        <ProFormText
          name={HttpParams.contentField}
          label={intl.formatMessage({id: 'pages.project.di.step.http.contentField'})}
          placeholder={intl.formatMessage({
            id: 'pages.project.di.step.http.contentField.placeholder',
          })}
        />
        <ProFormGroup label={intl.formatMessage({id: 'pages.project.di.step.http.fieldJson'})}>
          <ProFormList
            name={HttpParams.jsonField}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.http.fieldJson.list',
              }),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={HttpParams.key}
                label={intl.formatMessage({id: 'pages.project.di.step.http.fieldJson.key'})}
                placeholder={intl.formatMessage({
                  id: 'pages.project.di.step.http.fieldJson.key.placeholder',
                })}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={HttpParams.path}
                label={intl.formatMessage({id: 'pages.project.di.step.http.fieldJson.path'})}
                placeholder={intl.formatMessage({
                  id: 'pages.project.di.step.http.fieldJson.path.placeholder',
                })}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>
        <ProFormDigit
          name={HttpParams.pollIntervalMs}
          label={intl.formatMessage({id: 'pages.project.di.step.http.pollIntervalMs'})}
          colProps={{span: 24}}
        />
        <ProFormDigit
          name={HttpParams.retry}
          label={intl.formatMessage({id: 'pages.project.di.step.http.retry'})}
          colProps={{span: 6}}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormDigit
          name={HttpParams.retryBackoffMultiplierMs}
          label={intl.formatMessage({id: 'pages.project.di.step.http.retryBackoffMultiplierMs'})}
          colProps={{span: 9}}
          initialValue={100}
          fieldProps={{
            step: 1000,
            min: 0
          }}
        />
        <ProFormDigit
          name={HttpParams.retryBackoffMaxMs}
          label={intl.formatMessage({id: 'pages.project.di.step.http.retryBackoffMaxMs'})}
          colProps={{span: 9}}
          initialValue={10000}
          fieldProps={{
            step: 1000,
            min: 0
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceHttpStepForm;
