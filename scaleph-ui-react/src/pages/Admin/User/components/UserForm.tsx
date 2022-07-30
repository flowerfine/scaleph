import { SecUser } from "@/services/admin/typings";
import { ModalFormProps } from "@/app.d";
import { Form, Input, message, Modal } from "antd";
import { useIntl } from "umi";
import { addUser, updateUser } from "@/services/admin/user.service";

const UserForm: React.FC<ModalFormProps<SecUser>> = ({
    data,
    visible,
    onVisibleChange,
    onCancel,
}) => {
    const intl = useIntl();
    const [form] = Form.useForm();
    return (
        <Modal
            visible={visible}
            title={
                data.id ?
                    intl.formatMessage({ id: 'app.common.operate.edit.label' }) + intl.formatMessage({ id: 'pages.admin.user' }) :
                    intl.formatMessage({ id: 'app.common.operate.new.label' }) + intl.formatMessage({ id: 'pages.admin.user' })
            }
            width={580}
            destroyOnClose={true}
            onCancel={onCancel}
            onOk={() => {
                form.validateFields().then((values) => {
                    data.id ?
                        updateUser({ ...values }).then(d => {
                            if (d.success) {
                                message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                                onVisibleChange(false);
                            }
                        }) :
                        addUser({ ...values }).then(d => {
                            if (d.success) {
                                message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                                onVisibleChange(false);
                            }
                        })
                });
            }}
        >
            <Form
                form={form}
                layout="horizontal"
                labelCol={{ span: 6 }}
                wrapperCol={{ span: 16 }}
                initialValues={data}
            >
                <Form.Item name="id" hidden>
                    <Input></Input>
                </Form.Item>
                {/* <Form.Item
                    name="dictTypeCode"
                    label={intl.formatMessage({ id: 'pages.admin.dict.dictTypeCode' })}
                    rules={[
                        { required: true },
                        { max: 30 },
                        {
                            pattern: /^[a-zA-Z0-9_]+$/,
                            message: intl.formatMessage({ id: 'app.common.validate.characterWord' })
                        }]}
                >
                    <Input disabled={data.id ? true : false}></Input>
                </Form.Item>
                <Form.Item
                    name="dictTypeName"
                    label={intl.formatMessage({ id: 'pages.admin.dict.dictTypeName' })}
                    rules={[{ required: true }, { max: 100 }]}>
                    <Input></Input>
                </Form.Item>
                <Form.Item
                    name="remark"
                    label={intl.formatMessage({ id: 'pages.admin.dict.remark' })}
                    rules={[{ max: 200 }]}>
                    <Input></Input>
                </Form.Item> */}
            </Form>
        </Modal>
    );
}

export default UserForm;