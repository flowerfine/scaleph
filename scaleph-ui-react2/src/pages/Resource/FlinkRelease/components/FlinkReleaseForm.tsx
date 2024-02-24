import {useIntl} from 'umi';
import {useState} from 'react';
import {Form, message, Modal, UploadFile, UploadProps} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormUploadButton} from '@ant-design/pro-components';
import {ModalFormProps} from '@/app.d';
import {DICT_TYPE} from '@/constants/dictType';
import {DictDataService} from '@/services/admin/dictData.service';
import {FlinkReleaseService} from '@/services/resource/flinkRelease.service';
import {FlinkRelease, FlinkReleaseUploadParam} from '@/services/resource/typings';

const FlinkReleaseForm: React.FC<ModalFormProps<FlinkRelease>> = ({data, visible, onVisibleChange, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);

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
          ? intl.formatMessage({id: 'app.common.operate.edit.label'}) +
          intl.formatMessage({id: 'pages.resource.flinkRelease'})
          : intl.formatMessage({id: 'app.common.operate.upload.label'}) +
          intl.formatMessage({id: 'pages.resource.flinkRelease'})
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      confirmLoading={uploading}
      okText={
        uploading
          ? intl.formatMessage({id: 'app.common.operate.uploading.label'})
          : intl.formatMessage({id: 'app.common.operate.upload.label'})
      }
      onOk={() => {
        form.validateFields().then((values) => {
          const uploadParam: FlinkReleaseUploadParam = {
            version: values.version,
            file: fileList[0],
            remark: values.remark,
          };
          setUploading(true);
          FlinkReleaseService.upload(uploadParam)
            .then((response) => {
              if (response.success) {
                setFileList([]);
                message.success(intl.formatMessage({id: 'app.common.operate.upload.success'}));
              }
            })
            .finally(() => {
              setUploading(false);
              onVisibleChange(false);
            });
        });
      }}
    >
      <ProForm form={form} layout={"horizontal"} submitter={false} labelCol={{span: 6}} wrapperCol={{span: 16}}>
        <ProFormDigit name="id" hidden/>
        <ProFormSelect
          name="version"
          label={intl.formatMessage({id: 'pages.resource.flinkRelease.version'})}
          rules={[{required: true}]}
          request={(params, props) => {
            return DictDataService.listDictDataByType2(DICT_TYPE.flinkVersion)
          }}
        />
        <ProFormUploadButton
          name={"file"}
          label={intl.formatMessage({id: 'pages.resource.file'})}
          title={intl.formatMessage({id: 'pages.resource.flinkRelease.file'})}
          max={1}
          fieldProps={props}
          rules={[{required: true}]}
        />
        <ProFormText
          name="remark"
          label={intl.formatMessage({id: 'app.common.data.remark'})}
          rules={[{max: 200}]}
        />
      </ProForm>
    </Modal>
  );
};

export default FlinkReleaseForm;
