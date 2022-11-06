import admin from './admin';
import user from './user';
import studio from './studio';
import cluster from './cluster';
import project from './project';
import resource from './resource';
import dev from './dev';
import dataSource from "./dataSource";
export default {
  ...user,
  ...admin,
  ...studio,
  ...cluster,
  ...project,
  ...resource,
  ...dev,
  ...dataSource,
};
