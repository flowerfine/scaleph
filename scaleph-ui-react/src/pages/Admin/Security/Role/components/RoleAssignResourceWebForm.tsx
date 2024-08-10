import React, {useCallback, useEffect, useState} from 'react';
import {message, Modal, Tree} from 'antd';
import type {TreeProps} from 'antd/es/tree';
import {useIntl} from '@umijs/max';
import {ModalFormProps} from '@/typings';
import {SecResourceWeb, SecRole} from '@/services/admin/typings';
import {AuthorizationService} from "@/services/admin/security/authorization.service";

// 树节点类型
interface TreeNode {
  key: number;
  title: string;
  authorized?: string | number;
  children?: TreeNode[];
}

const RoleAssignResourceWebForm: React.FC<ModalFormProps<SecRole>> = ({data, visible, onCancel}) => {
  const intl = useIntl();
  const [treeData, setTreeData] = useState<TreeNode[]>([]);
  const [menuId, setMenuId] = useState<number[]>([]);
  const [menuIdTotal, setMenuIdTotal] = useState<number[]>([]);

  // 数据转换函数
  function convertData(secResourceWebList: SecResourceWeb[]): any[] {
    return secResourceWebList.map((item) => {
      const newItem: TreeNode = {
        key: item.id,
        title: item.label,
        authorized: item.authorized?.value
      };
      if (item.children && item.children.length > 0) {
        newItem.children = convertData(item.children);
      }
      return newItem;
    });
  }

  // 根据权限值查找节点的键
  function findKeysByAuthorized(data: TreeNode[], authorizedValue: string): number[] {
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
      const secResourceWebList = await AuthorizationService.requestResourceWebs({roleId: data?.id}).then(response => response.data);
      const treeData = convertData(secResourceWebList);
      const filteredIds = findKeysByAuthorized(treeData, '1');
      setTreeData(treeData || []);
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
      await AuthorizationService.requestDeleteRoleResourceWebs({
        roleId: data?.id,
        resourceWebIds: diffValues,
      }).then((res) => {
        if (res?.success) {
          message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}), 2);
        }
      });
    }

    if (extraValues.length !== 0) {
      await AuthorizationService.requestRoleResourceWebs({
        roleId: data?.id,
        resourceWebIds: extraValues,
      }).then((res) => {
        if (res?.success) {
          message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}), 2);
        }
      });
    }
    onCancel();
  };

  return (
    <Modal
      open={visible}
      title={data.id ? intl.formatMessage({id: 'pages.admin.security.authorization.role2ResourceWebs'}) : ''}
      width={500}
      destroyOnClose={true}
      onCancel={onCancel}
      cancelText={intl.formatMessage({id: 'app.common.operate.close.label'})}
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
          treeData={treeData}
          defaultExpandAll
        />
      </div>
    </Modal>
  );
};

export default RoleAssignResourceWebForm;
