import { ModalFormProps } from "@/app.d";
import { addDataSource, testConnection, updateDataSource } from "@/services/project/dataSource.service";
import { MetaDataSource } from "@/services/project/typings";
import { Button, Form, Input, InputNumber, message, Modal } from "antd";
import { useState } from "react";
import { useIntl } from "umi";

const DorisDataSourceForm: React.FC<ModalFormProps<MetaDataSource>> = ({
    data,
    visible,
    onVisibleChange,
    onCancel
}) => {
    const intl = useIntl();
    const [form] = Form.useForm();
    const [isPasswordChange, setPasswordChange] = useState<boolean>(false);

    const clearPassword = () => {
        form.setFieldValue("password", '');
        setPasswordChange(true);
    }

    return (
        <Modal
            visible={visible}
            title={
                data.id
                    ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
                    intl.formatMessage({ id: 'pages.project.di.dataSource' })
                    : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
                    intl.formatMessage({ id: 'pages.project.di.dataSource' })
            }
            width={580}
            destroyOnClose={true}
            onCancel={onCancel}
            footer={[
                <Button
                    key="test"
                    type="primary"
                    danger={true}
                    onClick={() => {
                        form.validateFields().then((values) => {
                            let ds: MetaDataSource = {
                                id: values.id,
                                datasourceName: values.datasourceName,
                                datasourceType: data.datasourceType,
                                props: {
                                    host: values.host,
                                    port: values.port,
                                    httpPort: values.httpPort,
                                    databaseName: values.databaseName,
                                    username: values.username,
                                    password: values.password,
                                },
                                remark: values.remark,
                                additionalPropsStr: values.additionalPropsStr,
                                passwdChanged: data.id ? isPasswordChange : true
                            };
                            testConnection(ds).then(resp => {
                                if (resp.success) {
                                    message.success(intl.formatMessage({ id: 'pages.project.di.dataSource.testConnect.success' }))
                                }
                            });
                        });
                    }}
                >
                    {intl.formatMessage({ id: 'pages.project.di.dataSource.testConnect' })}
                </Button>,
                <Button
                    key="cancel"
                    onClick={onCancel}>
                    {intl.formatMessage({ id: 'app.common.operate.cancel.label' })}
                </Button>,
                <Button
                    key="confirm"
                    type="primary"
                    onClick={() => {
                        form.validateFields().then((values) => {
                            let ds: MetaDataSource = {
                                id: values.id,
                                datasourceName: values.datasourceName,
                                datasourceType: data.datasourceType,
                                props: {
                                    host: values.host,
                                    port: values.port,
                                    httpPort: values.httpPort,
                                    databaseName: values.databaseName,
                                    username: values.username,
                                    password: values.password,
                                },
                                remark: values.remark,
                                additionalPropsStr: values.additionalPropsStr,
                            };
                            data.id
                                ? updateDataSource({ ...ds }).then((d) => {
                                    if (d.success) {
                                        message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                                        onVisibleChange(false);
                                    }
                                })
                                : addDataSource({ ...ds }).then((d) => {
                                    if (d.success) {
                                        message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                                        onVisibleChange(false);
                                    }
                                });
                        });
                    }}>
                    {intl.formatMessage({ id: 'app.common.operate.confirm.label' })}
                </Button>,
            ]}
        >
            <Form
                form={form}
                layout="horizontal"
                labelCol={{ span: 6 }}
                wrapperCol={{ span: 16 }}
                initialValues={{
                    id: data.id,
                    datasourceName: data.datasourceName,
                    host: data.props?.host,
                    port: data.props?.port,
                    httpPort: data.props?.httpPort,
                    databaseName: data.props?.databaseName,
                    username: data.props?.username,
                    password: data.props?.password,
                    remark: data.remark,
                    additionalPropsStr: data.additionalPropsStr,
                }}
            >
                <Form.Item name="id" hidden>
                    <Input></Input>
                </Form.Item>
                <Form.Item
                    name="datasourceName"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.dataSourceName' })}
                    rules={[
                        { required: true },
                        { max: 60 },
                        {
                            pattern: /^[a-zA-Z0-9_]+$/,
                            message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
                        },
                    ]}
                >
                    <Input disabled={data.id ? true : false}></Input>
                </Form.Item>
                <Form.Item
                    name="host"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.host' })}
                    rules={[
                        { required: true },
                        { max: 256 }
                    ]}
                >
                    <Input onChange={clearPassword}></Input>
                </Form.Item>
                <Form.Item
                    name="databaseName"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.databaseName' })}
                    rules={[
                        { required: true },
                        { max: 64 }
                    ]}
                >
                    <Input onChange={clearPassword}></Input>
                </Form.Item>
                <Form.Item
                    name="port"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.port' })}
                    rules={[
                        { required: true },
                    ]}
                >
                    <InputNumber style={{ width: '100%' }} onChange={clearPassword}></InputNumber>
                </Form.Item>
                <Form.Item
                    name="httpPort"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.httpPort' })}
                    rules={[
                        { required: true },
                    ]}
                >
                    <InputNumber style={{ width: '100%' }} onChange={clearPassword}></InputNumber>
                </Form.Item>
                <Form.Item
                    name="username"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.username' })}
                    rules={[
                        { required: true },
                        { max: 120 }
                    ]}
                >
                    <Input onChange={clearPassword}></Input>
                </Form.Item>
                <Form.Item
                    name="password"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.password' })}
                    rules={[
                        { required: true },
                        { max: 120 }
                    ]}
                >
                    <Input.Password></Input.Password>
                </Form.Item>
                <Form.Item
                    name="remark"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.remark' })}
                    rules={[
                        { max: 200 },
                    ]}
                >
                    <Input></Input>
                </Form.Item>
                <Form.Item
                    name="additionalPropsStr"
                    label={intl.formatMessage({ id: 'pages.project.di.dataSource.additionalProps' })}
                    rules={[
                        { max: 2048 },
                    ]}
                >
                    <Input.TextArea
                        autoSize={{ minRows: 5 }}
                        placeholder={intl.formatMessage({ id: 'pages.project.di.dataSource.additionalProps.placeholder' })}
                    >
                    </Input.TextArea>
                </Form.Item>
            </Form>
        </Modal>
    );
}

export default DorisDataSourceForm;