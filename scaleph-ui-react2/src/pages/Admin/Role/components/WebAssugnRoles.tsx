import mainHeight from '@/models/useMainSize';
import TableTransfer from '@/pages/Admin/Resource/Web/components/TransferTable';
import { SecResourceWeb } from '@/services/admin/typings';
import { AuthService } from '@/services/auth';
import { Button, Card, Form, message, Modal, Space } from 'antd';
import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useIntl } from 'umi';

// 定义组件 Props 类型
interface ModalFormParentProps<T> {
  data: T;
  visible: boolean;
  onVisibleChange?: (visible: boolean) => void;
  onCancel: () => void;
  onOK?: (values: any) => void;
}

const WebResourceForm: React.FC<ModalFormParentProps<SecResourceWeb>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const containerInfo = mainHeight('.ant-layout-content');
  const [roleLists, setRoleLists] = useState<Role[]>([]);

  // 角色表格列配置
  const tableColumns = [
    {
      dataIndex: 'userName',
      title: '用户名',
      width: 300,
    },
    {
      dataIndex: 'summary',
      title: '自我介绍',
      width: 300,
    },
  ];

  // 角色类型定义
  interface Role {
    id: string;
    name: string;
    resourceAuthorized: number;
    checkOut?: number;
  }

  // 合并数组
  function mergeArrays(array1: Role[], array2: Role[]): Role[] {
    array1.forEach((obj, index) => {
      obj.checkOut = 0;
    });
    array2.forEach((obj, index) => {
      obj.checkOut = 1;
    });
    return [...array1, ...array2];
  }

  // 异步获取数据
  const fetchData = useCallback(async () => {
    try {
      const res1 = await AuthService.requestUnauthorizedUsers({ roleId: data?.id });
      const res2 = await AuthService.requestAuthorizedUsers({ roleId: data?.id });
      if (res1?.records && res2?.records) {
        const mergedArray = mergeArrays(res1.records, res2.records);
        setRoleLists(mergedArray);
      }
    } catch (error) {
      console.error(error);
    }
  }, [data]);

  useEffect(() => {
    if (data) {
      fetchData();
    }
  }, [data, fetchData]);

  // 页面标题
  const returnTitle = useMemo(() => {
    return (
      <Space direction="vertical">
        <span>{` ${intl.formatMessage({ id: `menu.${data?.name}` })}-${intl.formatMessage({ id: 'app.common.operate.new.rolesUser' })}`}</span>
      </Space>
    );
  }, [data, intl]);

  // 获取选中角色的 id 数组
  const originTargetKeys = useMemo(() => {
    return roleLists?.filter((item) => item.checkOut === 1).map((item) => item.id);
  }, [roleLists]);

  // 过滤方法
  const handleFilter = useCallback((inputValue, item) => {
    return item?.name.indexOf(inputValue) !== -1;
  }, []);

  // 角色转移事件处理
  const handleChange = useCallback(
    async (targetKeys, direction, moveKeys) => {
      const roleIds = moveKeys.map((item: string | number) => +item);
      const params = {
        roleId: data?.id,
        userIds: roleIds,
      };
      if (direction === 'right') {
        // 批量为角色绑定用户
        await AuthService.rolesUser(params).then((res) => {
          if (res?.success) {
            message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }), 2);
          }
        });
      } else {
        // 批量为角色解除用户绑定
        await AuthService.deleteRolesUser(params).then((res) => {
          message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }), 2);
        });
      }
      fetchData();
    },
    [data, fetchData, intl],
  );

  return (
    <Modal
      open={visible}
      title={data.id ? intl.formatMessage({ id: 'app.common.operate.new.rolesUser' }) : ''}
      width={1100}
      destroyOnClose={true}
      onCancel={onCancel}
      cancelText={intl.formatMessage({ id: 'app.common.operate.close.label' })}
      centered
      footer={[
        <Button type="primary" onClick={onCancel}>
          {intl.formatMessage({ id: 'app.common.operate.close.label' })}
        </Button>,
      ]}
    >
      <div>
        <Card
          bodyStyle={{
            minHeight: `${containerInfo.height - 200}px`,
          }}
          title={returnTitle}
        >
          <TableTransfer
            containerHeight={containerInfo.height}
            titles={[intl.formatMessage({ id: 'app.common.operate.new.notAccreditUser' }), intl.formatMessage({ id: 'app.common.operate.new.accreditUser' })]}
            dataSource={roleLists}
            targetKeys={originTargetKeys}
            showSearch={true}
            rowKey={(record: { id: any }) => record.id}
            onChange={handleChange}
            filterOption={handleFilter}
            listStyle={{
              width: 500,
            }}
            leftColumns={tableColumns}
            rightColumns={tableColumns}
          />
        </Card>
      </div>
    </Modal>
  );
};

export default WebResourceForm;
