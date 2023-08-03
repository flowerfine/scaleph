import { Spin, Tabs } from 'antd';
import React, { useState } from 'react';
import EditorRightResultTable from './EditorRightResultTable';
import styles from './index.less';

// 定义每个标签页的类型
interface PaneItem {
  label: React.ReactNode;
  children: React.ReactNode;
  key: string;
}

const defaultPanes: PaneItem[] = new Array(4).fill(null).map((_, index) => {
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

const EditorRightResult: React.FC = () => {
  // 当前活动的标签页key
  const [activeKey, setActiveKey] = useState(defaultPanes?.[0]?.key);
  // 标签页列表
  const [items, setItems] = useState(defaultPanes);
  // 加载状态
  const [isLoading, setIsLoading] = useState(false);

  // 切换标签页时的回调函数
  const onChange = (key: string) => {
    setActiveKey(key);
  };

  return (
    <div className={styles.editorRightResult} style={{ height: '100%', width: '100%' }}>
      {/* 标签页组件 */}
      {items.length ? (
        <Tabs
          hideAdd
          onChange={onChange}
          activeKey={activeKey}
          type="editable-card"
          items={items}
        />
      ) : (
        <div className={styles.resultContentBox}> No Data ​</div>
      )}
      {/* 加载中的旋转动画 */}
      {isLoading && <Spin spinning={isLoading} className={styles.resultContentWrapper} />}
    </div>
  );
};

export default EditorRightResult;
