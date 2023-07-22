import { Tabs, Input } from 'antd';
import React, { useRef, useState } from 'react';
import Editor from '../Editor';
import './index.less';

const { TabPane } = Tabs;

interface TabItem {
  label: string;
  children: Element | string| JSX.Element;
  key: string;
  closable?: boolean;
  isEditing?: boolean;
}

const initialItems: TabItem[] = [
  { label: 'Tab 1', children: <Editor/>, key: '1' },
  { label: 'Tab 2', children: 'Content of Tab 2', key: '2' },
  { label: 'Tab 3', children: 'Content of Tab 3', key: '3' },
];

const TabComponents: React.FC = () => {
  const [activeKey, setActiveKey] = useState(initialItems[0].key);
  const [items, setItems] = useState<TabItem[]>(initialItems);
  const newTabIndex = useRef(0);

  const onChange = (newActiveKey: string) => {
    setActiveKey(newActiveKey);
  };

  const add = () => {
    const newActiveKey = `newTab${newTabIndex.current++}`;
    const newPanes = [...items];
    newPanes.push({ label: 'New Tab', children: 'Content of new Tab', key: newActiveKey });
    setItems(newPanes);
    setActiveKey(newActiveKey);
  };

  const remove = (targetKey: string) => {
    let newActiveKey = activeKey;
    let lastIndex = -1;
    items.forEach((item, i) => {
      if (item.key === targetKey) {
        lastIndex = i - 1;
      }
    });
    const newPanes = items.filter((item) => item.key !== targetKey);
    if (newPanes.length && newActiveKey === targetKey) {
      if (lastIndex >= 0) {
        newActiveKey = newPanes[lastIndex].key;
      } else {
        newActiveKey = newPanes[0].key;
      }
    }
    setItems(newPanes);
    setActiveKey(newActiveKey);
  };

  const onEdit = (targetKey: string, action: 'add' | 'remove') => {
    if (action === 'add') {
      add();
    } else {
      remove(targetKey);
    }
  };

  const handleTabLabelDoubleClick = (tabItem: TabItem) => {
    const { key } = tabItem;
    const newItems = items.map((item) => {
      if (item.key === key) {
        return { ...item, isEditing: true };
      }
      return item;
    });
    setItems(newItems);
  };

  const handleTabLabelChange = (e: React.ChangeEvent<HTMLInputElement>, tabItem: TabItem) => {
    const { value } = e.target;
    const { key } = tabItem;
    const newItems = items.map((item) => {
      if (item.key === key) {
        return { ...item, label: value, isEditing: false };
      }
      return item;
    });
    setItems(newItems);
  };


  return (
    <Tabs
      type="editable-card"
      onChange={onChange}
      activeKey={activeKey}
      onEdit={onEdit}
    >
      {items.map((item) => (
        <TabPane
          tab={
            item.isEditing ? (
              <Input
                defaultValue={item.label}
                autoFocus
                onBlur={(e) => handleTabLabelChange(e, item)}
                onPressEnter={(e) => handleTabLabelChange(e, item)}
                bordered={false}
                style={{ boxShadow: 'none', width: '120px', height: '22px' }}
              />
            ) : (
              <span onDoubleClick={() => handleTabLabelDoubleClick(item)}>{item.label}</span>
            )
          }
          key={item.key}
          closable={item.closable}
          style={{ height: 'auto' }}
        >
          {item.children}
        </TabPane>
      ))}
    </Tabs>
  );
};

export default TabComponents;