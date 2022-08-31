import { Dict, TreeNode } from '@/app.d';
import { DICT_TYPE, PRIVILEGE_CODE, WORKSPACE_CONF } from "@/constant";
import { listDictDataByType } from '@/services/admin/dictData.service';
import { deleteDir, listProjectDir } from '@/services/project/directory.service';
import { DiDirectory, DiDirectoryTreeNode, DiJob } from '@/services/project/typings';
import { DeleteOutlined, EditOutlined, PlusOutlined } from '@ant-design/icons';
import { ActionType, ProColumns, ProFormInstance } from '@ant-design/pro-components';
import { Button, Card, Col, Input, message, Modal, Row, Space, Tooltip, Tree, Typography } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useIntl, useAccess } from "umi";
import DirectoryForm from './components/DirectoryForm';
import styles from './index.less';

const DiBatchJob: React.FC = () => {
    const intl = useIntl();
    const access = useAccess();
    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId) + '';
    const [dirList, setDirList] = useState<TreeNode[]>([]);
    const [searchValue, setSearchValue] = useState<string>();
    const [expandKeys, setExpandKeys] = useState<React.Key[]>([]);
    const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
    const actionRef = useRef<ActionType>();
    const formRef = useRef<ProFormInstance>();
    const [selectedRows, setSelectedRows] = useState<DiJob[]>([]);
    const [jobTypeList, setJobTypeList] = useState<Dict[]>([]);
    const [jobStatusList, setJobStatusList] = useState<Dict[]>([]);
    const [runtimeStateList, setRuntimeStateList] = useState<Dict[]>([]);

    const [dirFormData, setDirFormData] = useState<{ visiable: boolean; data: DiDirectory; isUpdate: boolean }>({
        visiable: false,
        data: {},
        isUpdate: false
    });
    const [jobFormData, setJobFormData] = useState<{ visiable: boolean; data: DiJob }>({
        visiable: false,
        data: {},
    });
    const tableColumns: ProColumns<DiJob>[] = [
        {
            title: intl.formatMessage({ id: 'pages.project.di.batch.jobName' }),
            dataIndex: 'jobName',
        },
    ]
    useEffect(() => {
        listDictDataByType(DICT_TYPE.jobType).then((d) => {
            setJobTypeList(d);
        });
        listDictDataByType(DICT_TYPE.jobStatus).then((d) => {
            setJobStatusList(d);
        });
        listDictDataByType(DICT_TYPE.runtimeState).then((d) => {
            setRuntimeStateList(d);
        });
        refreshDirList();
    }, []);

    const refreshDirList = () => {
        listProjectDir(projectId).then(d => {
            setDirList(buildTree(d));
        })
    }

    const buildTree = (data: DiDirectoryTreeNode[]): TreeNode[] => {
        let tree: TreeNode[] = [];
        data.forEach((dir) => {
            const node: TreeNode = {
                key: '',
                title: '',
                origin: {
                    id: dir.id,
                    directoryName: dir.directoryName,
                    pid: dir.pid,
                },
            };
            if (dir.children) {
                node.key = dir.id;
                node.title = dir.directoryName;
                node.children = buildTree(dir.children);
                node.showOpIcon = false;
            } else {
                node.key = dir.id;
                node.title = dir.directoryName;
                node.showOpIcon = false;
            }
            tree.push(node);
        });
        return tree;
    };

    let keys: React.Key[] = [];
    const buildExpandKeys = (data: TreeNode[], value: string): React.Key[] => {
        data.forEach((dept) => {
            if (dept.children) {
                buildExpandKeys(dept.children, value);
            }
            if (dept.title?.toString().includes(value)) {
                keys.push(dept.key + '');
            }
        });
        return keys;
    };

    const searchDirTree = (value: string) => {
        keys = [];
        setExpandKeys(buildExpandKeys(dirList, value));
        setSearchValue(value);
        setAutoExpandParent(true);
    };

    const onExpand = (newExpandedKeys: React.Key[]) => {
        setExpandKeys(newExpandedKeys);
        setAutoExpandParent(false);
    };

    return (
        <Row gutter={[12, 12]}>
            <Col span={5}>
                <Card
                    className={styles.leftCard}
                    title={
                        <Typography.Title level={5}>
                            {intl.formatMessage({ id: 'pages.project.dir' })}
                        </Typography.Title>
                    }
                >
                    <Input.Search
                        style={{ marginBottom: 8 }}
                        allowClear={true}
                        onSearch={searchDirTree}
                        placeholder={intl.formatMessage({ id: 'app.common.operate.search.label' })}
                    ></Input.Search>
                    <Tree
                        treeData={dirList}
                        showLine={{ showLeafIcon: false }}
                        blockNode={true}
                        showIcon={false}
                        height={680}
                        defaultExpandAll={true}
                        expandedKeys={expandKeys}
                        autoExpandParent={autoExpandParent}
                        onExpand={onExpand}
                        titleRender={(node) => {
                            return (
                                <Row
                                    className={
                                        node.title?.toString().includes(searchValue + '') && searchValue != ''
                                            ? styles.siteTreeSearchValue
                                            : ''
                                    }
                                >
                                    <Col
                                        span={24}
                                        onMouseEnter={() => {
                                            node.showOpIcon = true;
                                            setDirList([...dirList]);
                                        }}
                                        onMouseLeave={() => {
                                            node.showOpIcon = false;
                                            setDirList([...dirList]);
                                        }}
                                    >
                                        <Typography.Text style={{ paddingRight: 12 }}>
                                            {node.title}
                                        </Typography.Text>
                                        {node.showOpIcon && (
                                            <Space size={2}>
                                                {access.canAccess(PRIVILEGE_CODE.datadevDirAdd) && (
                                                    <Tooltip
                                                        title={intl.formatMessage({ id: 'app.common.operate.new.label' })}
                                                    >
                                                        <Button
                                                            shape="default"
                                                            type="text"
                                                            size="small"
                                                            icon={<PlusOutlined />}
                                                            onClick={() => {
                                                                setDirFormData({
                                                                    visiable: true,
                                                                    data: {
                                                                        pid: node.origin.id,
                                                                        projectId: projectId
                                                                    },
                                                                    isUpdate: false,
                                                                });
                                                            }}
                                                        ></Button>
                                                    </Tooltip>
                                                )}
                                                {access.canAccess(PRIVILEGE_CODE.datadevDirEdit) && (
                                                    <Tooltip
                                                        title={intl.formatMessage({
                                                            id: 'app.common.operate.edit.label',
                                                        })}
                                                    >
                                                        <Button
                                                            shape="default"
                                                            type="text"
                                                            size="small"
                                                            icon={<EditOutlined />}
                                                            onClick={() => {
                                                                setDirFormData({
                                                                    visiable: true,
                                                                    data: {
                                                                        id: node.origin.id,
                                                                        directoryName: node.origin.directoryName,
                                                                        pid: node.origin.pid == '0' ? undefined : node.origin.pid,
                                                                        projectId: projectId
                                                                    },
                                                                    isUpdate: true,
                                                                });
                                                            }}
                                                        ></Button>
                                                    </Tooltip>
                                                )}
                                                {access.canAccess(PRIVILEGE_CODE.datadevDirDelete) && (
                                                    <Tooltip
                                                        title={intl.formatMessage({
                                                            id: 'app.common.operate.delete.label',
                                                        })}
                                                    >
                                                        <Button
                                                            shape="default"
                                                            type="text"
                                                            size="small"
                                                            icon={<DeleteOutlined />}
                                                            onClick={() => {
                                                                Modal.confirm({
                                                                    title: intl.formatMessage({
                                                                        id: 'app.common.operate.delete.confirm.title',
                                                                    }),
                                                                    content: intl.formatMessage({
                                                                        id: 'app.common.operate.delete.confirm.content',
                                                                    }),
                                                                    okText: intl.formatMessage({
                                                                        id: 'app.common.operate.confirm.label',
                                                                    }),
                                                                    okButtonProps: { danger: true },
                                                                    cancelText: intl.formatMessage({
                                                                        id: 'app.common.operate.cancel.label',
                                                                    }),
                                                                    onOk() {
                                                                        deleteDir(node.origin).then((d) => {
                                                                            if (d.success) {
                                                                                message.success(
                                                                                    intl.formatMessage({
                                                                                        id: 'app.common.operate.delete.success',
                                                                                    }),
                                                                                );
                                                                                refreshDirList();
                                                                            }
                                                                        });
                                                                    },
                                                                });
                                                            }}
                                                        ></Button>
                                                    </Tooltip>
                                                )}
                                            </Space>
                                        )}
                                    </Col>
                                </Row>
                            );
                        }}
                    ></Tree>
                </Card>
            </Col>
            <Col span={19}>list</Col>
            {dirFormData.visiable && (
                <DirectoryForm
                    visible={dirFormData.visiable}
                    isUpdate={dirFormData.isUpdate}
                    onCancel={() => {
                        setDirFormData({ visiable: false, data: {}, isUpdate: false });
                    }}
                    onVisibleChange={
                        (visiable) => {
                            setDirFormData({ visiable: false, data: {}, isUpdate: false });
                            refreshDirList();
                        }
                    }
                    data={dirFormData.data}
                ></DirectoryForm>
            )

            }
        </Row>
    );
}

export default DiBatchJob;