import { compareStrings } from '@/pages/Project/Workspace/Artifact/Sql/CodeEditor/components/sort';
import { Editor } from '@monaco-editor/react';
import { ArtColumn, BaseTable, features, useTablePipeline } from 'ali-react-table';
import { Button, message, Modal } from 'antd';
import React, { useEffect, useMemo, useState } from 'react';
import styles from './index.less';

interface IViewTableCellData {
  name: string;
  value: any;
}

const EditorRightResultTable: React.FC = ({ result, lastOneData }: any) => {
  const [viewTableCellData, setViewTableCellData] = useState<IViewTableCellData | null>(null);
  const [headerList, setHeaderList] = useState([]);
  const [dataList, setDataList] = useState([]);

  useEffect(() => {
    const data = result?.columns?.map((item: any) => ({
      dataType: item?.dataType,
      name: item?.columnName,
    }));
    setHeaderList(data);
  }, []);

  useEffect(() => {
    if (result?.data && lastOneData) {
      setDataList((prev) => prev.concat(result?.data));
    }
  }, [result]);

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

  const columns: ArtColumn[] = useMemo(
    () =>
      (headerList || []).map((item, index) => {
        const { dataType, name } = item;
        const isFirstLine = index === 0;
        const isNumber = dataType === 'STRING';
        return {
          code: name,
          name: name,
          key: name,
          lock: isFirstLine,
          width: 120,
          render: (value: any, row: any, rowIndex: number) => {
            return (
              <div className={styles.tableItem}>
                <div>{value}</div>
                <div className={styles.tableHoverBox}>
                  <img
                    src="https://s.xinc818.com/files/webcilklz16y4pxm3zv/位图 (1).svg"
                    alt="查看"
                    onClick={() => {
                      viewTableCell({ name, value });
                    }}
                  />
                  <img
                    src="https://s.xinc818.com/files/webcilklz19gz7rldus/复制_o.svg"
                    alt="复制"
                    onClick={() => {
                      copyTableCell({ name, value });
                    }}
                  />
                </div>
              </div>
            );
          },
          features: { sortable: isNumber ? compareStrings : true },
        };
      }),
    [headerList],
  );

  const pipeline = useTablePipeline()
    .input({ dataSource: dataList, columns })
    .use(
      features.sort({
        mode: 'single',
        // defaultSorts,
        highlightColumnWhenActive: true,
        // sorts,
        // onChangeSorts,
      }),
    )
    .use(
      features.columnResize({
        fallbackSize: 120,
        minSize: 80,
        maxSize: 1080,
        // handleBackground: '#ddd',
        // handleHoverBackground: '#aaa',
        // handleActiveBackground: '#89bff7',
      }),
    );

  return (
    <div className={styles.tableBox}>
      <BaseTable {...pipeline.getProps()} />
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
          {/* {viewTableCellData?.name} */}
          <Editor
            height="300px" // 设置编辑器高度
            defaultLanguage="sql" // 设置默认语言
            value={JSON.stringify(viewTableCellData?.value)} // 设置默认的SQL代码
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
