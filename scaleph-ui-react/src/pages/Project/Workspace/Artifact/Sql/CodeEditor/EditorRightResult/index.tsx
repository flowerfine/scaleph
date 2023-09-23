import { WORKSPACE_CONF } from '@/constant';
import { WsFlinkKubernetesSessionClusterService } from '@/services/project/WsFlinkKubernetesSessionClusterService';
import { WsFlinkSqlGatewayService } from '@/services/project/WsFlinkSqlGatewayService';
import { Spin, Tabs } from 'antd';
import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { useModel } from 'umi';
import EditorRightResultTable from './EditorRightResultTable';
import styles from './index.less';

interface TabItem {
  label: React.ReactNode;
  children: React.ReactNode;
  key: string | number | null | undefined;
}

const EditorRightResult: React.FC = () => {
  const [dataList, setDataList] = useState<any[]>([]);
  const urlParams = useLocation();
  const [sqlScript, setSqlScript] = useState<string>(''); // SQL 脚本内容
  const [items, setItems] = useState<TabItem[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const { executionData, setExecutionData } = useModel('executionResult');
  const [sessionClusterId, setSessionClusterId] = useState<string | undefined>();
  const flinkArtifactSql = urlParams.state;

  let sqlData: string;

  useEffect(() => {
    let clear: NodeJS.Timeout;

    const getResults = async (data: string) => {
      if (sqlData !== data) {
        setIsLoading(false);
        clearTimeout(clear);
        sqlData = data;
      }
      const catalogArray = await WsFlinkSqlGatewayService.getSqlResults(sessionClusterId!, data);

      if (catalogArray?.resultType === 'NOT_READY' || catalogArray?.resultType === 'PAYLOAD') {
        setIsLoading(true);
        clear = setTimeout(() => {
          getResults(data);
        }, 5000);

        if (catalogArray?.resultType === 'PAYLOAD') {
          setDataList((prevDataList) => [catalogArray]); // 使用函数方式更新数组数据
          setIsLoading(false);
        }
      } else {
        setIsLoading(false);
        clearTimeout(clear);
      }
    };

    if (executionData) {
      getResults(executionData);
    }

    return () => {
      clearTimeout(clear);
    };
  }, [executionData, sessionClusterId]);

  useEffect(() => {
    let clear: NodeJS.Timeout;

    const stopSqlCarryOut = async () => {
      clearTimeout(clear);

      if (sessionClusterId && executionData) {
        await WsFlinkSqlGatewayService.deleteSqlResults(sessionClusterId!, executionData);
        setExecutionData('');
      }
    };

    return stopSqlCarryOut;
  }, [sessionClusterId, executionData]);

  useEffect(() => {
    const handleTabs = (result: any[]) => {
      if (!result || !dataList) return [];

      return dataList.map((item) => ({
        label: (
          <div className={styles.bottomIcon}>
            <img
              style={{ width: 12, height: 13 }}
              src="https://s.xinc818.com/files/webcilkl00h48b0mc6z/成功.svg"
              alt="Success"
            />
            <span>{item?.jobID}</span>
          </div>
        ),
        children: <EditorRightResultTable result={item} />,
        key: item?.jobID,
      }));
    };

    const tabs = handleTabs(dataList);
    console.log(tabs, 'tabs');

    setItems(tabs);
  }, [dataList]);

  const onChange = (key: string | number | null | undefined) => {
    console.log(key);
  };

  useEffect(() => {
    setSessionClusterId(undefined);
    setItems([]);

    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

    const fetchSessionClusterId = async () => {
      const resSessionClusterId =
        await WsFlinkKubernetesSessionClusterService.getSqlGatewaySessionClusterId(projectId!);
      setSessionClusterId(resSessionClusterId);
    };
    if (flinkArtifactSql && flinkArtifactSql.script) {
      setSqlScript(flinkArtifactSql.script);
      fetchSessionClusterId();
    }
  }, [flinkArtifactSql]);

  return (
    <div className={styles.editorRightResult} style={{ height: '100%', width: '100%' }}>
      {items.length ? (
        <Tabs
          hideAdd
          onChange={onChange}
          type="editable-card"
          tabBarExtraContent={<div>Extra Content</div>}
        >
          {items.map((item) => (
            <Tabs.TabPane tab={item.label} key={item.key}>
              {item.children}
            </Tabs.TabPane>
          ))}
        </Tabs>
      ) : (
        <div className={styles.resultContentBox}> No Data </div>
      )}
      {isLoading && <Spin spinning={isLoading} className={styles.resultContentWrapper} />}
    </div>
  );
};

export default EditorRightResult;
