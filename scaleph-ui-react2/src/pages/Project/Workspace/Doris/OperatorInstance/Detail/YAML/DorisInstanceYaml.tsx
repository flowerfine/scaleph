import React, {useEffect, useRef} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {Props} from '@/app.d';
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import {connect} from "umi";

const DorisInstanceDetailYAMLInstance: React.FC<Props<WsFlinkKubernetesTemplate>> = (props: any) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={props.dorisInstanceDetail.instanceYaml}
      options={{
        selectOnLineNumbers: true,
        readOnly: true,
        minimap: {
          enabled: false
        }
      }}
      onMount={handleEditorDidMount}
    />
  );
}
const mapModelToProps = ({dorisInstanceDetail}: any) => ({dorisInstanceDetail})
export default connect(mapModelToProps)(DorisInstanceDetailYAMLInstance);
