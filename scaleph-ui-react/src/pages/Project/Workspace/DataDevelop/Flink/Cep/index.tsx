import { useRef, useState } from 'react';
import { Button, Form, Input, message, Modal, Space, Tooltip } from 'antd';
import { DeleteOutlined, DownloadOutlined, EditOutlined, FolderOpenOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import { history, useAccess, useIntl } from '@umijs/max';
import { WORKSPACE_CONF } from '@/constants/constant';
import FilterRules, { IComponentProps } from '@/components/FilterRules';

import { INIT_DATA, INIT_ROW_VALUES, IRow } from './constants';

const MyInput = ({ name }: IComponentProps<IRow>) => (
  <Form.Item name={['condition', ...name, 'input']}>
    <Input />
  </Form.Item>
);

const DataDevelopFlinkCepWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();
  const [form] = Form.useForm();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);



  return (
    <PageContainer title={false}>


     
      <Form form={form}>
        <Button
          onClick={() => form.setFieldsValue({ condition: INIT_DATA })}
          style={{ marginBottom: 16 }}
        >
          添加数据
        </Button>
        <Form.Item name="condition">
          <FilterRules<IRow>
            component={(props) => <MyInput {...props} />}
            notEmpty={{ data: false }}
            initValues={INIT_ROW_VALUES}
          />
        </Form.Item>
        <Button onClick={async () => console.log(await form.validateFields())}>
          控制台查看数据
        </Button>
      </Form>
    </PageContainer>
  );
};
export default DataDevelopFlinkCepWeb;
