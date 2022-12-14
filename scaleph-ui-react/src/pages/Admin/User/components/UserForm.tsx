import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { SecUser } from '@/services/admin/typings';
import { UserService } from '@/services/admin/user.service';
import { Col, DatePicker, Form, Input, message, Modal, Row, Select } from 'antd';
import moment from 'moment';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const UserForm: React.FC<ModalFormProps<SecUser>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [genderList, setGenderList] = useState<Dict[]>([]);
  const [nationList, setNationList] = useState<Dict[]>([]);
  const [idCardTypeList, setIdCardTypeList] = useState<Dict[]>([]);
  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.idCardType).then((d) => {
      setIdCardTypeList(d);
    });
    DictDataService.listDictDataByType(DICT_TYPE.gender).then((d) => {
      setGenderList(d);
    });
    DictDataService.listDictDataByType(DICT_TYPE.nation).then((d) => {
      setNationList(d);
    });
  }, []);

  return (
    <Modal
      open={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.admin.user' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.admin.user' })
      }
      width={780}
      bodyStyle={{ maxHeight: '640px', overflowY: 'scroll' }}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let user: SecUser = {
            id: values.id,
            userName: values.userName,
            email: values.email,
            nickName: values.nickName,
            realName: values.realName,
            mobilePhone: values.mobilePhone,
            gender: { value: values.gender },
            idCardType: { value: values.idCardType },
            idCardNo: values.idCardNo,
            nation: { value: values.nation },
            birthday: values.birthday?.valueOf(),
            qq: values.qq,
            wechat: values.wechat,
            summary: values.summary,
          };
          data.id
            ? UserService.updateUser({ ...user }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : UserService.addUser({ ...user }).then((d) => {
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
        layout="vertical"
        initialValues={{
          id: data.id,
          userName: data.userName,
          email: data.email,
          nickName: data.nickName,
          realName: data.realName,
          mobilePhone: data.mobilePhone,
          gender: data.gender?.value,
          idCardType: data.idCardType?.value,
          idCardNo: data.idCardNo,
          nation: data.nation?.value,
          birthday: data.birthday ? moment(data.birthday, 'YYYY-MM-DD HH:mm:ss') : null,
          qq: data.qq,
          wechat: data.wechat,
          summary: data.summary,
        }}
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Row gutter={[12, 12]}>
          <Col span={12}>
            <Form.Item
              name="userName"
              label={intl.formatMessage({ id: 'pages.admin.user.userName' })}
              rules={[
                { required: true },
                { max: 30 },
                { min: 5 },
                {
                  pattern: /^[a-zA-Z0-9_]+$/,
                  message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
                },
                {
                  validator: (rule, value, callback) => {
                    data.id
                      ? callback()
                      : UserService.isUserExists(value).then((resp) => {
                          if (resp) {
                            callback();
                          } else {
                            callback(
                              intl.formatMessage({ id: 'app.common.validate.sameUserName' }),
                            );
                          }
                        });
                  },
                },
              ]}
            >
              <Input disabled={data.id ? true : false}></Input>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="email"
              label={intl.formatMessage({ id: 'pages.admin.user.email' })}
              rules={[
                { required: true },
                { max: 100 },
                { type: 'email' },
                {
                  validator: (rule, value, callback) => {
                    data.id
                      ? callback()
                      : UserService.isEmailExists(value).then((resp) => {
                          if (resp) {
                            callback();
                          } else {
                            callback(intl.formatMessage({ id: 'app.common.validate.sameEmail' }));
                          }
                        });
                  },
                },
              ]}
            >
              <Input disabled={data.id ? true : false}></Input>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="nickName"
              label={intl.formatMessage({ id: 'pages.admin.user.nickName' })}
              rules={[{ max: 50 }]}
            >
              <Input></Input>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="realName"
              label={intl.formatMessage({ id: 'pages.admin.user.realName' })}
              rules={[{ max: 30 }]}
            >
              <Input></Input>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="mobilePhone"
              label={intl.formatMessage({ id: 'pages.admin.user.mobilePhone' })}
              rules={[{ max: 11 }]}
            >
              <Input></Input>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="gender" label={intl.formatMessage({ id: 'pages.admin.user.gender' })}>
              <Select
                showSearch={true}
                allowClear={true}
                optionFilterProp="label"
                filterOption={(input, option) =>
                  (option!.children as unknown as string)
                    .toLowerCase()
                    .includes(input.toLowerCase())
                }
              >
                {genderList.map((item) => {
                  return (
                    <Select.Option key={item.value} value={item.value}>
                      {item.label}
                    </Select.Option>
                  );
                })}
              </Select>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="idCardType"
              label={intl.formatMessage({ id: 'pages.admin.user.idCardType' })}
            >
              <Select
                showSearch={true}
                allowClear={true}
                optionFilterProp="label"
                filterOption={(input, option) =>
                  (option!.children as unknown as string)
                    .toLowerCase()
                    .includes(input.toLowerCase())
                }
              >
                {idCardTypeList.map((item) => {
                  return (
                    <Select.Option key={item.value} value={item.value}>
                      {item.label}
                    </Select.Option>
                  );
                })}
              </Select>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="idCardNo"
              label={intl.formatMessage({ id: 'pages.admin.user.idCardNo' })}
              rules={[{ max: 18 }]}
            >
              <Input></Input>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item name="nation" label={intl.formatMessage({ id: 'pages.admin.user.nation' })}>
              <Select
                showSearch={true}
                allowClear={true}
                optionFilterProp="label"
                filterOption={(input, option) =>
                  (option!.children as unknown as string)
                    .toLowerCase()
                    .includes(input.toLowerCase())
                }
              >
                {nationList.map((item) => {
                  return (
                    <Select.Option key={item.value} value={item.value}>
                      {item.label}
                    </Select.Option>
                  );
                })}
              </Select>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="birthday"
              label={intl.formatMessage({ id: 'pages.admin.user.birthday' })}
              rules={[{ type: 'date' }]}
            >
              <DatePicker picker="date" style={{ width: '100%' }} allowClear={true}></DatePicker>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="qq"
              label={intl.formatMessage({ id: 'pages.admin.user.qq' })}
              rules={[{ max: 15 }]}
            >
              <Input></Input>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="wechat"
              label={intl.formatMessage({ id: 'pages.admin.user.wechat' })}
              rules={[{ max: 50 }]}
            >
              <Input></Input>
            </Form.Item>
          </Col>
          <Col span={24}>
            <Form.Item
              name="summary"
              label={intl.formatMessage({ id: 'pages.admin.user.summary' })}
              rules={[{ max: 500 }]}
            >
              <Input.TextArea></Input.TextArea>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default UserForm;
