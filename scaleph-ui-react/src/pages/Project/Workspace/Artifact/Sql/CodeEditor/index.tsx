import Split from "react-split";
import {useEffect, useState} from "react";
import {Editor} from "@monaco-editor/react";
import {Button, Col, Input, Row, Space} from "antd";
import "./index.less";
import {useLocation} from "umi";
import {WsFlinkArtifactSql} from "@/services/project/typings";
import {FlinkArtifactSqlService} from "@/services/project/WsFlinkArtifactSqlService";

const CodeEditor: React.FC = () => {
  const urlParams = useLocation();
  const [showLeft, setShowLeft] = useState<boolean>(true);
  const [sqlScript, setSqlScript] = useState<string>('');
  const [horizontalSplitSizes, setHorizontalSplitSizes] = useState<number[]>([
    20, 80,
  ]);
  const flinkArtifactSql = urlParams.state as WsFlinkArtifactSql;

  useEffect(() => {
    setSqlScript(flinkArtifactSql.script);
  }, [])

  const onSave = () => {
    FlinkArtifactSqlService.updateScript({id: flinkArtifactSql.id, script: sqlScript});
  }

  return <>
    <Split
      className={"split-horizontal"}
      direction="horizontal"
      gutterSize={4}
      sizes={horizontalSplitSizes}
      minSize={[0, 0]}
      maxSize={[680, Infinity]}
      snapOffset={100}
      onDrag={(sizes: number[]) => {
        if (sizes[0] <= 6) {
          setShowLeft(false);
        } else {
          setShowLeft(true);
        }
        setHorizontalSplitSizes(sizes);
      }}
    >
      <div>
        <Row
          gutter={[12, 12]}
          className="container-left"
          style={{display: showLeft ? "block" : "none"}}>
          <Col span={24} style={{paddingLeft: 12, paddingRight: 12}}>
            <Input.Search></Input.Search>
          </Col>
        </Row>
      </div>
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
            >
            </Editor>
          </Col>
        </Row>
      </div>
    </Split>
  </>;
};

export default CodeEditor;
