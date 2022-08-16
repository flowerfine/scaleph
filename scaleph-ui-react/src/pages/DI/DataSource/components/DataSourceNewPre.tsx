import { ModalFormProps } from "@/app.d";
import { MetaDataSource } from "@/services/di/typings";
import { Card, Modal, Typography } from "antd";
import { useState } from "react";
import { useIntl } from "umi";
import JdbcDataSourceForm from "./JdbcDataSourceForm";

const gridStyle: React.CSSProperties = {
    width: '18%',
    textAlign: 'center',
    margin: '6px 6px',
    cursor: 'pointer',
    backgroundColor: '#fafafa'
};

const DataSourceNewPre: React.FC<ModalFormProps<any>> = ({
    data,
    visible,
    onVisibleChange,
    onCancel
}) => {
    const intl = useIntl();
    const [dataSourceFormData, setDataSourceFormData] = useState<{ visiable: boolean, data: MetaDataSource }>({ visiable: false, data: {} });
    return (
        <div>
            <Modal
                visible={visible}
                // destroyOnClose={true}
                title={intl.formatMessage({ id: 'pages.di.dataSource.dataSourceType.chose' })}
                width={860}
                onCancel={onCancel}
                footer={null}
            >
                <Typography.Title level={5} type="secondary">
                    {intl.formatMessage({ id: 'pages.di.dataSource.dataSourceType.rdbms' })}
                </Typography.Title>
                <Card bordered={false}>
                    <Card.Grid style={gridStyle}>
                        <Typography.Text strong >Mysql</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle}>
                        <Typography.Text strong >Oracle</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle}>
                        <Typography.Text strong >PostGreSQL</Typography.Text>
                    </Card.Grid>
                </Card>

                <Typography.Title level={5} type="secondary">
                    {intl.formatMessage({ id: 'pages.di.dataSource.dataSourceType.bigdata' })}
                </Typography.Title>
                <Card bordered={false}>
                    <Card.Grid style={gridStyle}>
                        <Typography.Text strong >Doris</Typography.Text>
                    </Card.Grid>
                    <Card.Grid style={gridStyle}>
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
                    {intl.formatMessage({ id: 'pages.di.dataSource.dataSourceType.mq' })}
                </Typography.Title>
                <Card bordered={false}>
                    <Card.Grid style={gridStyle}>
                        <Typography.Text strong >Kafka</Typography.Text>
                    </Card.Grid>
                </Card>

                <Typography.Title level={5} type="secondary">
                    {intl.formatMessage({ id: 'pages.di.dataSource.dataSourceType.other' })}
                </Typography.Title>
                <Card bordered={false}>
                    <Card.Grid style={gridStyle} onClick={() => {
                        // onVisibleChange(false);
                        console.log(dataSourceFormData);
                        setDataSourceFormData({ visiable: true, data: { datasourceType: { value: 'JDBC' } } });

                    }}>
                        <Typography.Text strong >JDBC</Typography.Text>
                    </Card.Grid>
                </Card>
            </Modal>
            {dataSourceFormData.visiable && dataSourceFormData.data.datasourceType?.value == 'JDBC' ? (
                <JdbcDataSourceForm
                    visible={dataSourceFormData.visiable}
                    onCancel={() => {
                        setDataSourceFormData({ visiable: visible, data: {} });
                    }}
                    onVisibleChange={(visiable) => {
                        setDataSourceFormData({ visiable: visiable, data: {} })
                        // onVisibleChange(false);
                    }}
                    data={dataSourceFormData.data}
                ></JdbcDataSourceForm>
            ) : null}
        </div>

    );
}

export default DataSourceNewPre;