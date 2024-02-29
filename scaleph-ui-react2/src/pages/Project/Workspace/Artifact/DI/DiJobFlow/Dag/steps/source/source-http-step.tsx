import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {HttpParams, STEP_ATTR_TYPE} from '../../constant';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormSelect, ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {useEffect} from 'react';
import {StepSchemaService} from '../helper';
import DataSourceItem from '@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/dataSource';
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/steps/fields";

const SourceHttpFileStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as WsDiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
  }, []);

  return (
    <Drawer
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll'}}
      destroyOnClose={true}
      onClose={onCancel}
      extra={
        <Button
          type="primary"
          onClick={() => {
            form.validateFields().then((values) => {
              let map: Map<string, any> = new Map();
              map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
              map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
              map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
              StepSchemaService.formatSchema(values);
              StepSchemaService.formatHeader(values);
              StepSchemaService.formatParam(values);
              StepSchemaService.formatJsonField(values);
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              WsDiJobService.saveStepAttr(map).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.success'}));
                  onOK ? onOK(values) : null;
                }
              });
            });
          }}
        >
          {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
        </Button>
      }
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
        />
        <DataSourceItem dataSource={'Http'}/>
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
      </ProForm>
    </Drawer>
  );
};

export default SourceHttpFileStepForm;
