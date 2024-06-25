import {useEffect, useState} from "react";
import {DsInfoService} from "@/services/datasource/info.service";
import SourceMySQLConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Source/SourceMySQLConnector";

type ConnectorProps = {
  type: string;
  dsId: number;
};

const FlinkCDCConnectorForm: React.FC<ConnectorProps> = ({type, dsId}) => {

  const [content, setConstent] = useState(<></>)

  useEffect(() => {
    switchStep()
  }, [type, dsId])

  const switchStep = () => {
    if (type && dsId) {
      DsInfoService.selectOne(dsId).then((response) => {
        if (response.data) {
          if (type === 'source' && response.data.dsType.type.value == 'MySQL') {
            setConstent(<SourceMySQLConnectorForm/>)
          } else if (type === 'sink' && response.data.dsType.type.value === 'Doris') {
            setConstent(<SourceMySQLConnectorForm/>)
          }
        }
      })
    } else {
      setConstent(<></>)
    }
  };

  return (<div>{content}</div>);
}

export default FlinkCDCConnectorForm;
