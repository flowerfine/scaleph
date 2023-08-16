import { Editor } from '@monaco-editor/react';
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
    name1: `1${i}`,
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

  // 复制表格单元格内容到剪贴板
  const copyTableCell = (data: IViewTableCellData) => {
    navigator.clipboard.writeText(data?.value || viewTableCellData?.value);
    message.success('复制成功');
  };

  // 点击查看表格单元格内容
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
                  copyTableCell({ name: value, value });
                }}
              />
            </div>
          </div>
        );
      },
    },
    {
      title: 'Name1',
      dataIndex: 'name1',
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
                  copyTableCell({ name: value, value });
                }}
              />
            </div>
          </div>
        );
      },
    },
    {
      title: 'Name',
      dataIndex: 'name',
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
                  copyTableCell({ name: value, value });
                }}
              />
            </div>
          </div>
        );
      },
    },
    {
      title: 'Address',
      dataIndex: 'address',
    },
  ];

  return (
    <div className={styles.tableBox}>
      <Table
        columns={columns}
        bordered={true}
        dataSource={data}
        pagination={false}
        scroll={{ x: 1200 }}
        sticky
      />
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
          <Editor
            height="300px" // 设置编辑器高度
            defaultLanguage="sql" // 设置默认语言
            value={viewTableCellData?.name} // 设置默认的SQL代码
            theme="vs" // 设置主题样式
            options={{
              readOnly: true,
            }}
          />
        </div>
      </Modal>
    </div>
  );
};

export default EditorRightResultTable;