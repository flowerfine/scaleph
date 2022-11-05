import { ModalFormProps } from "@/app.d";
import { MetaDataSource } from "@/services/project/typings";
import { Card, Modal, Typography } from "antd";
import { useIntl } from "umi";

const gridStyle: React.CSSProperties = {
    width: '18%',
    textAlign: 'center',
    margin: '6px 6px',
    cursor: 'pointer',
    backgroundColor: '#fafafa'
};

interface DataSourceFormProps extends ModalFormProps<MetaDataSource> {
    onSelect: (type: string) => void;
}

const DataSourceNewPre: React.FC<DataSourceFormProps> = ({
    data,
    visible,
    onVisibleChange,
    onCancel,
    onSelect
}) => {
    const intl = useIntl();
    return (
        <div>
            <Modal
                open={visible}
                destroyOnClose={true}
                title={intl.formatMessage({ id: 'pages.project.di.dataSource.dataSourceType.chose' })}
                width={860}
                onCancel={onCancel}
                footer={null}
            >
                <Typography.Title level={5} type="secondary">
                    {intl.formatMessage({ id: 'pages.project.di.dataSource.dataSourceType.rdbms' })}
                </Typography.Title>
                <Card bordered={false}>
                    <Card.Grid style={gridStyle} onClick={() => { onSelect('MySQL') }}>
                        <Typography.Text strong >Mysql</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle} onClick={() => { onSelect('Oracle') }}>
                        <Typography.Text strong >Oracle</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle} onClick={() => { onSelect('PostGreSQL') }}>
                        <Typography.Text strong >PostGreSQL</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle} onClick={() => { onSelect('Greenplum') }}>
                        <Typography.Text strong >Greenplum</Typography.Text>
                    </Card.Grid>
                </Card>

                <Typography.Title level={5} type="secondary">
                    {intl.formatMessage({ id: 'pages.project.di.dataSource.dataSourceType.bigdata' })}
                </Typography.Title>
                <Card bordered={false}>
                    <Card.Grid style={gridStyle} onClick={() => { onSelect('Doris') }}>
                        <Typography.Text strong >Doris</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle} onClick={() => { onSelect('ClickHouse') }}>
                        <Typography.Text strong >ClickHouse</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle}>
                        <Typography.Text strong >Hive</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle}>
                        <Typography.Text strong >Hbase</Typography.Text>
                    </Card.Grid>
                </Card>

                <Typography.Title level={5} type="secondary">
                    {intl.formatMessage({ id: 'pages.project.di.dataSource.dataSourceType.mq' })}
                </Typography.Title>
                <Card bordered={false}>
                    <Card.Grid style={gridStyle} onClick={() => { onSelect('Kafka') }}>
                        <Typography.Text strong >Kafka</Typography.Text>
                    </Card.Grid>
                </Card>

                <Typography.Title level={5} type="secondary">
                    {intl.formatMessage({ id: 'pages.project.di.dataSource.dataSourceType.other' })}
                </Typography.Title>
                <Card bordered={false}>
                    <Card.Grid style={gridStyle} onClick={() => { onSelect('JDBC') }}>
                        <Typography.Text strong >JDBC</Typography.Text>
                    </Card.Grid>
                </Card>
            </Modal>
        </div>
    );
}

export default DataSourceNewPre;
