import React from 'react';
import {Table, TableColumnsType, Transfer, TransferProps} from 'antd';
import {ProTable} from "@ant-design/pro-components";
import {difference} from 'lodash'; // 注意这里改为大写的Difference
import {TransferItem} from "antd/lib/transfer";
import {TableRowSelection} from "antd/lib/table/interface";

interface DataType {
  id: number;
  name: string;
  remark: string;
}

interface TableTransferProps extends TransferProps<TransferItem> {
  dataSource: DataType[];
  leftColumns: TableColumnsType<DataType>;
  rightColumns: TableColumnsType<DataType>;
}

// 使用React.FC声明函数组件，并传入Props类型
const TableTransfer: React.FC<TableTransferProps> = (props) => {
  const {leftColumns, rightColumns, ...restProps} = props;
  return (
    <Transfer style={{ width: '100%' }} {...restProps}>
      {({
          direction,
          filteredItems,
          onItemSelect,
          onItemSelectAll,
          selectedKeys: listSelectedKeys,
          disabled: listDisabled,
        }) => {
        const columns = direction === 'left' ? leftColumns : rightColumns;
        const rowSelection: TableRowSelection<TransferItem> = {
          getCheckboxProps: () => ({ disabled: listDisabled }),
          onChange(selectedRowKeys) {
            onItemSelectAll(selectedRowKeys, 'replace');
          },
          selectedRowKeys: listSelectedKeys,
          selections: [Table.SELECTION_ALL, Table.SELECTION_INVERT, Table.SELECTION_NONE],
        };

        return (
          <Table>
            rowSelection={rowSelection}
            columns={columns}
            dataSource={filteredItems}
            size="small"
            style={{ pointerEvents: listDisabled ? 'none' : undefined }}
            onRow={({ key, disabled: itemDisabled }) => ({
              onClick: () => {
                if (itemDisabled || listDisabled) {
                  return;
                }
                onItemSelect(key, !listSelectedKeys.includes(key));
              },
            })}
          />
        );
      }}
    </Transfer>
  );
}

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
)
;
export default React.memo(TableTransfer);
