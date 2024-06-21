import SourceMySQLConnectorForm
    from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/Source/SourceMySQLConnector";

type ConnectorProps = {
    type: string;
    name: string;
};

const FlinkCDCConnectorForm: React.FC<ConnectorProps> = ({type, name}) => {

    const switchStep = () => {
        if (type === 'source' && name === 'MySQL') {
            return (<SourceMySQLConnectorForm/>);
        } else if (type === 'sink' && name === 'Doris') {
            return (<SourceMySQLConnectorForm/>);
        } else {
            return <></>;
        }
    };
    return (<div>{switchStep()}</div>);
}

export default FlinkCDCConnectorForm;
