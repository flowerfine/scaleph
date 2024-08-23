import React, { useEffect, useRef, useState } from 'react';
import { Button, message } from 'antd';
import { useIntl, useModel, useLocation } from '@umijs/max';
import Editor, {loader, useMonaco} from '@monaco-editor/react';
import { language } from 'monaco-editor/esm/vs/basic-languages/sql/sql';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import * as sqlFormatter from 'sql-formatter';
import { WORKSPACE_CONF } from '@/constants/constant';
import { WsArtifactFlinkSql } from '@/services/project/typings';
import { WsArtifactFlinkSqlService } from '@/services/project/WsArtifactFlinkSqlService';
import { WsFlinkKubernetesSessionClusterService } from '@/services/project/WsFlinkKubernetesSessionClusterService';
import { WsFlinkSqlGatewayService } from '@/services/project/WsFlinkSqlGatewayService';
import styles from './index.less';

// 定义 SQL 语法提示关键字列表
const { keywords: SQLKeys } = language;

const CodeEditor: React.FC = () => {
  const urlParams = useLocation();
  const [sqlScript, setSqlScript] = useState<any>(''); // 内容
  const flinkArtifactSql = urlParams.state as WsArtifactFlinkSql;
  const intl = useIntl(); //语言切换
  const [sessionClusterId, setSessionClusterId] = useState<string>();
  const editorRef = useRef<monaco.editor.IStandaloneCodeEditor | null>(null);
  const { setExecutionData } = useModel('executionResult'); //存储执行结果

  useEffect(() => {
    loader.config({monaco})
    setSqlScript(flinkArtifactSql?.script);
    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
    (async () => {
      const resSessionClusterId =
        await WsFlinkKubernetesSessionClusterService.getSqlGatewaySessionClusterId(projectId);
      setSessionClusterId(resSessionClusterId);
    })();
    return () => {
      setExecutionData('');
    };
  }, []);

  // 点击运行获取选中或者全部值
  const onRun = async (): Promise<void> => {
    if (!editorRef.current) return;

    const selection = editorRef.current.getSelection();
    if (selection && !selection.isEmpty()) {
      const selectedValue = editorRef.current.getModel()?.getValueInRange(selection);
      // console.log("选中的值:", selectedValue);
      const catalogArray = await WsFlinkSqlGatewayService.executeSqlList(sessionClusterId, {
        sql: selectedValue || '',
        configuration: {},
      });
      if (catalogArray) {
        message.success(`${intl.formatMessage({ id: 'RequestSuccessful' })}`, 1);
        setExecutionData(catalogArray);
      }
    } else {
      const fullValue = editorRef.current.getModel()?.getValue();
      // console.log("全部的值:", fullValue);
    }
  };

  // 保存数据
  const onSave = async (): Promise<void> => {
    const resultData = await WsArtifactFlinkSqlService.updateScript({
      id: flinkArtifactSql.id,
      script: sqlScript,
    });
    if (resultData.success) {
      message.success(`${intl.formatMessage({ id: 'SaveSuccessful' })}`, 1);
    }
  };

  // 点击格式化按钮
  const onFormat = (): void => {
    const formattedScript = sqlFormatter.format(sqlScript, { language: 'sql' });
    setSqlScript(formattedScript);
    message.success(`${intl.formatMessage({ id: 'FormatSuccessful' })}`, 1);
  };

  // 获取 SQL 语法提示
  const getSQLSuggest = (): monaco.languages.CompletionItem[] => {
    return SQLKeys.map((key: string) => ({
      label: key,
      kind: monaco.languages.CompletionItemKind.Keyword,
      insertText: key,
      detail: '<Keywords>',
    }));
  };

  const provideCompletionItems = (
    model: monaco.editor.ITextModel,
    position: monaco.Position,
  ): monaco.languages.ProviderResult<monaco.languages.CompletionItem[]> => {
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
        onChange={(value) => {
          setSqlScript(value || '');
        }}
        onMount={(editor) => (editorRef.current = editor)}
      />
      <div className={styles.consoleOptionsWrapper}>
        <div className={styles.consoleOptionsLeft}>
          <Button type="primary" className={styles.runButton} onClick={onRun}>
            <img src="/images/EditorResult/播放.svg" alt="" />
            {intl.formatMessage({ id: 'Run' })}
          </Button>
          <Button type="default" className={styles.saveButton} onClick={onSave}>
            {intl.formatMessage({ id: 'Save' })}
          </Button>
        </div>
        <Button type="text" onClick={onFormat}>
          {intl.formatMessage({ id: 'Format' })}
        </Button>
      </div>
    </div>
  );
};

export default CodeEditor;
