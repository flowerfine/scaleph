import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { NsGraph } from '@antv/xflow';
import { Button, Col, Form, Input, Modal, Row, Select, Space } from 'antd';
import { useEffect, useState } from 'react';
import { FormattedMessage } from 'umi';

const SourceJdbcStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({ data, visible, onCancel }) => {
  const nodeInfo = data.node.data;
  const [form] = Form.useForm();
  const [dataSourceTypeList, setDataSourceTypeList] = useState<Dict[]>([]);
  const [dataSourceList, setDataSourceList] = useState<Dict[]>([]);
  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.datasourceType).then((d) => {
      setDataSourceTypeList(d);
    });
  }, []);

  return (
    <Modal
      visible={visible}
      title={nodeInfo.label}
      width={780}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {}}
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name="stepTitle"
          label={<FormattedMessage id="pages.project.di.step.stepTitle"></FormattedMessage>}
          rules={[{ required: true }, { max: 30 }]}
        >
          <Input />
        </Form.Item>
        <Row gutter={[12, 12]}>
          <Col span={12}>
            <Form.Item
              name="dataSourceType"
              label={
                <FormattedMessage id="pages.project.di.step.dataSourceType"></FormattedMessage>
              }
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
              name="dataSource"
              label={<FormattedMessage id="pages.project.di.step.dataSource"></FormattedMessage>}
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
          name="partitionColumn"
          label={<FormattedMessage id="pages.project.di.step.partitionColumn"></FormattedMessage>}
          rules={[{ required: true }]}
        >
          <Input />
        </Form.Item>
        <Row gutter={[12, 12]}>
          <Col span={19}>
            <Form.Item
              name="query"
              label={<FormattedMessage id="pages.project.di.step.query"></FormattedMessage>}
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
                <FormattedMessage id="pages.project.di.step.getsql"></FormattedMessage>
              </Button>
              <Button
                type="default"
                block
                onClick={() => {
                  alert('comming soon ...');
                }}
              >
                <FormattedMessage id="pages.project.di.step.preview"></FormattedMessage>
              </Button>
            </Space>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default SourceJdbcStepForm;
