import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  ProForm,
  ProFormDigit,
  ProFormGroup,
  ProFormList,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {Neo4jParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkNeo4jStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <Drawer
        open={visible}
        title={data.data.label}
        width={780}
        bodyStyle={{overflowY: 'scroll'}}
        destroyOnClose={true}
        onClose={onCancel}
        extra={
          <Button
            type="primary"
            onClick={() => {
              form.validateFields().then((values) => {
                StepSchemaService.formatPositionMapping(values);
                if (onOK) {
                  onOK(values);
                }
              });
            }}
          >
            {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
          </Button>
        }
      >
        <ProForm form={form} initialValues={data.data.attrs} grid={true} submitter={false}>
          <ProFormText
            name={STEP_ATTR_TYPE.stepTitle}
            label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
            rules={[{required: true}, {max: 120}]}
          />
          <DataSourceItem dataSource={'Neo4j'}/>
          <ProFormText
            name={Neo4jParams.database}
            label={intl.formatMessage({id: 'pages.project.di.step.neo4j.database'})}
            rules={[{required: true}]}
          />
          <ProFormTextArea
            name={Neo4jParams.query}
            label={intl.formatMessage({id: 'pages.project.di.step.neo4j.query'})}
            rules={[{required: true}]}
          />
          <ProFormGroup
            label={intl.formatMessage({id: 'pages.project.di.step.neo4j.queryParamPosition'})}
            tooltip={{
              title: intl.formatMessage({
                id: 'pages.project.di.step.neo4j.queryParamPosition.tooltip',
              }),
              icon: <InfoCircleOutlined/>,
            }}
          >
            <ProFormList
              name={Neo4jParams.queryParamPositionArray}
              copyIconProps={false}
              creatorButtonProps={{
                creatorButtonText: intl.formatMessage({
                  id: 'pages.project.di.step.neo4j.queryParamPosition.list',
                }),
                type: 'text',
              }}
            >
              <ProFormGroup>
                <ProFormText
                  name={Neo4jParams.field}
                  label={intl.formatMessage({id: 'pages.project.di.step.neo4j.field'})}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormDigit
                  name={Neo4jParams.position}
                  label={intl.formatMessage({id: 'pages.project.di.step.neo4j.position'})}
                  colProps={{span: 10, offset: 1}}
                  fieldProps={{
                    min: 0,
                  }}
                />
              </ProFormGroup>
            </ProFormList>
          </ProFormGroup>
          <ProFormDigit
            name={Neo4jParams.maxTransactionRetryTime}
            label={intl.formatMessage({id: 'pages.project.di.step.neo4j.maxTransactionRetryTime'})}
            initialValue={30}
            fieldProps={{
              step: 5,
              min: 0,
            }}
          />
          <ProFormDigit
            name={Neo4jParams.maxConnectionTimeout}
            label={intl.formatMessage({id: 'pages.project.di.step.neo4j.maxConnectionTimeout'})}
            initialValue={30}
            fieldProps={{
              step: 5,
              min: 0,
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkNeo4jStepForm;
