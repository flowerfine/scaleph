import {useModel} from "umi";
import React, {useEffect, useRef} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";

const SessionClusterYAMLStepForm: React.FC = () => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const {sessionCluster} = useModel('sessionClusterStep', (model) => ({
    sessionCluster: model.sessionCluster
  }));

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
        value={sessionCluster}
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

export default SessionClusterYAMLStepForm
