import {useIntl} from 'umi';
import {Form, message, Modal, UploadFile, UploadProps} from 'antd';
import {ProForm, ProFormText, ProFormUploadButton} from '@ant-design/pro-components';
import {ModalFormProps} from '@/app.d';
import {ClusterCredentialService} from '@/services/resource/clusterCredential.service';
import {ClusterCredential, ClusterCredentialUploadParam} from '@/services/resource/typings';
import {useState} from "react";

const ClusterCredentialForm: React.FC<ModalFormProps<ClusterCredential>> = ({
                                                                              data,
                                                                              visible,
                                                                              onVisibleChange,
                                                                              onCancel
                                                                            }) => {
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
        intl.formatMessage({id: 'app.common.operate.new.label'}) +
        intl.formatMessage({id: 'pages.resource.clusterCredential'})
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
          const param: ClusterCredentialUploadParam = {
            name: values.name,
            context: values.context,
            file: fileList[0],
            remark: values.remark,
          };
          setUploading(true);
          ClusterCredentialService.upload(param)
            .then((response) => {
              if (response.success) {
                message.success(intl.formatMessage({id: 'app.common.operate.upload.success'}));
              }
            })
            .finally(() => {
              setUploading(false);
              if (onVisibleChange) {
                onVisibleChange(false);
              }
            });
        });
      }}
    >
      <ProForm form={form} layout={"horizontal"} submitter={false} labelCol={{span: 6}} wrapperCol={{span: 16}}>
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.resource.clusterCredential.name'})}
          rules={[
            {required: true},
            {max: 30},
            {
              pattern: /^[\w\s-_.]+$/,
              message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
            },
          ]}
        />
        <ProFormText
          name="context"
          label={intl.formatMessage({id: 'pages.resource.clusterCredential.context'})}
        />
        <ProFormUploadButton
          name={"file"}
          label={intl.formatMessage({id: 'pages.resource.file'})}
          title={intl.formatMessage({id: 'pages.resource.clusterCredential.file'})}
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

export default ClusterCredentialForm;
