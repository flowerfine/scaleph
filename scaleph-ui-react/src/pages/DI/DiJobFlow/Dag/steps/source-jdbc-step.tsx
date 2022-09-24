import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { DataSourceService } from '@/services/project/dataSource.service';
import { JobService } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import { InfoCircleOutlined } from '@ant-design/icons';
import { NsGraph } from '@antv/xflow';
import { Button, Col, Form, Input, message, Modal, Row, Select, Space } from 'antd';
import { useEffect, useState } from 'react';
import { getIntl, getLocale } from 'umi';
import { STEP_ATTR_TYPE } from '../constant';

const SourceJdbcStepForm: React.FC<
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
  const [dataSourceTypeList, setDataSourceTypeList] = useState<Dict[]>([]);
  const [dataSourceList, setDataSourceList] = useState<Dict[]>([]);
  useEffect(() => {
    console.log(nodeInfo)
    DictDataService.listDictDataByType(DICT_TYPE.datasourceType).then((d) => {
      setDataSourceTypeList(d);
    });
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.label);
    JobService.listStepAttr(jobInfo.id + '', nodeInfo.id).then((resp) => {
      let stepAttrMap: Map<string, string> = new Map();
      resp.map((step) => {
        stepAttrMap.set(step.stepAttrKey, step.stepAttrValue);
      });
      refreshDataSource(stepAttrMap.get(STEP_ATTR_TYPE.dataSourceType) as string);
      form.setFieldValue(STEP_ATTR_TYPE.query, stepAttrMap.get(STEP_ATTR_TYPE.query));
      form.setFieldValue(STEP_ATTR_TYPE.dataSourceType, stepAttrMap.get(STEP_ATTR_TYPE.dataSourceType));
      form.setFieldValue(STEP_ATTR_TYPE.dataSource, stepAttrMap.get(STEP_ATTR_TYPE.dataSource));
      form.setFieldValue(STEP_ATTR_TYPE.partitionColumn, stepAttrMap.get(STEP_ATTR_TYPE.partitionColumn));
    });
  }, []);

  const refreshDataSource = (value: string) => {
    DataSourceService.listDataSourceByType(value).then((d) => {
      setDataSourceList(d);
    });
  };

  return (
    <Modal
      visible={visible}
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
          map.set(STEP_ATTR_TYPE.dataSourceType, values[STEP_ATTR_TYPE.dataSourceType]);
          map.set(STEP_ATTR_TYPE.dataSource, values[STEP_ATTR_TYPE.dataSource]);
          map.set(STEP_ATTR_TYPE.query, values[STEP_ATTR_TYPE.query]);
          map.set(STEP_ATTR_TYPE.partitionColumn, values[STEP_ATTR_TYPE.partitionColumn]);
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
      <Form form={form} layout="vertical" >
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
              name={STEP_ATTR_TYPE.dataSourceType}
              label={intl.formatMessage({ id: 'pages.project.di.step.dataSourceType' })}
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
                onChange={(value) => {
                  refreshDataSource(value);
                  form.setFieldValue(STEP_ATTR_TYPE.dataSource, null);
                }}
              >
                {dataSourceTypeList.map((item) => {
                  return (
                    <Select.Option key={item.value} value={item.value}>
                      {item.label}
                    </Select.Option>
                  );
                })}
              </Select>
            </Form.Item>
          </Col>
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
          name={STEP_ATTR_TYPE.partitionColumn}
          label={intl.formatMessage({ id: 'pages.project.di.step.partitionColumn' })}
          tooltip={{
            title: intl.formatMessage({ id: 'pages.project.di.step.partitionColumn.tooltip' }),
            icon: <InfoCircleOutlined />,
          }}
        >
          <Input />
        </Form.Item>
        <Row gutter={[12, 12]}>
          <Col span={19}>
            <Form.Item
              name={STEP_ATTR_TYPE.query}
              label={intl.formatMessage({ id: 'pages.project.di.step.query' })}
              rules={[{ required: true }]}
            >
              <Input.TextArea rows={8}></Input.TextArea>
            </Form.Item>
          </Col>
          <Col span={5}>
            <Space direction="vertical" style={{ paddingTop: '28px' }}>
              <Button
                type="default"
                block
                onClick={() => {
                  alert('comming soon ...');
                }}
              >
                {intl.formatMessage({ id: 'pages.project.di.step.getsql' })}
              </Button>
              <Button
                type="default"
                block
                onClick={() => {
                  alert('comming soon ...');
                }}
              >
                {intl.formatMessage({ id: 'pages.project.di.step.preview' })}
              </Button>
            </Space>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default SourceJdbcStepForm;
