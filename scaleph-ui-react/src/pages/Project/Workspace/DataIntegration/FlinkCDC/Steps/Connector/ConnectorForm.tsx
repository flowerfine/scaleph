import {useEffect, useState} from "react";
import {DsInfoService} from "@/services/datasource/info.service";
import SourceMySQLConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Source/SourceMySQLConnector";
import SinkDorisConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Sink/SinkDorisConnector";
import SinkStarRocksConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Sink/SinkStarRocksConnector";
import SourceKafkaConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Source/SourceKafkaConnector";

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
            setConstent(<SinkDorisConnectorForm/>)
          } else if (type === 'sink' && response.data.dsType.type.value === 'StarRocks') {
            setConstent(<SinkStarRocksConnectorForm/>)
          } else if (type === 'sink' && response.data.dsType.type.value === 'Kafka') {
            setConstent(<SinkStarRocksConnectorForm/>)
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
