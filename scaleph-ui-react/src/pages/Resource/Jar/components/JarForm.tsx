import {ModalFormProps} from '@/app.d';
import {Jar, JarUploadParam} from '@/services/resource/typings';
import {Button, Form, Input, message, Modal, Upload, UploadFile, UploadProps} from 'antd';
import {useIntl} from 'umi';
import {useState} from "react";
import {UploadOutlined} from "@ant-design/icons";
import {upload} from "@/services/resource/jar.service";

const JarForm: React.FC<ModalFormProps<Jar>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);

  const props: UploadProps = {
    multiple: false,
    maxCount: 1,
    onRemove: file => {
      const index = fileList.indexOf(file);
      const newFileList = fileList.slice();
      newFileList.splice(index, 1);
      setFileList(newFileList);
    },
    beforeUpload: file => {
      setFileList([...fileList, file]);
      return false;
    },
    fileList,
  };

  return (
    <Modal
      visible={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.resource.jar' })
          : intl.formatMessage({ id: 'app.common.operate.upload.label' }) +
            intl.formatMessage({ id: 'pages.resource.jar' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      confirmLoading={uploading}
      okText={uploading ? intl.formatMessage({ id: 'app.common.operate.uploading.label' }) : intl.formatMessage({ id: 'app.common.operate.upload.label' })}
      onOk={() => {
        form.validateFields().then((values) => {
          const uploadParam: JarUploadParam  ={
            group: values.group,
            file: fileList[0],
            remark: values.remark
          };
          setUploading(true);
          upload(uploadParam)
            .then(() => {
              setFileList([]);
              message.success(intl.formatMessage({ id: 'app.common.operate.upload.success' }));
            })
            .catch(() => {
              message.error(intl.formatMessage({ id: 'app.common.operate.upload.failure' }));
            })
            .finally(() => {
              setUploading(false);
              onVisibleChange(false);
            });
        });
      }}
    >
      <Form form={form} layout="horizontal" labelCol={{ span: 6 }} wrapperCol={{ span: 16 }}>
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="group"
          label={intl.formatMessage({ id: 'pages.resource.jar.group' })}
          rules={[{ required: true }, { max: 128 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          label={intl.formatMessage({ id: 'pages.resource.file' })}
          rules={[{ required: true }]}
        >
          <Upload {...props}>
            <Button icon={<UploadOutlined />}>{intl.formatMessage({ id: 'pages.resource.jar.file' })}</Button>
          </Upload>
        </Form.Item>
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.resource.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default JarForm;
