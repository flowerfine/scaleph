import React, {useCallback, useEffect, useMemo, useState} from 'react';
import {Button, Card, message, Modal, Space, TableColumnsType, Tag} from 'antd';
import {useIntl} from '@umijs/max';
import mainHeight from '@/models/useMainSize';
import {ModalFormProps} from "@/typings";
import {SecUser} from '@/services/admin/typings';
import TableTransfer, {DataType} from "@/components/TableTransfer";
import {AuthorizationService} from "@/services/admin/security/authorization.service";

const UserAssignRoleForm: React.FC<ModalFormProps<SecUser>> = ({data, visible, onCancel}) => {
    const intl = useIntl();
    const containerInfo = mainHeight('.ant-layout-content');
    const [roleLists, setRoleLists] = useState<DataType[]>([]);

    // 角色表格列配置
    const tableColumns: TableColumnsType<DataType> = [
        {
            dataIndex: 'name',
            title: intl.formatMessage({id: 'pages.admin.role.name'}),
            width: 300,
        },
        {
            dataIndex: 'desc',
            title: intl.formatMessage({id: 'app.common.data.remark'}),
            width: 300,
        },
    ];

    // 合并数组
    function mergeArrays(unauthorized: DataType[], authorized: DataType[]): any {
        unauthorized.forEach((obj, index: any) => {
            obj.checkOut = 0;
        });
        authorized.forEach((obj, index: any) => {
            obj.checkOut = 1;
        });
        return [...unauthorized, ...authorized];
    }

    // 异步获取数据
    const fetchData = useCallback(async () => {
        try {
            const unauthorized = await AuthorizationService.listUnauthorizedRolesByUserId({userId: data?.id})
                .then(response => {
                    return response.data?.records.map(role => {
                        const dataType: DataType = {
                            id: role.id,
                            name: role.name,
                            status: role.status?.label,
                            remark: role.remark
                        }
                        return dataType;
                    })
                });
            const authorized = await AuthorizationService.listAuthorizedRolesByUserId({userId: data?.id})
                .then(response => {
                    return response.data?.records.map(role => {
                        const dataType: DataType = {
                            id: role.id,
                            name: role.name,
                            status: role.status?.label,
                            remark: role.remark
                        }
                        return dataType;
                    })
                });
            if (unauthorized && authorized) {
                const mergedArray = mergeArrays(unauthorized, authorized);
                setRoleLists(mergedArray);
            }
        } catch (error) {
            console.error(error);
        }
    }, [data]);

    useEffect(() => {
        if (data) {
            fetchData();
        }
    }, [data, fetchData]);

    // 页面标题
    const returnTitle = useMemo(() => {
        return (
            <Space direction="vertical">
                <span>{` ${data?.nickName}-用户授权详情`}</span>
            </Space>
        );
    }, [data, intl]);

    // 获取选中角色的 id 数组
    const originTargetKeys = useMemo(() => {
        return roleLists?.filter((item) => item.checkOut === 1).map((item) => item.id);
    }, [roleLists]);

    // 过滤方法
    const handleFilter = useCallback((inputValue, item) => {
        return item?.name.indexOf(inputValue) !== -1;
    }, []);

    // 角色转移事件处理
    const handleChange = useCallback(
        async (targetKeys, direction, moveKeys) => {
            const roleIds = moveKeys.map((item: string | number) => +item);
            const params = {
                userId: data?.id,
                roleIds: roleIds,
            };
            if (direction === 'right') {
                // 批量为角色绑定用户
                await AuthorizationService.authorizeUser2Roles(params).then((res) => {
                    if (res?.success) {
                        message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}), 2);
                    }
                });
            } else {
                // 批量为角色解除用户绑定
                await AuthorizationService.unauthorizeUser2Roles(params).then((res) => {
                    message.success(intl.formatMessage({id: 'app.common.operate.edit.success'}), 2);
                });
            }
            fetchData();
        },
        [data, fetchData, intl],
    );

    return (
        <Modal
            open={visible}
            title={data.id ? intl.formatMessage({id: 'pages.admin.security.authorization.user2Roles'}) : ''}
            width={1100}
            centered
            destroyOnClose={true}
            onCancel={onCancel}
            cancelText={intl.formatMessage({id: 'app.common.operate.close.label'})}
            closeIcon={false}
            footer={[
                <Button type="primary" onClick={onCancel}>
                    {intl.formatMessage({id: 'app.common.operate.close.label'})}
                </Button>,
            ]}
        >
            <div>
                <Card
                    styles={{
                        body: {minHeight: `${containerInfo.height - 200}px`,}
                    }}
                    title={returnTitle}
                >
                    <TableTransfer
                        containerHeight={containerInfo.height}
                        titles={[intl.formatMessage({id: 'pages.admin.security.authorization.user2Roles.unauthorized'}), intl.formatMessage({id: 'pages.admin.security.authorization.user2Roles.authorized'})]}
                        dataSource={roleLists}
                        targetKeys={originTargetKeys}
                        showSearch={true}
                        rowKey={(record: { id: any }) => record.id}
                        onChange={handleChange}
                        filterOption={handleFilter}
                        listStyle={{
                            width: 500,
                        }}
                        leftColumns={tableColumns}
                        rightColumns={tableColumns}
                    />
                </Card>
            </div>
        </Modal>
    );
};

export default UserAssignRoleForm;
