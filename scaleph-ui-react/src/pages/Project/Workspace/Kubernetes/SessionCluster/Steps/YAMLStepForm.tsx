import {connect} from "umi";
import React, {useEffect, useRef, useState} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";

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

  return (
    <ProCard>
      <Editor
        width="730"
        height="600px"
        language="yaml"
        theme="vs-white"
        value={props.flinkKubernetesSessionClusterSteps.sessionClusterYaml}
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
const mapModelToProps = ({flinkKubernetesSessionClusterSteps}: any) => ({flinkKubernetesSessionClusterSteps})
export default connect(mapModelToProps)(SessionClusterYAMLStepForm);
