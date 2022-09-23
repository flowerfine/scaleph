import { ModalFormProps } from '@/app.d';
import { NsGraph } from '@antv/xflow';
import { Col, Form, Input, Modal, Row } from 'antd';
import { useEffect } from 'react';
import { FormattedMessage } from 'umi';

const SourceJdbcStepForm: React.FC<
    ModalFormProps<{
        node: NsGraph.INodeConfig;
        graphData: NsGraph.IGraphData;
        graphMeta: NsGraph.IGraphMeta;
    }>
> = ({ data, visible, onCancel }) => {
    const [form] = Form.useForm();
    const nodeInfo = data.node.data;
    useEffect(() => {
    }, []);

    return (
        <Modal
            visible={visible}
            title={nodeInfo.label}
            width={780}
            destroyOnClose={true}
            onCancel={onCancel}
            onOk={() => { }}
        >
            <Form
                form={form}
                layout="vertical"
            >
                <Form.Item
                    name="stepTitle"
                    label={<FormattedMessage id="pages.project.di.step.stepTitle"></FormattedMessage>}
                    rules={[{ required: true }]}
                >
                    <Input />
                </Form.Item>
                <Row gutter={[12, 12]}>
                    <Col span={12}>
                        <Form.Item name="dataSourceType"
                            label={<FormattedMessage id="pages.project.di.step.dataSourceType"></FormattedMessage>}
                            rules={[{ required: true }]}
                        >
                            <Input />
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Form.Item name="dataSource"
                            label={<FormattedMessage id="pages.project.di.step.dataSource"></FormattedMessage>}
                            rules={[{ required: true }]}
                        >
                            <Input />
                        </Form.Item>
                    </Col>
                </Row>


                <Form.Item name="partitionColumn"
                    label={<FormattedMessage id="pages.project.di.step.partitionColumn"></FormattedMessage>}
                    rules={[{ required: true }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item name="query"
                    label={<FormattedMessage id="pages.project.di.step.query"></FormattedMessage>}
                    rules={[{ required: true }]}
                >
                    <Input />
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default SourceJdbcStepForm;
