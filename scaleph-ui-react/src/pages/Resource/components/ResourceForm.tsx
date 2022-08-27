import { Dict, ModalFormProps } from "@/app.d";
import { listAllProject } from "@/services/project/project.service";
import { DiResourceFile } from "@/services/resource/typings";
import { InboxOutlined } from "@ant-design/icons";
import { Form, Input, Modal, Select, Upload } from "antd";
import { useEffect, useState } from "react";
import { useIntl } from "umi";

const ResourceForm: React.FC<ModalFormProps<DiResourceFile>> = ({
    data,
    visible,
    onVisibleChange,
    onCancel
}) => {
    const intl = useIntl();
    const [form] = Form.useForm();
    const [projectList, setProjectList] = useState<Dict[]>([]);

    useEffect(() => {
        listAllProject().then((d) => {
            setProjectList(d);
        });
    }, []);

    const normFile = (e: any) => {
        console.log('Upload event:', e);
        if (Array.isArray(e)) {
            return e;
        }
        return e?.fileList;
    };

    return (
        <Modal
            visible={visible}
            title={
                data.id
                    ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
                    intl.formatMessage({ id: 'pages.resource' })
                    : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
                    intl.formatMessage({ id: 'pages.resource' })
            }
            width={580}
            destroyOnClose={true}
            onCancel={onCancel}
            onOk={() => {
                form.validateFields().then((values) => {
                    let d: DiResourceFile = {
                        id: values.id,
                        // clusterName: values.clusterName,
                        // clusterType: { value: values.clusterType },
                        // clusterHome: values.clusterHome,
                        // clusterVersion: values.clusterVersion,
                        // clusterConf: values.clusterConf,
                        // remark: values.remark,
                    };
                    // data.id
                    //     ? updateCluster({ ...d }).then((d) => {
                    //         if (d.success) {
                    //             message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                    //             onVisibleChange(false);
                    //         }
                    //     })
                    //     : addCluster({ ...d }).then((d) => {
                    //         if (d.success) {
                    //             message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                    //             onVisibleChange(false);
                    //         }
                    //     });
                });
            }}
        >
            <Form
                form={form}
                layout="horizontal"
                labelCol={{ span: 6 }}
                wrapperCol={{ span: 16 }}
                initialValues={{
                    // id: data.id,
                    // clusterName: data.clusterName,
                    // clusterType: data.clusterType?.value,
                    // clusterHome: data.clusterHome,
                    // clusterConf: data.clusterConf,
                    // clusterVersion: data.clusterVersion,
                    // remark: data.remark,
                }}
            >
                <Form.Item name="id" hidden>
                    <Input></Input>
                </Form.Item>
                <Form.Item
                    name="projectId"
                    label={intl.formatMessage({ id: 'pages.resource.projectCode' })}
                    rules={[
                        { required: true },
                        { max: 128 },
                    ]}
                >
                    <Select
                        disabled={data.id ? true : false}
                        showSearch={true}
                        allowClear={true}
                        optionFilterProp="label"
                        filterOption={(input, option) =>
                            (option!.children as unknown as string)
                                .toLowerCase()
                                .includes(input.toLowerCase())
                        }
                    >
                        {projectList.map((item) => {
                            return (
                                <Select.Option key={item.value} value={item.value}>
                                    {item.label}
                                </Select.Option>
                            );
                        })}
                    </Select>
                </Form.Item>
                <Form.Item label="Dragger">
                    <Form.Item name="dragger" valuePropName="fileList" getValueFromEvent={normFile} noStyle>
                        <Upload.Dragger name="files" action="/upload.do">
                            <p className="ant-upload-drag-icon">
                                <InboxOutlined />
                            </p>
                            <p className="ant-upload-text">Click or drag file to this area to upload</p>
                            <p className="ant-upload-hint">Support for a single or bulk upload.</p>
                        </Upload.Dragger>
                    </Form.Item>
                </Form.Item>
            </Form>
        </Modal>
    );
}

export default ResourceForm;