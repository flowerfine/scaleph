import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { DataSourceService } from '@/services/project/dataSource.service';
import { InfoCircleOutlined } from '@ant-design/icons';
import { NsGraph } from '@antv/xflow';
import { Button, Col, Form, Input, Modal, Row, Select, Space } from 'antd';
import { useEffect, useState } from 'react';
import { getIntl, getLocale } from 'umi';

const SourceJdbcStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({ data, visible, onCancel }) => {
  const nodeInfo = data.node.data;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();
  const [dataSourceTypeList, setDataSourceTypeList] = useState<Dict[]>([]);
  const [dataSourceList, setDataSourceList] = useState<Dict[]>([]);
  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.datasourceType).then((d) => {
      setDataSourceTypeList(d);
    });
  }, []);

  const handleDataSourceTypeChange = (value: string) => {
    DataSourceService.listDataSourceByType(value).then((d) => {
      setDataSourceList(d);
      form.setFieldValue('dataSource', null);
    });
  };

  return (
    <Modal
      visible={visible}
      title={nodeInfo.label}
      width={780}
      bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        console.log('local', getLocale());

        console.log(intl.formatMessage({ id: 'pages.project.di.step.partitionColumn.tooltip' }));
        form.validateFields().then((values) => {
          console.log(values);
        });
      }}
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name="stepTitle"
          label={intl.formatMessage({ id: 'pages.project.di.step.stepTitle' })}
          rules={[{ required: true }, { max: 120 }]}
        >
          <Input />
        </Form.Item>
        <Row gutter={[12, 12]}>
          <Col span={12}>
            <Form.Item
              name="dataSourceType"
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
                onChange={handleDataSourceTypeChange}
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
          name="partitionColumn"
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
              name="query"
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
