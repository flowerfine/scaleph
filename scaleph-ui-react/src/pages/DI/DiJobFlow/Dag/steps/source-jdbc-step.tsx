import { ModalFormProps } from "@/app.d";
import { Form, Input, Modal } from "antd"
import { useEffect } from "react";

const SourceJdbcStepForm: React.FC<ModalFormProps<any>> = ({
    data,
    visible,
    onVisibleChange,
    onCancel
}) => {
    const [form] = Form.useForm();

    useEffect(() => {
        console.log('23452345');
    }, []);
    return (
        <Modal
            visible={visible}
            title="test modal"
            width={580}
            destroyOnClose={true}
            onCancel={onCancel}
        >
            <Form
                form={form}
                layout="horizontal"
            >
                <Form.Item
                    name="name"
                    label="name"
                    rules={[{ required: true }]}
                >
                    <Input />
                </Form.Item>
            </Form>
        </Modal>

    )
}

export default SourceJdbcStepForm;