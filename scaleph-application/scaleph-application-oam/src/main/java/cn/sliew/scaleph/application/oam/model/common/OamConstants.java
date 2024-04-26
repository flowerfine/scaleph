package cn.sliew.scaleph.application.oam.model.common;

public enum OamConstants {
    ;

    public static final String OAM_GROUP = "core.oam.dev";
    public static final String OAM_VERSION = "v1beta1";
    public static final String OAM_API_VERSION = OAM_GROUP + "/" + OAM_VERSION;

    public static final String OAM_KIND_COMPONENT_DEFINITION = "ComponentDefinition";
    public static final String OAM_KIND_POLICY_DEFINITION = "PolicyDefinition";
    public static final String OAM_KIND_TRAIT_DEFINITION = "TraitDefinition";


    public static final String OAM_ANNOTATION_NAME = "name";
    public static final String OAM_ANNOTATION_DESCRIPTION = "description";
}
