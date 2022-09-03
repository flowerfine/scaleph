import {Dict, ModalFormProps} from '@/app.d';
import {USER_AUTH} from '@/constant';
import {listAllProject} from '@/services/project/project.service';
import {addResourceFile} from '@/services/resource/resource.service';
import {DiResourceFile} from '@/services/resource/typings';
import {InboxOutlined} from '@ant-design/icons';
import {Form, Input, message, Modal, Select, Spin, Upload} from 'antd';
import {RcFile, UploadChangeParam, UploadFile} from 'antd/lib/upload';
import {useEffect, useState} from 'react';
import {request, useIntl} from 'umi';

const ResourceForm: React.FC<ModalFormProps<DiResourceFile>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [projectList, setProjectList] = useState<Dict[]>([]);
  const [projectId, setProjectId] = useState<number>();
  const [fileName, setFileName] = useState<string>('');
  const [isUploading, setIsUploading] = useState<boolean>(false);
  const [isDone, setIsDone] = useState<boolean>(false);
  useEffect(() => {
    listAllProject().then((d) => {
      setProjectList(d);
    });
  }, []);

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
      okButtonProps={{ disabled: !isDone }}
      onOk={() => {
        form.validateFields().then((values) => {
          let d: DiResourceFile = {
            projectId: projectId,
            fileName: fileName,
          };
          addResourceFile({ ...d }).then((d) => {
            if (d.success) {
              message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
              onVisibleChange(false);
            }
          });
        });
      }}
    >
      <Form form={form} layout="horizontal" labelCol={{ span: 6 }} wrapperCol={{ span: 16 }}>
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="projectId"
          label={intl.formatMessage({ id: 'pages.resource.projectCode' })}
          rules={[{ required: true }, { max: 128 }]}
        >
          <Select
            disabled={data.id ? true : false}
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
            onChange={(item) => {
              setProjectId(item);
            }}
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
        <Form.Item label={intl.formatMessage({ id: 'pages.resource.file' })}>
          <Form.Item name="fileName" noStyle>
            <Spin spinning={isUploading}>
              <Upload.Dragger
                action="/api/di/resource/upload"
                maxCount={1}
                name="file"
                headers={{ u_token: localStorage.getItem(USER_AUTH.token) + '' }}
                data={{ projectId: form.getFieldValue('projectId') }}
                disabled={projectId ? false : true}
                beforeUpload={(file: RcFile) => {
                  const sizeLimit = file.size / 1024 / 1024 <= 500;
                  if (!sizeLimit) {
                    message.error(
                      intl.formatMessage({ id: 'pages.resource.file.upload.limit.500' }),
                    );
                  }
                  return sizeLimit;
                }}
                onChange={(info: UploadChangeParam<UploadFile<any>>) => {
                  let status = info.file.status;
                  let response = info.file.response;
                  status === 'uploading' ? setIsUploading(true) : setIsUploading(false);
                  if (status === 'done' && response && response.success) {
                    setIsDone(true);
                    setFileName(response.data);
                    message.success(
                      intl.formatMessage({ id: 'pages.resource.file.upload.success' }),
                    );
                  } else if (status === 'error') {
                    setFileName('');
                    message.success(intl.formatMessage({ id: 'pages.resource.file.upload.error' }));
                  } else if (status === 'removed') {
                    setFileName('');
                  } else if (response && !response.success) {
                    setFileName('');
                    message.error(response.errorMessage);
                  }
                }}
                onRemove={(file: UploadFile) => {
                  setIsDone(false);
                  request('/api/di/resource/upload', {
                    method: 'DELETE',
                    params: { projectId: form.getFieldValue('projectId'), fileName: file.name },
                  });
                }}
              >
                <p className="ant-upload-drag-icon">
                  <InboxOutlined />
                </p>
                <p className="ant-upload-text">
                  {intl.formatMessage({ id: 'pages.resource.file.upload.tooltip' })}
                </p>
                <p className="ant-upload-hint">
                  {intl.formatMessage({ id: 'pages.resource.file.upload.hint.single' })}
                </p>
              </Upload.Dragger>
            </Spin>
          </Form.Item>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default ResourceForm;
