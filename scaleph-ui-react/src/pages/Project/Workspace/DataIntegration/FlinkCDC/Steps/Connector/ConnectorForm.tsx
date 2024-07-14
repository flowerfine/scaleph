import {useEffect, useState} from "react";
import {DsInfoService} from "@/services/datasource/info.service";
import SourceMySQLConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Source/SourceMySQLConnector";
import SinkDorisConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Sink/SinkDorisConnector";
import SinkStarRocksConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Sink/SinkStarRocksConnector";
import SinkKafkaConnectorForm
  from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Sink/SinkKafkaConnector";

type ConnectorProps = {
  prefix: string;
  type: string;
  dsId: number;
};

const FlinkCDCConnectorForm: React.FC<ConnectorProps> = ({prefix, type, dsId}) => {

  const [content, setConstent] = useState(<></>)

  useEffect(() => {
    switchStep()
  }, [type, dsId])

  const switchStep = () => {
    if (type && dsId) {
      DsInfoService.selectOne(dsId).then((response) => {
        if (response.data) {
          if (type === 'source' && response.data.dsType.type.value == 'MySQL') {
            setConstent(<SourceMySQLConnectorForm prefix={prefix} />)
          } else if (type === 'sink' && response.data.dsType.type.value === 'Doris') {
            setConstent(<SinkDorisConnectorForm prefix={prefix} />)
          } else if (type === 'sink' && response.data.dsType.type.value === 'StarRocks') {
            setConstent(<SinkStarRocksConnectorForm prefix={prefix} />)
          } else if (type === 'sink' && response.data.dsType.type.value === 'Kafka') {
            setConstent(<SinkKafkaConnectorForm prefix={prefix} />)
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
