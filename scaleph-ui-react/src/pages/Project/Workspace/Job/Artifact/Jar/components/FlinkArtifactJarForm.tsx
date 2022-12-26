import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkArtifactJarService } from '@/services/project/flinkArtifactJar.service';
import { WsFlinkArtifactJar } from '@/services/project/typings';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Modal, Select, Upload, UploadFile, UploadProps } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const FlinkArtifactJarForm: React.FC<ModalFormProps<WsFlinkArtifactJar>> = ({
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
    accept: '.jar',
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
      open={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.project.artifact.jar' })
          : intl.formatMessage({ id: 'app.common.operate.upload.label' }) +
            intl.formatMessage({ id: 'pages.project.artifact.jar' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      confirmLoading={uploading}
      okText={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.confirm.label' })
          : uploading
          ? intl.formatMessage({ id: 'app.common.operate.uploading.label' })
          : intl.formatMessage({ id: 'app.common.operate.upload.label' })
      }
      onOk={() => {
        data.id
          ? form.validateFields().then((values) => {
              const params: WsFlinkArtifactJar = {
                id: values.id,
                wsFlinkArtifact: { id: data.wsFlinkArtifact?.id as number },
                version: values.version,
                flinkVersion: values.flinkVersion,
                entryClass: values.entryClass,
                jarParams: values.jarParams,
              };
              FlinkArtifactJarService.update(params).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange ? onVisibleChange(false) : null;
                }
              });
            })
          : form.validateFields().then((values) => {
              const uploadParam: WsFlinkArtifactJar = {
                wsFlinkArtifact: { id: data.wsFlinkArtifact?.id as number },
                version: values.version,
                flinkVersion: values.flinkVersion,
                entryClass: values.entryClass,
                file: fileList[0],
                jarParams: values.jarParams,
              };
              setUploading(true);
              FlinkArtifactJarService.upload(uploadParam)
                .then((response) => {
                  setFileList([]);
                  if (response.success) {
                    message.success(
                      intl.formatMessage({ id: 'app.common.operate.upload.success' }),
                    );
                  }
                })
                .catch(() => {
                  message.error(intl.formatMessage({ id: 'app.common.operate.upload.failure' }));
                })
                .finally(() => {
                  setUploading(false);
                  onVisibleChange ? onVisibleChange(false) : null;
                });
            });
      }}
    >
      <Form
        form={form}
        layout="horizontal"
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 16 }}
        initialValues={{
          id: data.id,
          version: data.version,
          flinkVersion: data.flinkVersion?.value,
          entryClass: data.entryClass,
          jarParams: data.jarParams,
        }}
      >
        <Form.Item hidden name="id">
          <Input></Input>
        </Form.Item>
        {!data.id && (
          <Form.Item
            name="file"
            label={intl.formatMessage({ id: 'pages.project.artifact.jar.file' })}
            rules={[{ required: true }]}
          >
            <Upload {...props}>
              <Button icon={<UploadOutlined />}>
                {intl.formatMessage({ id: 'pages.project.artifact.jar' })}
              </Button>
            </Upload>
          </Form.Item>
        )}
        <Form.Item
          name="version"
          rules={[{ required: true }]}
          label={intl.formatMessage({ id: 'pages.project.artifact.jar.version' })}
        >
          <Input />
        </Form.Item>
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
          name="entryClass"
          label={intl.formatMessage({ id: 'pages.project.artifact.jar.entryClass' })}
          rules={[{ required: true }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="jarParams"
          label={intl.formatMessage({ id: 'pages.project.artifact.jar.jarParams' })}
        >
          <Input.TextArea
            rows={3}
            placeholder={intl.formatMessage({
              id: 'pages.project.artifact.jar.jarParams.placeholder',
            })}
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default FlinkArtifactJarForm;
