export type GenericResource = {
  apiVersion: string;
  kind: number;
  metadata?: Record<string, any>;
  spec?: Record<string, any>;
  status?: Record<string, any>;
};

export type Namespace = {
  kind: number;
  apiVersion: string;
  metadata?: Record<string, any>;
  spec?: Record<string, any>;
  status?: Record<string, any>;
  additionalProperties?: Record<string, any>;
};
