import { Dict, ModalFormProps } from '@/app.d';
import { DataSourceService } from '@/services/project/dataSource.service';
import { JobService } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import { NsGraph } from '@antv/xflow';
import {Col, Form, Input, InputNumber, message, Modal, Row, Select} from 'antd';
import { useEffect, useState } from 'react';
import { getIntl, getLocale } from 'umi';
import {STEP_ATTR_TYPE, ClickHouseParams, BaseFileParams} from '../constant';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
} from "@ant-design/pro-components";
/**
 * query
 * batch_size
 */
const SinkClickHouseStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({ data, visible, onCancel, onOK }) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as DiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();
  const [dataSourceList, setDataSourceList] = useState<Dict[]>([]);

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.label);
    JobService.listStepAttr(jobInfo.id + '', nodeInfo.id).then((resp) => {
      let stepAttrMap: Map<string, string> = new Map();
      resp.map((step) => {
        stepAttrMap.set(step.stepAttrKey, step.stepAttrValue);
      });
      refreshDataSource(ClickHouseParams.dataSourceType as string);
      form.setFieldValue(STEP_ATTR_TYPE.dataSource, stepAttrMap.get(STEP_ATTR_TYPE.dataSource));
      form.setFieldValue(STEP_ATTR_TYPE.table, stepAttrMap.get(STEP_ATTR_TYPE.table));
      form.setFieldValue(STEP_ATTR_TYPE.fields, stepAttrMap.get(STEP_ATTR_TYPE.fields));
      form.setFieldValue(STEP_ATTR_TYPE.bulkSize, stepAttrMap.get(STEP_ATTR_TYPE.bulkSize));
      form.setFieldValue(ClickHouseParams.splitMode, stepAttrMap.get(ClickHouseParams.splitMode));
      form.setFieldValue(ClickHouseParams.shardingKey, stepAttrMap.get(ClickHouseParams.shardingKey));
      form.setFieldValue(ClickHouseParams.clickhouseConf, stepAttrMap.get(ClickHouseParams.clickhouseConf));

    });
  }, []);

  const refreshDataSource = (value: string) => {
    DataSourceService.listDataSourceByType(value).then((d) => {
      setDataSourceList(d);
    });
  };

  return (
    <Modal
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let map: Map<string, string> = new Map();
          map.set(STEP_ATTR_TYPE.jobId, jobInfo.id + '');
          map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
          map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
          map.set(STEP_ATTR_TYPE.stepTitle, values[STEP_ATTR_TYPE.stepTitle]);
          map.set(STEP_ATTR_TYPE.dataSource, values[STEP_ATTR_TYPE.dataSource]);
          map.set(STEP_ATTR_TYPE.bulkSize, values[STEP_ATTR_TYPE.bulkSize]);
          map.set(STEP_ATTR_TYPE.table, values[STEP_ATTR_TYPE.table]);
          map.set(STEP_ATTR_TYPE.fields, values[STEP_ATTR_TYPE.fields]);
          map.set(ClickHouseParams.splitMode, values[ClickHouseParams.splitMode]);
          map.set(ClickHouseParams.shardingKey, values[ClickHouseParams.shardingKey]);
          map.set(ClickHouseParams.clickhouseConf, values[ClickHouseParams.clickhouseConf]);
          JobService.saveStepAttr(map).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
              onCancel();
              onOK ? onOK() : null;
            }
          });
        });
      }}
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({ id: 'pages.project.di.step.stepTitle' })}
          rules={[{ required: true }, { max: 120 }]}
        >
          <Input />
        </Form.Item>

        <Row gutter={[12, 12]}>
          <Col span={12}>
            <Form.Item
              name={STEP_ATTR_TYPE.dataSource}
              label={intl.formatMessage({ id: 'pages.project.di.step.dataSource' })}
              rules={[{ required: true }]}
            >
              <Select
                showSearch={true}
                allowClear={false}
                optionFilterProp="label"
                filterOption={(input, option) =>
                  (option!.children as unknown as string)
                  .toLowerCase()
                  .includes(input.toLowerCase())
                }
              >
                {dataSourceList.map((item) => {
                  return (
                    <Select.Option key={item.value} value={item.value}>
                      {item.label}
                    </Select.Option>
                  );
                })}
              </Select>
            </Form.Item>
          </Col>
        </Row>

        <Form.Item
          name={STEP_ATTR_TYPE.table}
          label={intl.formatMessage({ id: 'pages.project.di.step.clickhosue.table' })}
          rules={[{ required: true }]}
        >
          <Input/>
        </Form.Item>

        <Form.Item
          name={STEP_ATTR_TYPE.fields}
          label={intl.formatMessage({ id: 'pages.project.di.step.clickhosue.fields' })}
          tooltip={{
            title: intl.formatMessage({ id: 'pages.project.di.step.clickhosue.fields.tooltip' }),
            icon: <InfoCircleOutlined />,
          }}
        >
          <Input/>
        </Form.Item>


        <Form.Item
          name={STEP_ATTR_TYPE.bulkSize}
          label={intl.formatMessage({ id: 'pages.project.di.step.bulkSize' })}
        >
          <InputNumber defaultValue={20000} step={100} style={{ width: '100%' }} />
        </Form.Item>

        <ProFormSelect
          name={"split_mode"}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.splitMode'})}
          tooltip={{
            title: intl.formatMessage({ id: 'pages.project.di.step.clickhosue.splitMode.tooltip' }),
            icon: <InfoCircleOutlined />,
          }}
          valueEnum={{
            true: "true",
            false: "false"
          }}
        />
        <ProFormDependency name={["split_mode"]}>
          {({split_mode}) => {
            if (split_mode == "true") {
              return (

                <Form.Item
                  name={ClickHouseParams.shardingKey}
                  label={intl.formatMessage({ id: 'pages.project.di.step.clickhosue.shardingKey' })}
                  rules={[{ required: true }]}
                >
                  <Input/>
                </Form.Item>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <Form.Item
          name={ClickHouseParams.clickhouseConf}
          label={intl.formatMessage({ id: 'pages.project.di.step.clickhosue.clickhouseConf' })}
          tooltip={{
            title: intl.formatMessage({ id: 'pages.project.di.step.clickhosue.clickhouseConf.tooltip' }),
            icon: <InfoCircleOutlined />,
          }}
        >
          <Input.TextArea rows={8}></Input.TextArea>
        </Form.Item>

      </Form>
    </Modal>
  );
};

export default SinkClickHouseStepForm;
