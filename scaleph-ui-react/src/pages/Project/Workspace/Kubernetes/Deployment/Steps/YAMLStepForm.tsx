import {connect} from "umi";
import React, {useEffect, useRef, useState} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";

const DeploymentYAMLStepForm: React.FC = (props: any) => {
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
    if (props.deploymentStep.deployment) {
      WsFlinkKubernetesDeploymentService.asYAML(props.deploymentStep.deployment).then(response => {
        if (response.data) {
          setYaml(YAML.stringify(response.data))
        }
      })
    }

  }, [props.deploymentStep.deployment]);

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
const mapModelToProps = ({deploymentStep}: any) => ({deploymentStep})
export default connect(mapModelToProps)(DeploymentYAMLStepForm);
