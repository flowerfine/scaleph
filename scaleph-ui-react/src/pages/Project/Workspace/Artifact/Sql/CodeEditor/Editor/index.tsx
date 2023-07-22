import { Editor } from '@monaco-editor/react';
import { language } from 'monaco-editor/esm/vs/basic-languages/sql/sql';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';
import React, { useEffect, useState } from 'react';
const { keywords: SQLKeys } = language;

import { WsFlinkArtifactSql } from '@/services/project/typings';
import { FlinkArtifactSqlService } from '@/services/project/WsFlinkArtifactSqlService';
import { Button, Col, Row, Space } from 'antd';
import { useLocation } from 'react-router-dom';
import './index.less';

const CodeEditor: React.FC = () => {
  const urlParams = useLocation();
  const [sqlScript, setSqlScript] = useState<string>('');
  const flinkArtifactSql = urlParams.state || ('' as WsFlinkArtifactSql);

  useEffect(() => {
    setSqlScript(flinkArtifactSql.script);
  }, []);

  const onSave = () => {
    FlinkArtifactSqlService.updateScript({ id: flinkArtifactSql.id, script: sqlScript });
  };

  // 获取 SQL 语法提示
  const getSQLSuggest = (): monaco.languages.CompletionItem[] => {
    return SQLKeys.map((key: string) => ({
      label: key,
      kind: monaco.languages.CompletionItemKind.Keyword,
      insertText: key,
      detail: '​<Keywords>',
    }));
  };

  const provideCompletionItems = (
    model: monaco.editor.ITextModel,
    position: monaco.Position,
  ): monaco.languages.ProviderResult<monaco.languages.CompletionList> => {
    return {
      suggestions: getSQLSuggest(),
    };
  };

  return (
    <div>
      <Row gutter={[12, 12]}>
        <Col span={24}>
          <Space style={{ marginLeft: 12, marginBottom: 6 }}>
            <Button onClick={onSave}>保存​</Button>
          </Space>
        </Col>
      </Row>
      <Row>
        <Col span={24}>
          <Editor
            language="sql"
            value={sqlScript}
            height="calc(100vh - 175px)"
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
          />
        </Col>
      </Row>
    </div>
  );
};

export default CodeEditor;
