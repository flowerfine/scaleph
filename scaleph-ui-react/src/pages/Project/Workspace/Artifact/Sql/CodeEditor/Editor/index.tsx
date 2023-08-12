import React, { useEffect, useState } from 'react';
import { Editor, CompletionItem, Position, CompletionList } from '@monaco-editor/react';
import { Button,message } from 'antd';
import {useIntl} from 'umi';
import { language } from 'monaco-editor/esm/vs/basic-languages/sql/sql';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import * as sqlFormatter from 'sql-formatter'; 
const { keywords: SQLKeys } = language;

import {WORKSPACE_CONF} from "@/constant";
import { WsFlinkArtifactSql } from '@/services/project/typings';
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {WsFlinkSqlGatewayService} from "@/services/project/WsFlinkSqlGatewayService";
import {FlinkArtifactSqlService} from "@/services/project/WsFlinkArtifactSqlService";
import { useLocation } from 'react-router-dom';
import styles from './index.less';

const CodeEditor: React.FC = () => {
  const urlParams = useLocation();
  const [sqlScript, setSqlScript] = useState<string>('');// 内容
  const flinkArtifactSql = urlParams.state || ('' as WsFlinkArtifactSql);
  const intl = useIntl();//语言切换
  const [sessionClusterId, setSessionClusterId] = useState<string>();

  useEffect(async () => {
    setSqlScript(flinkArtifactSql.script);
        const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
    const resSessionClusterId = await WsFlinkKubernetesSessionClusterService.getSqlGatewaySessionClusterId(projectId);
    setSessionClusterId(resSessionClusterId);
  }, []);

  // 点击运行获取选中或者全部值
  const onRun = async (editor: monaco.editor.IStandaloneCodeEditor): void => {
    const selection = editor.getSelection();
    if (selection && !selection.isEmpty()) {
      const selectedValue = editor.getModel()?.getValueInRange(selection);
      // console.log("选中的值:", selectedValue);
      const catalogArray = await WsFlinkSqlGatewayService.executeSqlList(sessionClusterId, { sql: selectedValue, configuration: {} });
      if (catalogArray) { 
       message.success(`${intl.formatMessage({id: 'RequestSuccessful'})}`,1)
      }
      // const catalogArray123 = await WsFlinkSqlGatewayService.listCatalogsOne(sessionClusterId, sqlGatewaySessionHandleId);
      // console.log(catalogArray123,'catalogArray123123');
      
      
    } else {
      const fullValue = editor.getModel()?.getValue();
      // console.log("全部的值:", fullValue);
    }
  };

  // 保存数据
  const onSave = async(): void => {
    let resultData = await FlinkArtifactSqlService.updateScript({ id: flinkArtifactSql.id, script: sqlScript });
    if (resultData.success) { 
       message.success(`${intl.formatMessage({id: 'SaveSuccessful'})}`,1)
    }
  }

  // 点击格式化按钮
  const onFormat = (): void => {
    const formattedScript = sqlFormatter.format(sqlScript, { language: 'sql' });
    setSqlScript(formattedScript);
    message.success(`${intl.formatMessage({id: 'FormatSuccessful'})}`,1)
  };

  // 获取 SQL 语法提示
  const getSQLSuggest = (): CompletionItem[] => {
    return SQLKeys.map((key: string) => ({
      label: key,
      kind: monaco.languages.CompletionItemKind.Keyword,
      insertText: key,
      detail: '<Keywords>',
    }));
  };

  const provideCompletionItems = (
    model: monaco.editor.ITextModel,
    position: Position,
  ): ProviderResult<CompletionList> => {
    return {
      suggestions: getSQLSuggest(),
    };
  };

  return (
    <div style={{ overflow: 'hidden', height: '100%', width: '100%', position: 'relative' }}>
      <Editor
        language="sql"
        value={sqlScript}
        width="100%"
        theme="vs"
        options={{ minimap: { autohide: true, side: 'right' } }}
        beforeMount={(monaco) => {
          // 注册代码补全提供者
          monaco.languages.registerCompletionItemProvider('sql', {
            provideCompletionItems,
          });
        }}
        onChange={(value, event) => {
          setSqlScript(value);
        }}
        onMount={(editor) => (window.editor = editor)}
      />
      <div className={styles.consoleOptionsWrapper}>
        <div className={styles.consoleOptionsLeft}>
          <Button type="primary" className={styles.runButton} onClick={() => onRun(window.editor!)}>
            <img src="https://s.xinc818.com/files/webcilkhr2wedded3qp/播放.svg" alt="" />
            {intl.formatMessage({id: 'Run'})}
          </Button>
          <Button
            type="default"
            className={styles.saveButton}
            onClick={onSave}
          >
            {intl.formatMessage({id: 'Save'})} 
          </Button>
        </div>
        <Button
          type="text"
           onClick={onFormat}
        >
          {intl.formatMessage({id: 'Format'})}
        </Button>
      </div>
    </div>
  );
};

export default CodeEditor;