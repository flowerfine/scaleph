import React, {useEffect, useMemo, useState} from 'react';
import {Button, message, Modal, Table, Typography} from 'antd';
import {Editor, loader} from '@monaco-editor/react';
import * as monaco from "monaco-editor";
import {features, useTablePipeline} from 'ali-react-table';
import styles from './index.less';

interface IViewTableCellData {
  name: string;
  value: any;
}

loader.config({monaco})

const EditorRightResultTable: React.FC = ({ result, lastOneData, verticalSplitSizes }: any) => {
  const { Paragraph, Text } = Typography;
  const [viewTableCellData, setViewTableCellData] = useState<IViewTableCellData | null>(null);
  const [headerList, setHeaderList] = useState([]);
  const [dataList, setDataList] = useState([]);
  // const heightTable = useRef()

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

  const columns: any = useMemo(
    () =>
      (headerList || []).map((item, index) => {
        const { dataType, name } = item;
        const isFirstLine = index === 0 ? 'left' : '';
        const isNumber = dataType === 'STRING';
        return {
          title: name,
          dataIndex: name,
          key: name,
          fixed: isFirstLine,
          width: 120,
          render: (value: any, row: any, rowIndex: number) => {
            return (
              <div className={styles.tableItem}>
                <Paragraph ellipsis={{ rows: 1 }}>
                  {/* 将数组或者对象类型转换成字符串类型 */}
                  {Array.isArray(value) || typeof value === 'object'
                    ? JSON.stringify(value)
                    : value}
                </Paragraph>
                <div className={styles.tableHoverBox}>
                  <img
                    src="/images/EditorResult/查看.svg"
                    alt="查看"
                    onClick={() => {
                      viewTableCell({ name, value });
                    }}
                  />
                  <img
                    src="/images/EditorResult/复制.svg"
                    alt="复制"
                    onClick={() => {
                      copyTableCell({ name, value });
                    }}
                  />
                </div>
              </div>
            );
          },
          // features: { sortable: isNumber ? compareStrings : true },
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
    <div className={styles.tableBox} >
      {/* <BaseTable {...pipeline.getProps()} /> */}
      <Table columns={columns} dataSource={dataList} scroll={{ y: verticalSplitSizes[1] * 7, x: 1300 }} pagination={false} />
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
