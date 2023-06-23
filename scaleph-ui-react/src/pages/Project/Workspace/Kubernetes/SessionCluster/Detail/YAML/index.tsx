import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {connect} from "umi";

const FlinkKubernetesSessinClusterDetailYAMLWeb: React.FC = (props: any) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();
  const [sessionCluster, setSessionCluster] = useState<string>()

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    if (props.sessionClusterDetail.sessionCluster) {
      const sessionCluster: WsFlinkKubernetesSessionCluster = {...props.sessionClusterDetail.sessionCluster}
      sessionCluster.supportSqlGateway = null
      refreshYaml(sessionCluster)
    }
  }, [props.sessionClusterDetail.sessionCluster]);

  const refreshYaml = (sessionCluster: WsFlinkKubernetesSessionCluster) => {
    if (sessionCluster.state) {
      WsFlinkKubernetesSessionClusterService.status(sessionCluster).then((response) => {
        setSessionCluster(YAML.stringify(response.data))
      })
    } else {
      WsFlinkKubernetesSessionClusterService.asYAML(sessionCluster).then((response) => {
        setSessionCluster(YAML.stringify(response.data))
      })
    }
  }

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
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
  );
}

const mapModelToProps = ({sessionClusterDetail}: any) => ({sessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailYAMLWeb);
