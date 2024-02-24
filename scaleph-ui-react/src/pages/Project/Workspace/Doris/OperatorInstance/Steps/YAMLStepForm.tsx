import React, {useEffect, useRef} from "react";
import {ProCard} from "@ant-design/pro-components";
import {connect} from "@umijs/max";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";

const DorisInstanceYAML: React.FC = (props: any) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

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
        value={props.dorisInstanceSteps.instanceYaml}
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

const mapModelToProps = ({dorisInstanceSteps}: any) => ({dorisInstanceSteps})
export default connect(mapModelToProps)(DorisInstanceYAML);
