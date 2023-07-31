import { Button, message, Modal, Table } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import React, { useState } from 'react';
import styles from './index.less';

interface DataType {
  key: React.Key;
  name: string;
  age: number;
  address: string;
}

interface IViewTableCellData {
  name: string;
  value: any;
}

const data: DataType[] = [];
for (let i = 0; i < 100; i++) {
  data.push({
    key: i,
    name: `Edward King ${i}`,
    age: 32,
    address: `London, Park Lane no. ${i}`,
  });
}

const EditorRightResultTable: React.FC = () => {
  const [viewTableCellData, setViewTableCellData] = useState<IViewTableCellData | null>(null);

  // 关闭弹窗
  const handleCancel = () => {
    setViewTableCellData(null);
  };
  //复制
  const copyTableCell = (data: IViewTableCellData) => {
    navigator.clipboard.writeText(data?.value || viewTableCellData?.value);
    message.success('复制成功');
  };
  //点击唤起modal
  const viewTableCell = (data: IViewTableCellData) => {
    setViewTableCellData(data);
  };

  const columns: ColumnsType<DataType> = [
    {
      title: 'Name',
      dataIndex: 'name',
      width: 130,
      fixed: 'left',
      render: (value: any, row: any, rowIndex: number) => {
        return (
          <div className={styles.tableItem}>
            <div>{value}</div>
            <div className={styles.tableHoverBox}>
              <img
                src="https://s.xinc818.com/files/webcilklz16y4pxm3zv/位图 (1).svg"
                alt="查看"
                onClick={() => {
                  viewTableCell({ name: value, value });
                }}
              />
              <img
                src="https://s.xinc818.com/files/webcilklz19gz7rldus/复制_o.svg"
                alt="复制"
                onClick={() => {
                    copyTableCell({ name: value, value })
                }}
              />
            </div>
          </div>
        );
      },
      sorter: (a, b) => a.name.length - b.name.length,
    },
    {
      title: 'Age',
      dataIndex: 'age',
      width: 130,
      render: (value: any, row: any, rowIndex: number) => {
        return (
          <div className={styles.tableItem}>
            <div>{value}</div>
            <div className={styles.tableHoverBox}>
              <img
                src="https://s.xinc818.com/files/webcilklz16y4pxm3zv/位图 (1).svg"
                alt="查看"
                onClick={() => {
                  viewTableCell({ name: value, value });
                }}
              />
              <img
                src="https://s.xinc818.com/files/webcilklz19gz7rldus/复制_o.svg"
                alt="复制"
                onClick={() => {
                    copyTableCell({ name: value, value })
                }}
              />
            </div>
          </div>
        );
      },
      sorter: (a, b) => a.age - b.age,
    },
    {
      title: 'Address',
      dataIndex: 'address',
    },
  ];
  return (
    <div className={styles.tableBox}>
      <Table columns={columns} bordered={true} dataSource={data} pagination={false} scroll={{x:1200}}/>
      <div className={styles.statusBar}>Result：执行成功. Time Consuming：25ms</div>
      <Modal
        title={viewTableCellData?.name}
        open={!!viewTableCellData?.name}
        onCancel={handleCancel}
        width="60vw"
        maskClosable={false}
        footer={
          <>
            {
              <Button
                onClick={copyTableCell.bind(null, viewTableCellData!)}
                className={styles.cancel}
              >
                Copy
              </Button>
            }
          </>
        }
      >
        <div className={styles.monacoEditor}>
          {/* <MonacoEditor
            id="view_table-Cell_data"
            appendValue={{
              text: viewTableCellData?.value,
              range: 'reset',
            }}
            options={{
              readOnly: true,
            }}
          /> */}
        </div>
      </Modal>
    </div>
  );
};
export default EditorRightResultTable;
