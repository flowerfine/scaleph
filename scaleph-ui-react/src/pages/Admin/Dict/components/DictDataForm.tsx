import { Dict, ModalFormProps } from '@/app.d';
import { DictDataService } from '@/services/admin/dictData.service';
import { DictTypeService } from '@/services/admin/dictType.service';
import { SysDictData } from '@/services/admin/typings';
import { Form, Input, message, Modal, Select } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const { Option } = Select;

const DictDataForm: React.FC<ModalFormProps<SysDictData>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [dictTypeList, setDictTypeList] = useState<Dict[]>([]);

  useEffect(() => {
    DictTypeService.listAllDictType().then((d) => {
      setDictTypeList(d);
    });
  }, []);

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.admin.dict.dictData' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.admin.dict.dictData' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let dictData: SysDictData = {
            id: values.id,
            dictCode: values.dictCode,
            dictValue: values.dictValue,
            remark: values.remark,
            dictType: data.id
              ? {
                  dictTypeCode: values.dictType.value,
                }
              : {
                  dictTypeCode: values.dictType,
                },
          };
          data.id
            ? DictDataService.updateDictData({ ...dictData }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : DictDataService.addDictData({ ...dictData }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                  onVisibleChange(false);
                }
              });
        });
      }}
    >
      <Form
        form={form}
        layout="horizontal"
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 16 }}
        initialValues={
          data.id
            ? {
                id: data?.id,
                dictType: {
                  value: data?.dictType?.dictTypeCode,
                  label: data?.dictType?.dictTypeCode + '-' + data?.dictType?.dictTypeName,
                },
                dictCode: data?.dictCode,
                dictValue: data?.dictValue,
                remark: data?.remark,
              }
            : {}
        }
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="dictType"
          label={intl.formatMessage({ id: 'pages.admin.dict.dictType' })}
          rules={[{ required: true }]}
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
            {dictTypeList.map((item) => {
              return (
                <Option key={item.value} value={item.value}>
                  {item.value + '-' + item.label}
                </Option>
              );
            })}
          </Select>
        </Form.Item>
        <Form.Item
          name="dictCode"
          label={intl.formatMessage({ id: 'pages.admin.dict.dictCode' })}
          rules={[
            { required: true },
            { max: 30 },
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
            },
          ]}
        >
          <Input disabled={data.id ? true : false}></Input>
        </Form.Item>
        <Form.Item
          name="dictValue"
          label={intl.formatMessage({ id: 'pages.admin.dict.dictValue' })}
          rules={[{ required: true }, { max: 100 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.admin.dict.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default DictDataForm;
