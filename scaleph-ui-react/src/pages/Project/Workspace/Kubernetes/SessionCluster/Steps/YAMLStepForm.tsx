import {connect} from "umi";
import React, {useEffect, useRef, useState} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import YAML from "yaml";

const SessionClusterYAMLStepForm: React.FC = (props: any) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [yaml, setYaml] = useState<string>()

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  useEffect(() => {
    if (props.sessionClusterStep2.sessionCluster) {
      WsFlinkKubernetesSessionClusterService.asYAML(props.sessionClusterStep2.sessionCluster).then(response => {
        if (response.data) {
          setYaml(YAML.stringify(response.data))
        }
      })
    }

  }, [props.sessionClusterStep2.sessionCluster]);

  return (
    <ProCard>
      <Editor
        width="730"
        height="600px"
        language="yaml"
        theme="vs-white"
        value={yaml}
        options={{
          selectOnLineNumbers: true,
          readOnly: true,
          minimap: {
            enabled: false
          }
        }}
        onMount={handleEditorDidMount}
      />
    </ProCard>

  );
}
const mapModelToProps = ({sessionClusterStep2}: any) => ({sessionClusterStep2})
export default connect(mapModelToProps)(SessionClusterYAMLStepForm);
