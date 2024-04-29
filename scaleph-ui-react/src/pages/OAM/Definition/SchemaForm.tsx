import {useState} from "react";
import {useAccess, useIntl} from '@umijs/max';
import {DatePicker} from 'antd';
import type {ProFormLayoutType} from '@ant-design/pro-components';
import {PageContainer, ProFormColumnsType} from "@ant-design/pro-components";
import {BetaSchemaForm} from "@ant-design/pro-form";
import dayjs from 'dayjs';

const valueEnum = {
  all: {text: '全部', status: 'Default'},
  open: {
    text: '未解决',
    status: 'Error',
  },
  closed: {
    text: '已解决',
    status: 'Success',
    disabled: true,
  },
  processing: {
    text: '解决中',
    status: 'Processing',
  },
};

type DataItem = {
  name: string;
  state: string;
};
const columns: ProFormColumnsType<DataItem>[] = [
  {
    title: '标题',
    dataIndex: 'title',
    formItemProps: {
      rules: [
        {
          required: true,
          message: '此项为必填项',
        },
      ],
    },
    width: 'md',
    colProps: {
      xs: 24,
      md: 12,
    },
    initialValue: '默认值',
    convertValue: (value) => {
      return `标题：${value}`;
    },
    transform: (value) => {
      return {
        title: `${value}-转换`,
      };
    },
  },
  {
    title: '状态',
    dataIndex: 'state',
    valueType: 'select',
    valueEnum,
    width: 'md',
    colProps: {
      xs: 24,
      md: 12,
    },
  },
  {
    title: '标签',
    dataIndex: 'labels',
    width: 'md',
    colProps: {
      xs: 12,
      md: 4,
    },
  },
  {
    valueType: 'switch',
    title: '开关',
    dataIndex: 'Switch',
    fieldProps: {
      style: {
        width: '200px',
      },
    },
    width: 'md',
    colProps: {
      xs: 12,
      md: 20,
    },
  },
  {
    title: '创建时间',
    key: 'showTime',
    dataIndex: 'createName',
    initialValue: [dayjs().add(-1, 'm'), dayjs()],
    renderFormItem: () => <DatePicker.RangePicker/>,
    width: 'md',
    colProps: {
      xs: 24,
      md: 12,
    },
  },
  {
    title: '更新时间',
    dataIndex: 'updateName',
    initialValue: [dayjs().add(-1, 'm'), dayjs()],
    renderFormItem: () => <DatePicker.RangePicker/>,
    width: 'md',
    colProps: {
      xs: 24,
      md: 12,
    },
  },
  {
    title: '分组',
    valueType: 'group',
    columns: [
      {
        title: '状态',
        dataIndex: 'groupState',
        valueType: 'select',
        width: 'xs',
        colProps: {
          xs: 12,
        },
        valueEnum,
      },
      {
        title: '标题',
        width: 'md',
        dataIndex: 'groupTitle',
        colProps: {
          xs: 12,
        },
        formItemProps: {
          rules: [
            {
              required: true,
              message: '此项为必填项',
            },
          ],
        },
      },
    ],
  },
  {
    title: '列表',
    valueType: 'formList',
    dataIndex: 'list',
    initialValue: [{state: 'all', title: '标题'}],
    colProps: {
      xs: 24,
      sm: 12,
    },
    columns: [
      {
        valueType: 'group',
        columns: [
          {
            title: '状态',
            dataIndex: 'state',
            valueType: 'select',
            colProps: {
              xs: 24,
              sm: 12,
            },
            width: 'xs',
            valueEnum,
          },
          {
            title: '标题',
            dataIndex: 'title',
            width: 'md',
            formItemProps: {
              rules: [
                {
                  required: true,
                  message: '此项为必填项',
                },
              ],
            },
            colProps: {
              xs: 24,
              sm: 12,
            },
          },
        ],
      },
      {
        valueType: 'dateTime',
        initialValue: new Date(),
        dataIndex: 'currentTime',
        width: 'md',
      },
    ],
  },
  {
    title: 'FormSet',
    valueType: 'formSet',
    dataIndex: 'formSet',
    colProps: {
      xs: 24,
      sm: 12,
    },
    rowProps: {
      gutter: [16, 0],
    },
    columns: [
      {
        title: '状态',
        dataIndex: 'groupState',
        valueType: 'select',
        width: 'md',
        valueEnum,
      },
      {
        width: 'xs',
        title: '标题',
        dataIndex: 'groupTitle',
        tooltip: '标题过长会自动收缩',
        formItemProps: {
          rules: [
            {
              required: true,
              message: '此项为必填项',
            },
          ],
        },
      },
    ],
  },
  {
    title: '创建时间',
    dataIndex: 'created_at',
    valueType: 'dateRange',
    width: 'md',
    colProps: {
      span: 24,
    },
    transform: (value) => {
      return {
        startTime: value[0],
        endTime: value[1],
      };
    },
  },
];

const OamDefinitionSchemaFormWeb: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();

  const [layoutType, setLayoutType] = useState<ProFormLayoutType>('Form');

  return (
    <PageContainer title={false}>
      <BetaSchemaForm<DataItem>
        layoutType={layoutType}
        rowProps={{
          gutter: [16, 16],
        }}
        colProps={{
          span: 12,
        }}
        grid={true}
        onFinish={async (values) => {
          console.log(values);
        }}
        columns={columns}
      />
    </PageContainer>

  );
};

export default OamDefinitionSchemaFormWeb;
