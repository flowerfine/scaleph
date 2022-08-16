import { ModalFormProps } from "@/app.d";
import { addDataSource, updateDataSource } from "@/services/di/dataSource.service";
import { MetaDataSource } from "@/services/di/typings";
import { Form, Input, message, Modal } from "antd";
import { useIntl } from "umi";

const JdbcDataSourceForm: React.FC<ModalFormProps<MetaDataSource>> = ({
    data,
    visible,
    onVisibleChange,
    onCancel
}) => {
    const intl = useIntl();
    const [form] = Form.useForm();
    return (
        <Modal
            visible={visible}
            title={
                data.id
                    ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
                    intl.formatMessage({ id: 'pages.di.dataSource' })
                    : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
                    intl.formatMessage({ id: 'pages.di.dataSource' })
            }
            width={580}
            destroyOnClose={true}
            onCancel={onCancel}
            onOk={() => {
                form.validateFields().then((values) => {
                    let ds: MetaDataSource = {
                        id: values.id,
                        // roleCode: values.roleCode,
                        // roleName: values.roleName,
                        // roleStatus: { value: values.roleStatus },
                        // roleDesc: values.roleDesc,
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
            }}
        >
            <Form
                form={form}
                layout="horizontal"
                labelCol={{ span: 6 }}
                wrapperCol={{ span: 16 }}
                initialValues={{}}
            >
                <Form.Item name="id" hidden>
                    <Input></Input>
                </Form.Item>
                <Form.Item
                    name="datasourceName"
                    label={intl.formatMessage({ id: 'pages.di.dataSource.dataSourceName' })}
                    rules={[
                        { required: true },
                        { max: 60 },
                        {
                            pattern: /^[a-zA-Z0-9_]+$/,
                            message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
                        },
                    ]}
                >
                    <Input></Input>
                </Form.Item>
            </Form>
        </Modal>
    );
}

export default JdbcDataSourceForm;