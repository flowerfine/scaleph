import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {Props} from '@/app.d';
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";
import {connect} from "umi";

const FlinkKubernetesJobDetailYAMLWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {

  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [job, setJob] = useState<string>()

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    if (props.jobDetail.job) {
      WsFlinkKubernetesJobService.asYaml(props.jobDetail.job?.id).then((response) => {
        if (response.success) {
          setJob(response.data)
        }
      })
    }
  }, [props.jobDetail.job]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={job}
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


const mapModelToProps = ({jobDetail}: any) => ({jobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailYAMLWeb);
