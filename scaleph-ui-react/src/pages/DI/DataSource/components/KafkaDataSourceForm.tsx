import { ModalFormProps } from '@/app.d';
import { DataSourceService } from '@/services/project/dataSource.service';
import { MetaDataSource } from '@/services/project/typings';
import { Button, Form, Input, message, Modal } from 'antd';
import { useIntl } from 'umi';

const KafkaDataSourceForm: React.FC<ModalFormProps<MetaDataSource>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.project.di.dataSource' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.project.di.dataSource' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      footer={[
        <Button
          key="test"
          type="primary"
          danger={true}
          onClick={() => {
            form.validateFields().then((values) => {
              let ds: MetaDataSource = {
                id: values.id,
                datasourceName: values.datasourceName,
                datasourceType: data.datasourceType,
                props: {
                  bootstrapServers: values.bootstrapServers,
                },
                remark: values.remark,
                passwdChanged: true,
              };
              DataSourceService.testConnection(ds).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({ id: 'pages.project.di.dataSource.testConnect.success' }));
                }
              });
            });
          }}
        >
          {intl.formatMessage({ id: 'pages.project.di.dataSource.testConnect' })}
        </Button>,
        <Button key="cancel" onClick={onCancel}>
          {intl.formatMessage({ id: 'app.common.operate.cancel.label' })}
        </Button>,
        <Button
          key="confirm"
          type="primary"
          onClick={() => {
            form.validateFields().then((values) => {
              let ds: MetaDataSource = {
                id: values.id,
                datasourceName: values.datasourceName,
                datasourceType: data.datasourceType,
                props: {
                  bootstrapServers: values.bootstrapServers,
                },
                remark: values.remark,
              };
              data.id
                ? DataSourceService.updateDataSource({ ...ds }).then((d) => {
                    if (d.success) {
                      message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                      onVisibleChange(false);
                    }
                  })
                : DataSourceService.addDataSource({ ...ds }).then((d) => {
                    if (d.success) {
                      message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                      onVisibleChange(false);
                    }
                  });
            });
          }}
        >
          {intl.formatMessage({ id: 'app.common.operate.confirm.label' })}
        </Button>,
      ]}
    >
      <Form
        form={form}
        layout="horizontal"
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 16 }}
        initialValues={{
          id: data.id,
          datasourceName: data.datasourceName,
          bootstrapServers: data.props?.bootstrapServers,
          remark: data.remark,
        }}
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="datasourceName"
          label={intl.formatMessage({ id: 'pages.project.di.dataSource.dataSourceName' })}
          rules={[
            { required: true },
            { max: 60 },
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
            },
          ]}
        >
          <Input disabled={data.id ? true : false}></Input>
        </Form.Item>
        <Form.Item
          name="bootstrapServers"
          label={intl.formatMessage({ id: 'pages.project.di.dataSource.bootstrapServers' })}
          rules={[{ required: true }, { max: 2048 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.project.di.dataSource.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default KafkaDataSourceForm;
