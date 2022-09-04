import { selectJobById } from "@/services/project/job.service";
import { DiJob } from "@/services/project/typings";
import { CloseOutlined } from "@ant-design/icons";
import { Button, Drawer, Popover, Space, Tag, Tooltip } from "antd";
import { useEffect, useState } from "react";
import { useAccess, useIntl } from "umi";

interface DiJobFlowPorps {
    visible: boolean;
    data: DiJob;
    onVisibleChange: (visible: boolean, data: any) => void;
    onCancel: () => void;
}

const DiJobFlow: React.FC<DiJobFlowPorps> = ({
    visible,
    data,
    onVisibleChange,
    onCancel
}) => {
    const intl = useIntl();
    const access = useAccess();
    const [jobInfo, setJobInfo] = useState<DiJob>({});

    useEffect(() => {
        console.log(data);
        refreshGraph();
    }, []);

    const refreshGraph = () => {
        selectJobById(data.id as number).then((d) => {
            setJobInfo(d);
        })
    }

    return <>
        <Drawer
            title={
                <Space>
                    <Popover
                        content={<>
                            <p>{intl.formatMessage({ id: 'pages.project.di.jobName' }) + ' : ' + jobInfo.jobName}</p>
                            <p>{intl.formatMessage({ id: 'pages.project.di.jobStatus' }) + ' : ' + jobInfo.jobStatus?.label}</p>
                            <p>{intl.formatMessage({ id: 'pages.project.di.jobVersion' }) + ' : ' + jobInfo.jobVersion}</p>
                            <p>{intl.formatMessage({ id: 'pages.project.di.createTime' }) + ' : ' + jobInfo.createTime}</p>
                            <p>{intl.formatMessage({ id: 'pages.project.di.updateTime' }) + ' : ' + jobInfo.updateTime}</p>
                        </>}
                        title={false}
                        placement="bottom"
                        trigger="hover"
                    >
                        <Tag color="blue">
                            {intl.formatMessage({ id: 'pages.project.di.job.batch' }) + ' : ' + jobInfo.jobCode}
                        </Tag>
                    </Popover>
                </Space>
            }
            placement="top"
            width="100%"
            height="100%"
            closable={false}
            visible={visible}
            onClose={onCancel}
            extra={
                <Space>
                    <Tooltip title={intl.formatMessage({ id: 'app.common.operate.close.label' })}></Tooltip>
                    <Button
                        shape="default"
                        type="text"
                        icon={<CloseOutlined />}
                        onClick={onCancel}
                    >
                    </Button>
                </Space>
            }
        >
            <p>Some contents...</p>
            <p>Some contents...</p>
            <p>Some contents...</p>
        </Drawer>
    </>
}

export default DiJobFlow;