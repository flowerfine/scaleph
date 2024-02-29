import { ResponseBody } from '@/app';
import { SecResourceWeb } from '@/services/admin/typings';
import { AuthService } from '@/services/auth';
import { message, Modal, Tree } from 'antd';
import type { TreeProps } from 'antd/es/tree';
import React, { useCallback, useEffect, useState } from 'react';
import { useIntl } from 'umi';

// 定义组件 Props 类型
interface ModalFormParentProps<T> {
  data: T;
  visible: boolean;
  onVisibleChange?: (visible: boolean) => void;
  onCancel: () => void;
  onOK?: (values: any) => void;
}

// WebResourceForm 组件
const WebResourceForm: React.FC<ModalFormParentProps<SecResourceWeb>> = ({
  data,
  visible,
  onCancel,
}) => {
  const intl = useIntl();
  const [roleLists, setRoleLists] = useState<any[]>([]);
  const [menuId, setMenuId] = useState<number[]>([]);
  const [menuIdTotal, setMenuIdTotal] = useState<number[]>([]);

  // 数据转换函数
  function convertData(data: any[] | ResponseBody<any>): any[] {
    return data.map((item) => {
      const newItem = {
        title: item.name,
        key: String(item.id),
        authorized: item.authorized.value,
      };
      if (item.children && item.children.length > 0) {
        newItem.children = convertData(item.children);
      }
      return newItem;
    });
  }

  // 树节点类型
  interface TreeNode {
    title: string;
    key: number;
    authorized: boolean;
    children?: TreeNode[];
  }

  // 根据权限值查找节点的键
  function findKeysByAuthorized(data: TreeNode[], authorizedValue: boolean): number[] {
    const result: number[] = [];
    function traverse(node: TreeNode) {
      if (node.authorized === authorizedValue) {
        result.push(+node.key);
      }
      if (node.children) {
        node.children.forEach((child) => traverse(child));
      }
    }
    data.forEach((item) => traverse(item));
    return result;
  }

  // 异步获取数据
  const fetchData = useCallback(async () => {
    try {
      const res1 = await AuthService.requestResourceWebs({ roleId: data?.id });
      const treeData = convertData(res1);
      const filteredIds = findKeysByAuthorized(treeData, '1');
      setRoleLists(res1 || []);
      setMenuId(filteredIds || []);
      setMenuIdTotal(filteredIds || []);
    } catch (error) {
      console.error(error);
    }
  }, [data]);

  useEffect(() => {
    if (data) {
      fetchData();
    }
  }, [data, fetchData]);

  // 处理勾选事件
  const onCheck: TreeProps['onCheck'] = (checkedKeys, info) => {
    setMenuId(checkedKeys);
  };

  // 提交操作
  const submit = async () => {
    const diffValues = menuIdTotal.filter((item) => !menuId.includes(item)); // 批量为角色解除
    const extraValues = menuId.filter((item) => !menuIdTotal.includes(item)); // 批量为角色绑定

    if (diffValues.length !== 0) {
      await AuthService.requestDeleteRoleResourceWebs({
        roleId: data?.id,
        resourceWebIds: diffValues,
      }).then((res) => {
        if (res?.success) {
          message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }), 2);
        }
      });
    }

    if (extraValues.length !== 0) {
      await AuthService.requestRoleResourceWebs({
        roleId: data?.id,
        resourceWebIds: extraValues,
      }).then((res) => {
        if (res?.success) {
          message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }), 2);
        }
      });
    }
    onCancel();
  };

  return (
    <Modal
      open={visible}
      title={data.id ? intl.formatMessage({ id: 'app.common.operate.new.rolesWeb' }) : ''}
      width={500}
      destroyOnClose={true}
      onCancel={onCancel}
      cancelText={intl.formatMessage({ id: 'app.common.operate.close.label' })}
      centered
      onOk={submit}
    >
      <div
        style={{
          maxHeight: '450px',
          overflow: 'auto',
        }}
      >
        <Tree
          checkable
          onCheck={onCheck}
          checkedKeys={menuId}
          treeData={roleLists}
          defaultExpandAll
          fieldNames={{
            title: 'name',
            key: 'id',
            children: 'children',
          }}
        />
      </div>
    </Modal>
  );
};

export default WebResourceForm;
