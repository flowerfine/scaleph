import admin from './admin';
import di from './di';
import user from './user';

export default {
  ...user,
  ...admin,
  ...di,
};
