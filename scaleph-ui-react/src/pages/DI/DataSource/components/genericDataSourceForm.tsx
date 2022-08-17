import { ModalFormProps } from "@/app.d";
import { MetaDataSource } from "@/services/di/typings";
import { Form, Modal } from "antd";
import { useIntl } from "umi";

const GenericDataSourceForm: React.FC<ModalFormProps<MetaDataSource>> = ({
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
            onCancel={onCancel}        >
            123123
        </Modal>
    );
}

export default GenericDataSourceForm;