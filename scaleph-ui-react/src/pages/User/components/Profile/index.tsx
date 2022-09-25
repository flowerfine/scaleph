import { Dict } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { SecUser } from '@/services/admin/typings';
import { UserService } from '@/services/admin/user.service';
import { Button, Card, Col, DatePicker, Form, Input, message, Row, Select } from 'antd';
import moment from 'moment';
import { useLayoutEffect, useState } from 'react';
import { useIntl } from 'umi';

const Profile: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [genderList, setGenderList] = useState<Dict[]>([]);
  const [nationList, setNationList] = useState<Dict[]>([]);
  const [idCardTypeList, setIdCardTypeList] = useState<Dict[]>([]);
  useLayoutEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.idCardType).then((d) => {
      setIdCardTypeList(d);
    });
    DictDataService.listDictDataByType(DICT_TYPE.gender).then((d) => {
      setGenderList(d);
    });
    DictDataService.listDictDataByType(DICT_TYPE.nation).then((d) => {
      setNationList(d);
    });
    refreshUserInfo();
  }, []);

  const refreshUserInfo = () => {
    UserService.getUserInfo().then((resp) => {
      form.setFieldsValue({
        id: resp.id,
        userName: resp.userName,
        email: resp.email,
        nickName: resp.nickName,
        realName: resp.realName,
        mobilePhone: resp.mobilePhone,
        gender: resp.gender?.value,
        idCardType: resp.idCardType?.value,
        idCardNo: resp.idCardNo,
        nation: resp.nation?.value,
        birthday: resp?.birthday ? moment(resp?.birthday, 'YYYY-MM-DD HH:mm:ss') : null,
        qq: resp.qq,
        wechat: resp.wechat,
        summary: resp.summary,
      });
    });
  };

  return (
    <Card style={{ width: '70%' }} bordered={false}>
      <Form
        form={form}
        layout="vertical"
        onFinish={() => {
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
            UserService.updateUser({ ...user }).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                refreshUserInfo();
              }
            });
          });
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
              ]}
            >
              <Input disabled={true}></Input>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name="email"
              label={intl.formatMessage({ id: 'pages.admin.user.email' })}
              rules={[{ required: true }, { max: 100 }, { type: 'email' }]}
            >
              <Input disabled={true}></Input>
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
              <DatePicker picker="date" style={{ width: '100%' }} allowClear={false}></DatePicker>
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
            <Form.Item>
              <Button type="primary" htmlType="submit">
                {intl.formatMessage({ id: 'app.common.operate.confirm.label' })}
              </Button>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Card>
  );
};

export default Profile;
