import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {JdbcParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";
import {DsInfoParam} from "@/services/datasource/typings";
import {DsInfoService} from "@/services/datasource/info.service";

const SourceJdbcStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatSchema(values);
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
          <ProFormSelect
            name={"dataSourceType"}
            label={intl.formatMessage({id: 'pages.project.di.step.dataSourceType'})}
            colProps={{span: 6}}
            initialValue={"MySQL"}
            allowClear={false}
            request={() => DictDataService.listDictDataByType2(DICT_TYPE.datasourceType)}
          />
          <ProFormSelect
            name={STEP_ATTR_TYPE.dataSource}
            label={intl.formatMessage({id: 'pages.project.di.step.dataSource'})}
            rules={[{required: true}]}
            colProps={{span: 18}}
            dependencies={["dataSourceType"]}
            request={((params, props) => {
              const param: DsInfoParam = {
                name: params.keyWords,
                dsType: params.dataSourceType
              };
              return DsInfoService.list(param).then((response) => {
                return response.data.map((item) => {
                  return {label: item.name, value: item.id, item: item};
                });
              });
            })}
          />
          <ProFormDigit
            name={JdbcParams.connectionCheckTimeoutSec}
            label={intl.formatMessage({id: 'pages.project.di.step.jdbc.connectionCheckTimeoutSec'})}
            fieldProps={{
              min: 0
            }}
          />
          <ProFormText
            name={JdbcParams.compatibleMode}
            label={intl.formatMessage({id: 'pages.project.di.step.jdbc.compatibleMode'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.jdbc.compatibleMode.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
          />
          <ProFormText
            name={JdbcParams.partitionColumn}
            label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionColumn'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionColumn.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            colProps={{span: 6}}
          />
          <ProFormDigit
            name={JdbcParams.partitionLowerBound}
            label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionLowerBound'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionLowerBound.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            colProps={{span: 6}}
          />
          <ProFormDigit
            name={JdbcParams.partitionUpperBound}
            label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionUpperBound'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionUpperBound.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            colProps={{span: 6}}
          />
          <ProFormDigit
            name={JdbcParams.partitionNum}
            label={intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionNum'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.jdbc.partitionNum.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            colProps={{span: 6}}
          />
          <ProFormDigit
            name={JdbcParams.fetchSize}
            label={intl.formatMessage({id: 'pages.project.di.step.jdbc.fetchSize'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.jdbc.fetchSize.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            initialValue={0}
          />

          <ProFormTextArea
            name={JdbcParams.query}
            label={intl.formatMessage({id: 'pages.project.di.step.jdbc.query'})}
            rules={[{required: true}]}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceJdbcStepForm;
