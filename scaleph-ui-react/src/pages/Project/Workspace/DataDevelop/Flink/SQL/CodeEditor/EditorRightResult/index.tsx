import React, { useEffect, useRef, useState } from 'react';
import { Spin, Tabs } from 'antd';
import { useModel, useLocation } from '@umijs/max';
import { WORKSPACE_CONF } from '@/constants/constant';
import { WsFlinkKubernetesSessionClusterService } from '@/services/project/WsFlinkKubernetesSessionClusterService';
import { WsFlinkSqlGatewayService } from '@/services/project/WsFlinkSqlGatewayService';
import EditorRightResultTable from './EditorRightResultTable';
import styles from './index.less';
import {WsArtifactFlinkSql} from "@/services/project/typings";

// 定义标签项的接口
interface TabItem {
  label: React.ReactNode;
  children: React.ReactNode;
  key: string | number | null | undefined;
}

const EditorRightResult: React.FC = ({ verticalSplitSizes }) => {
  const [dataList, setDataList] = useState<any[]>([]); // 数据列表
  const urlParams = useLocation(); // 当前url参数
  const [sqlScript, setSqlScript] = useState<string>(''); // SQL脚本内容
  const [items, setItems] = useState<TabItem[]>([]); // 标签项列表
  const [isLoading, setIsLoading] = useState<boolean>(false); // 加载状态标志
  const { executionData, setExecutionData } = useModel('executionResult'); // 执行结果和设置执行结果的model
  const [sessionClusterId, setSessionClusterId] = useState<string | undefined>(); // 会话集群id
  const [activeKey, setActiveKey] = useState<string | undefined | number>(); // 控制当前激活的tab
  const flinkArtifactSql = urlParams.state as WsArtifactFlinkSql; // Flink SQL参数对象
  let sqlData: string;
  const executionDataString = useRef<string>();
  const sessionClusterIdString = useRef<string>();
  const clearTimeOut = useRef<NodeJS.Timeout>();

  const stopSqlCarryOut = async () => {
    if (sessionClusterIdString.current && executionDataString.current) {
      // 调用后端接口停止SQL执行
      await WsFlinkSqlGatewayService.deleteSqlResults(
        sessionClusterIdString.current,
        executionDataString.current,
      );
    }
  };

  useEffect(() => {
    const getResults = async (data: string, nextToken = 0) => {
      if (sqlData !== data) {
        setIsLoading(false);
        clearTimeout(clearTimeOut.current);
        stopSqlCarryOut();
        sqlData = data;
      }
      // 调用后端接口获取SQL结果
      const catalogArray: any = await WsFlinkSqlGatewayService.getSqlResults(
        sessionClusterId!,
        data,
        nextToken,
      );
      if (catalogArray?.resultType === 'NOT_READY' || catalogArray?.resultType === 'PAYLOAD') {
        setIsLoading(true);
        clearTimeOut.current = setTimeout(() => {
          getResults(data, catalogArray?.nextToken);
        }, 5000);
        if (catalogArray?.resultType === 'PAYLOAD') {
          setDataList((prevDataList) => {
            const lastItem = prevDataList[prevDataList?.length - 1];
            if (lastItem && lastItem?.jobID === catalogArray?.jobID) {
              return [...prevDataList.slice(0, -1), catalogArray];
            } else {
              setActiveKey(catalogArray?.jobID);
              return [...prevDataList, catalogArray];
            }
          });
          setIsLoading(false);
        }
      } else {
        setIsLoading(false);
        clearTimeout(clearTimeOut.current);
      }
    };

    if (executionData) {
      getResults(executionData);
      executionDataString.current = executionData;
    }
    return () => {
      clearTimeout(clearTimeOut.current);
    };
  }, [executionData]);

  useEffect((): any => {
    return stopSqlCarryOut;
  }, []);

  useEffect(() => {
    const handleTabs = (result: any[]) => {
      if (!result || !dataList) return [];
      return dataList.map((item, index) => ({
        label: (
          <div className={styles.bottomIcon}>
            <img
              style={{ width: 12, height: 13 }}
              src="/images/EditorResult/成功.svg"
              alt="Success"
            />
            <span>{item?.jobID}</span>
          </div>
        ),
        children: (
          <EditorRightResultTable
            result={item}
            key={item?.key}
            verticalSplitSizes={verticalSplitSizes}
            lastOneData={dataList?.length - 1 == index}
          />
        ),
        key: item?.jobID,
      }));
    };

    const tabs = handleTabs(dataList);
    setItems(tabs);
  }, [dataList, verticalSplitSizes]);

  const onEdit = (key: string | number | null | undefined) => {
    setDataList((prevList) => prevList.filter((item) => item?.jobID !== key));

    const lastIndex = items?.length - 1;
    const lastKey = items?.[lastIndex]?.key;

    if (key === lastKey) {
      setActiveKey(items?.[lastIndex - 1]?.key || '');
    } else {
      setActiveKey(lastKey || '');
    }

    setItems((prevItems) => prevItems.filter((item) => item?.key !== key));

    if (lastIndex !== undefined && key === items[lastIndex]?.key) {
      setIsLoading(false);
      stopSqlCarryOut();
      clearTimeout(clearTimeOut.current);
    }
  };

  useEffect(() => {
    setSessionClusterId(undefined);
    setItems([]);
    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

    const fetchSessionClusterId = async () => {
      // 获取会话集群id
      const resSessionClusterId =
        await WsFlinkKubernetesSessionClusterService.getSqlGatewaySessionClusterId(projectId!);
      setSessionClusterId(resSessionClusterId);
      sessionClusterIdString.current = resSessionClusterId;
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
          onChange={(itemId) => {
            setActiveKey(itemId);
          }}
          onEdit={onEdit}
          activeKey={activeKey}
          type="editable-card"
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
