import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { DataSourceService } from '@/services/project/dataSource.service';
import { JobService } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import { NsGraph } from '@antv/xflow';
import { Button, Col, Form, Input, InputNumber, message, Modal, Row, Select, Space } from 'antd';
import { useEffect, useState } from 'react';
import { getIntl, getLocale } from 'umi';
import { STEP_ATTR_TYPE } from '../../constant';

const SinkJdbcStepForm: React.FC<
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
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.label);
    DictDataService.listDictDataByType(DICT_TYPE.datasourceType).then((d) => {
      setDataSourceTypeList(d);
    });
    if (nodeInfo.data.attrs) {
      let type = nodeInfo.data.attrs[STEP_ATTR_TYPE.dataSourceType];
      type ? refreshDataSource(type) : null;
    }
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
          map.set(STEP_ATTR_TYPE.stepAttrs, form.getFieldsValue());
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
      <Form form={form} layout="vertical" initialValues={nodeInfo.data.attrs}>
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
          name={STEP_ATTR_TYPE.batchSize}
          label={intl.formatMessage({ id: 'pages.project.di.step.batchSize' })}
        >
          <InputNumber defaultValue={1000} step={100} style={{ width: '100%' }} />
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
            <Space direction="vertical" style={{ paddingTop: '28px', width: '100%' }}>
              <Button
                type="default"
                block
                onClick={() => {
                  alert('comming soon ...');
                }}
              >
                {intl.formatMessage({ id: 'pages.project.di.step.getsql' })}
              </Button>
            </Space>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default SinkJdbcStepForm;
