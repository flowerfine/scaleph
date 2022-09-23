import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkArtifactJarService } from '@/services/dev/flinkArtifactJar.service';
import { FlinkArtifactJarUploadParam } from '@/services/dev/typings';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Modal, Select, Upload, UploadFile, UploadProps } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const FlinkArtifactJarForm: React.FC<ModalFormProps<{ id: number }>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);
  const [flinkVersionList, setFlinkVersionList] = useState<Dict[]>([]);
  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.flinkVersion).then((d) => {
      setFlinkVersionList(d);
    });
  }, []);

  const props: UploadProps = {
    multiple: false,
    maxCount: 1,
    onRemove: (file) => {
      const index = fileList.indexOf(file);
      const newFileList = fileList.slice();
      newFileList.splice(index, 1);
      setFileList(newFileList);
    },
    beforeUpload: (file) => {
      setFileList([...fileList, file]);
      return false;
    },
    fileList,
  };

  return (
    <Modal
      visible={visible}
      title={
        intl.formatMessage({ id: 'app.common.operate.upload.label' }) +
        intl.formatMessage({ id: 'pages.dev.artifact.jar' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      confirmLoading={uploading}
      okText={
        uploading
          ? intl.formatMessage({ id: 'app.common.operate.uploading.label' })
          : intl.formatMessage({ id: 'app.common.operate.upload.label' })
      }
      onOk={() => {
        form.validateFields().then((values) => {
          const uploadParam: FlinkArtifactJarUploadParam = {
            flinkArtifactId: data.id,
            version: values.version,
            flinkVersion: values.flinkVersion,
            entryClass: values.entryClass,
            file: fileList[0],
          };
          setUploading(true);
          FlinkArtifactJarService.upload(uploadParam)
            .then((response) => {
              setFileList([]);
              if (response.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.upload.success' }));
              }
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
        <Form.Item
          name="flinkVersion"
          label={intl.formatMessage({ id: 'pages.resource.flinkRelease.version' })}
          rules={[{ required: true }]}
        >
          <Select
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {flinkVersionList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        </Form.Item>
        <Form.Item
          name="version"
          label={intl.formatMessage({ id: 'pages.dev.artifact.jar.version' })}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="entryClass"
          label={intl.formatMessage({ id: 'pages.dev.artifact.jar.entryClass' })}
          rules={[{ required: true }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label={intl.formatMessage({ id: 'pages.dev.artifact.jar.file' })}
          rules={[{ required: true }]}
        >
          <Upload {...props}>
            <Button icon={<UploadOutlined />}>
              {intl.formatMessage({ id: 'pages.dev.artifact.jar' })}
            </Button>
          </Upload>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default FlinkArtifactJarForm;
