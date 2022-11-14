import { ModalFormProps } from '@/app.d';
import { RoleService } from '@/services/admin/role.service';
import { SecRole, SecUser } from '@/services/admin/typings';
import { UserService } from '@/services/admin/user.service';
import { message, Modal, Transfer } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const RoleGrant: React.FC<ModalFormProps<SecRole>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [targetKeys, setTargetKeys] = useState<string[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
  const [userList, setUserList] = useState<SecUser[]>([]);

  useEffect(() => {
    //all user list limit 100000
    UserService.listUserByPage({ current: 1, pageSize: 100000 }).then((resp) => {
      setUserList(resp.data);
    });
    //user granted with role id
    UserService.listByUserNameAndRole('', data.id + '', '1').then((resp) => {
      setTargetKeys(resp.map((item) => item.value));
    });
  }, []);

  const onSelectChange = (sourceSelectedKeys: string[], targetSelectedKeys: string[]) => {
    setSelectedKeys([...sourceSelectedKeys, ...targetSelectedKeys]);
  };

  const onChange = (nextTargetKeys: string[]) => {
    setTargetKeys(nextTargetKeys);
  };

  return (
    <Modal
      open={visible}
      destroyOnClose={true}
      title={
        intl.formatMessage(
          { id: 'app.common.operate.grant.title' },
          { name: intl.formatMessage({ id: 'pages.admin.user.role' }) },
        ) +
        ' - ' +
        data.roleName
      }
      width={780}
      onCancel={onCancel}
      onOk={() => {
        RoleService.grantRoleToUsers(data.id + '', targetKeys).then((d) => {
          if (d.success) {
            message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
          }
        });
        onVisibleChange(false);
      }}
    >
      <Transfer
        dataSource={userList}
        titles={[
          intl.formatMessage({ id: 'pages.admin.user.awaitGrant' }),
          intl.formatMessage({ id: 'pages.admin.user.granted' }),
        ]}
        listStyle={{ width: '100%', minHeight: 420 }}
        showSearch={true}
        pagination={true}
        showSelectAll={true}
        targetKeys={targetKeys}
        selectedKeys={selectedKeys}
        onChange={onChange}
        onSelectChange={onSelectChange}
        render={(item) => item.userName + ''}
        rowKey={(item) => item.id + ''}
      ></Transfer>
    </Modal>
  );
};

export default RoleGrant;
