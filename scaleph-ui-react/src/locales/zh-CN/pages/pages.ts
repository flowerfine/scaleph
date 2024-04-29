import base from './base';
import admin from './admin';
import user from './user';
import studio from './studio';
import project from './project';
import resource from './resource';
import dataSource from "./dataSource";
import metadata from "./metadata";
import oam from "./oam";
import stdata from "./stdata";
export default {
  ...base,
  ...user,
  ...admin,
  ...studio,
  ...project,
  ...resource,
  ...dataSource,
  ...metadata,
  ...oam,
  ...stdata,
};
