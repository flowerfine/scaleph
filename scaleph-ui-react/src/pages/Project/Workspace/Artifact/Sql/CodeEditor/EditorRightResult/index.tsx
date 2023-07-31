import { Button, Modal, Tabs,message } from 'antd';
import React, { useState } from 'react';
import EditorRightResultTable from './EditorRightResultTable';
import styles from './index.less';

const defaultPanes = new Array(20).fill(null).map((_, index) => {
  const id = String(index + 1);
  return {
    label: (
      <div className={styles.bottomIcon}>
        <img
          style={{ width: 12, height: 13 }}
          src="https://s.xinc818.com/files/webcilkl00h48b0mc6z/成功.svg"
          alt="Success"
        />
        <span>Tab {id}</span>
      </div>
    ),
    children: <EditorRightResultTable />,
    key: id,
  };
});

interface IViewTableCellData {
  name: string;
  value: any;
}

const App: React.FC = () => {
  const [activeKey, setActiveKey] = useState(defaultPanes[0].key);
  const [items, setItems] = useState(defaultPanes);
  const [viewTableCellData, setViewTableCellData] = useState<IViewTableCellData | null>(null);
  const onChange = (key: string) => {
    setActiveKey(key);
  };

  const remove = (targetKey: string) => {
    const targetIndex = items.findIndex((pane) => pane.key === targetKey);
    const newPanes = items.filter((pane) => pane.key !== targetKey);
    if (newPanes.length && targetKey === activeKey) {
      const { key } = newPanes[targetIndex === newPanes.length ? targetIndex - 1 : targetIndex];
      setActiveKey(key);
    }
    setItems(newPanes);
  };

  const onEdit = (targetKey: string, action: 'add' | 'remove') => {
    remove(targetKey);
  };
  // 关闭弹窗
  const handleCancel = () => {
    setViewTableCellData(null);
  };

  const  copyTableCell=(data: IViewTableCellData)=> {
    navigator.clipboard.writeText(data?.value || viewTableCellData?.value);
    message.success('复制成功');
  }

  return (
    <div
      className={styles.editorRightResult}
      style={{ overflow: 'auto', height: '100%', width: '100%' }}
    >
      <Tabs
        hideAdd
        onChange={onChange}
        activeKey={activeKey}
        type="editable-card"
        onEdit={onEdit}
        items={items}
      />
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
          <MonacoEditor
            id="view_table-Cell_data"
            appendValue={{
              text: viewTableCellData?.value,
              range: 'reset',
            }}
            options={{
              readOnly: true,
            }}
          />
        </div>
      </Modal>
    </div>
  );
};

export default App;
