import { useIntl } from 'umi';
import { Button, Card, Col, Divider, Form, Input, Row, Space } from "antd";
import { PageContainer, ProFormDatePicker, ProFormDateRangePicker, ProFormSelect, ProFormText, ProTable, QueryFilter } from '@ant-design/pro-components';

const Dict: React.FC = () => {
    const intl = useIntl();
    const [form] = Form.useForm();
    return (<div>
        <Row gutter={[12, 12]} >
            <Col span={12}>
                <ProTable
                    headerTitle="字典类型"
                    search={{ filterType: "light" }}
                    options={false}
                    toolbar={{
                        search: {
                            onSearch: (value) => {
                                alert(value);
                            },
                        },
                        actions: [
                            <Button key="list" type="primary">
                                新建
                            </Button>,
                            <Button key="list" type="default">
                                删除
                            </Button>,
                        ],
                    }}
                ></ProTable>
            </Col>
            <Col span={12}>
                <ProTable
                    headerTitle="数据字典"
                    search={{ filterType: "light" }}
                    options={false}
                    toolbar={{
                        // search: {
                        //     onSearch: (value) => {
                        //         alert(value);
                        //     },
                        // },
                        actions: [
                            <Button key="list" type="primary">
                                新建
                            </Button>,
                            <Button key="list" type="default">
                                删除
                            </Button>,
                        ],
                    }}

                ></ProTable>
            </Col>
        </Row>
    </div>
    );
}

export default Dict;
