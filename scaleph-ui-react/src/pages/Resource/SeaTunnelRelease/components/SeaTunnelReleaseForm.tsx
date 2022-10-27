import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { SeatunnelReleaseService } from '@/services/resource/seatunnelRelease.service';
import { SeaTunnelRelease, SeaTunnelReleaseUploadParam } from '@/services/resource/typings';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Modal, Select, Upload, UploadFile, UploadProps } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const SeaTunnelReleaseForm: React.FC<ModalFormProps<SeaTunnelRelease>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);
  const [seatunnelVersionList, setSeatunnelVersionList] = useState<Dict[]>([]);
  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.seatunnelVersion).then((d) => {
      setSeatunnelVersionList(d);
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
      open={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.resource.seatunnelRelease' })
          : intl.formatMessage({ id: 'app.common.operate.upload.label' }) +
            intl.formatMessage({ id: 'pages.resource.seatunnelRelease' })
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
          const uploadParam: SeaTunnelReleaseUploadParam = {
            version: values.version,
            file: fileList[0],
            remark: values.remark,
          };
          setUploading(true);
          SeatunnelReleaseService.upload(uploadParam)
            .then((response) => {
              if (response.success) {
                setFileList([]);
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
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="version"
          label={intl.formatMessage({ id: 'pages.resource.seatunnelRelease.version' })}
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
          >
            {seatunnelVersionList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        </Form.Item>
        <Form.Item
          label={intl.formatMessage({ id: 'pages.resource.file' })}
          rules={[{ required: true }]}
        >
          <Upload {...props}>
            <Button icon={<UploadOutlined />}>
              {intl.formatMessage({ id: 'pages.resource.seatunnelRelease.file' })}
            </Button>
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

export default SeaTunnelReleaseForm;
