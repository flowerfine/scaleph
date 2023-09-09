import { WORKSPACE_CONF } from '@/constant';
import { WsFlinkArtifactSql } from '@/services/project/typings';
import { WsFlinkKubernetesSessionClusterService } from '@/services/project/WsFlinkKubernetesSessionClusterService';
import { WsFlinkSqlGatewayService } from '@/services/project/WsFlinkSqlGatewayService';
import { Spin, Tabs } from 'antd';
import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { useModel } from 'umi';
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
  const urlParams = useLocation();
  const [sqlScript, setSqlScript] = useState<any>(''); // 内容
  const [activeKey, setActiveKey] = useState(defaultPanes?.[0]?.key); // 当前活动的标签页key
  const [items, setItems] = useState(defaultPanes); // 标签页列表
  const [isLoading, setIsLoading] = useState(false); // 加载状态
  const { executionData } = useModel('executionResult'); //获取执行结果

  const [sessionClusterId, setSessionClusterId] = useState<string>();
  const flinkArtifactSql = urlParams.state as WsFlinkArtifactSql;

  const getResults = async (data: string) => {
    const catalogArray = await WsFlinkSqlGatewayService.getSqlResults(sessionClusterId, data);
    let clear;
    if (catalogArray?.resultType === 'NOT_READY') {
      setIsLoading(true);
      clear = setTimeout(() => {
        getResults(data);
      }, 1000);
    } else {
      setIsLoading(false);
      clearTimeout(clear);
      console.log(catalogArray, 'catalogArray');
    }
  };

  useEffect(() => {
    getResults(executionData);
  }, [executionData]);

  // 切换标签页时的回调函数
  const onChange = (key: string) => {
    setActiveKey(key);
  };

  useEffect(() => {
    setSqlScript(flinkArtifactSql.script);
    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
    (async () => {
      const resSessionClusterId =
        await WsFlinkKubernetesSessionClusterService.getSqlGatewaySessionClusterId(projectId);
      setSessionClusterId(resSessionClusterId);
    })();
  }, []);

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
