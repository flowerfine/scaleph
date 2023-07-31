import {  Tabs } from 'antd';
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


const App: React.FC = () => {
  const [activeKey, setActiveKey] = useState(defaultPanes[0].key);
  const [items, setItems] = useState(defaultPanes);
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
    </div>
  );
};

export default App;
