import {ModalFormProps} from '@/app.d';
import {UploadOutlined} from '@ant-design/icons';
import {Form, message, Modal, UploadFile} from 'antd';
import {useState} from 'react';
import {useIntl} from 'umi';
import {Kerberos, KerberosUploadParam} from "@/pages/Resource/typings";
import {ProForm, ProFormText, ProFormUploadButton} from '@ant-design/pro-components';
import {KerberosService} from "@/pages/Resource/KerberosService";

const KerberosForm: React.FC<ModalFormProps<Kerberos>> = ({data, visible, onVisibleChange, onCancel}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);

  return (
    <Modal
      open={visible}
      title={
        intl.formatMessage({id: 'app.common.operate.upload.label'}) +
        intl.formatMessage({id: 'pages.resource.jar'})
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
          const uploadParam: KerberosUploadParam = {
            name: values.name,
            principal: values.principal,
            file: fileList[0],
            remark: values.remark,
          };
          setUploading(true);
          KerberosService.upload(uploadParam)
            .then(() => {
              setFileList([]);
              message.success(intl.formatMessage({id: 'app.common.operate.upload.success'}));
            })
            .catch(() => {
              message.error(intl.formatMessage({id: 'app.common.operate.upload.failure'}));
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
      <ProForm form={form} layout={"horizontal"} labelCol={{span: 6}} wrapperCol={{span: 16}} submitter={false}>
        <ProFormText name={"id"} hidden/>
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.resource.kerberos.name'})}
          rules={[{required: true}, {max: 128}]}
        />
        <ProFormText
          name="principal"
          label={intl.formatMessage({id: 'pages.resource.kerberos.principal'})}
          rules={[{required: true}]}
        />
        <ProFormUploadButton
          label={intl.formatMessage({id: 'pages.resource.file'})}
          icon={<UploadOutlined/>}
          title={intl.formatMessage({id: 'pages.resource.kerberos.file'})}
          max={1}
          fieldProps={{
            multiple: false,
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
            fileList
          }}
        />
        <ProFormText
          name="remark"
          label={intl.formatMessage({id: 'pages.resource.remark'})}
          rules={[{max: 200}]}
        />
      </ProForm>
    </Modal>
  );
};

export default KerberosForm;
