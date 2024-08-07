import React from 'react';
import {Transfer} from 'antd';
import {ProTable} from "@ant-design/pro-components";
import {difference} from 'lodash'; // 注意这里改为大写的Difference

// 定义组件的Props类型
interface Props {
  leftColumns: any[]; // 左侧表格列配置数组
  rightColumns: any[]; // 右侧表格列配置数组
  containerHeight: number; // 容器高度
}

// 使用React.FC声明函数组件，并传入Props类型
const TableTransfer: React.FC<Props> = ({leftColumns, rightColumns, ...restProps}) => (
  <Transfer {...restProps}>
    {({
        direction,
        filteredItems,
        onItemSelectAll,
        onItemSelect,
        selectedKeys: listSelectedKeys,
        disabled: listDisabled,
      }) => {
      const columns = direction === 'left' ? leftColumns : rightColumns;
      // 表格行选择配置
      const rowSelection = {
        onSelectAll(selected: boolean, selectedRows: any[]) {
          const treeSelectedKeys = selectedRows.map(({key}) => key);
          const diffKeys = selected
            ? difference(treeSelectedKeys, listSelectedKeys)
            : difference(listSelectedKeys, treeSelectedKeys);
          onItemSelectAll(diffKeys, selected);
        },
        onSelect: ({id}: { id: string }, selected: boolean) => {
          onItemSelect(id, selected);
        },
        selectedRowKeys: listSelectedKeys,
      };

      return (
        <ProTable
          scroll={{y: restProps.containerHeight - 380}}
          rowSelection={rowSelection}
          columns={columns}
          dataSource={filteredItems}
          size="small"
          rowKey="id"
          pagination={{
            defaultPageSize: 20,
          }}
          style={{pointerEvents: listDisabled ? 'none' : null}}
        />
      );
    }}
  </Transfer>
);
export default React.memo(TableTransfer);
