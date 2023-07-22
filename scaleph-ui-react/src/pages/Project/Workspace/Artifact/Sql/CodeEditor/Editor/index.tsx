import React, {useEffect, useState} from "react";
import {Editor} from "@monaco-editor/react";
import {Button, Col, Row, Space} from "antd";
import "./index.less";
import {useLocation} from "umi";
import {WsFlinkArtifactSql} from "@/services/project/typings";
import {FlinkArtifactSqlService} from "@/services/project/WsFlinkArtifactSqlService";

const CodeEditor: React.FC = () => {
  const urlParams = useLocation();
  const [sqlScript, setSqlScript] = useState<string>('');
  const flinkArtifactSql = urlParams.state||'' as WsFlinkArtifactSql;

  useEffect(() => {
    setSqlScript(flinkArtifactSql.script);
  }, [])

  const onSave = () => {
    FlinkArtifactSqlService.updateScript({id: flinkArtifactSql.id, script: sqlScript});
  }

  const handleEditorDidMount = (editor, monaco) => {
    // 在 editorDidMount 回调函数中对 Monaco Editor 进行配置
    monaco.languages.register({ id: "sql" });

    monaco.languages.setMonarchTokensProvider("sql", {
      // 设置 SQL 语言的自定义语法定义
      tokenizer: {
        root: [
          // 自定义的标记规则
        ],
      },
    });

    monaco.languages.registerCompletionItemProvider("sql", {
      provideCompletionItems: function (model, position) {
        const textUntilPosition = model.getValueInRange({
          startLineNumber: position.lineNumber,
          startColumn: 1,
          endLineNumber: position.lineNumber,
          endColumn: position.column,
        });

        // 根据输入的文本返回相应的代码补全建议项
        const suggestions = getSQLSuggestions(textUntilPosition);

        return {
          suggestions: suggestions,
        };
      },
    });
  };

  const getSQLSuggestions = (text) => {
    // 根据输入的文本生成相应的代码补全建议项
    // 可以根据自己的需求自定义补全逻辑
    // 返回的建议项应该包括 label、kind、insertText 等属性
    return [
      {
        label: "SELECT",
        kind: monaco.languages.CompletionItemKind.Keyword,
        insertText: "SELECT ",
      },
      {
        label: "FROM",
        kind: monaco.languages.CompletionItemKind.Keyword,
        insertText: "FROM ",
      },
      // 其他补全建议项...
    ];
  };


  return <>
      <div>
        <Row gutter={[12, 12]}>
          <Col span={24}>
            <Space style={{marginLeft: 12, marginBottom: 6}}>
              <Button onClick={onSave}>保存</Button>
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
              options={{minimap: {autohide: true, side: "right"}}}
              onChange={(value, event) => {
                setSqlScript(value)
              }}
              editorDidMount={handleEditorDidMount}
            />
          </Col>
        </Row>
      </div>
  </>;
};

export default CodeEditor;
