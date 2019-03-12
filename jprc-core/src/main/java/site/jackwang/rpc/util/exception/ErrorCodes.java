package site.jackwang.rpc.util.exception;

public enum ErrorCodes {
    // serialize
    SERIALIZE_FAILURE (10000, "couldn't serialize the object %s"),
    DESERIALIZE_FAILURE (10001, "couldn't deserialize the object %s"),

    // reflect
    INVOKE_PERSISTED_ENTITY_METHOD_ERROR (10002, "couldn't invoke method of PersistedEntity"),
    NOT_ALLOW_ACCESS_FIELD(10003, "wasn't allowed to get field '%s.%s'"),
    FIELD_NOT_FOUND(10004, "couldn't find field '%s.%s'"),
    NOT_ALLOW_ACCESS_METHOD(10005, "wasn't allowed to get method '%s.%s(%s)'"),
    METHOD_NOT_FOUND(10006, "couldn't find method '%s.%s(%s)'"),
    CONSTRUCT_NEW_INSTANCE_ERROR(10007, "couldn't construct new '%s' with args %s"),
    CANNOT_GET_FIELD_VALUE(10008, "couldn't get '%s'"),
    CANNOT_SET_FIELD_VALUE(10009, "couldn't set '%s' to '%s'"),
    METHOD_NULL(10010, "method is null"),
    FIELD_NULL(10011, "field is null"),
    METHOD_INVOKE_ERROR(10012, "couldn't invoke '%s' with %s on %s: %s"),
    CONSTRUCTOR_NOT_FOUND(10013, "couldn't find constructor of '%s' with parameter typs '%s'"),
    ILLEGAL_CLASS_NAME(10014, "illegal class name"),
    CLASS_NOT_FOUND(10015, "class '%s' not found"),
    METHOD_UNEXPECTED_PARAMETERS(10016, "Method requires unexpected parameters"),
    NOT_INTERFACE(10017, "The interface class '%s' is not a interface!"),

    // net
    NETTY_SEND_INTERRUPTED(11000, "netty send package interrupted"),

    // service provider
    PUBLISH_SERVICE_NOT_REGISTERED(12000, "serviceInterface %s has been registered!"),
    PUBLISH_SERVICE_NOT_IMPLEMENT_INTERFACE(12001, "serviceImpl %s must implement the interface %s"),
    PUBLISH_SERVICE_NON(12002, "jrpc provider doesn't publish service: %s"),

    // registry
    SERVICE_REGISTRY_ZK_NOT_FOUND(13000, "JZkClient.zooKeeper is null."),
    SERVICE_REGISTRY_ZK_CREATE_NODE_FAILURE(13001, "JZkClient.zooKeeper create node failure."),
    SERVICE_REGISTRY_ZK_DELETE_NODE_FAILURE(13002, "JZkClient.zooKeeper delete node failure."),
    SERVICE_REGISTRY_ZK_SET_DATA_FAILURE(13003, "JZkClient.zooKeeper set data failure."),
    SERVICE_REGISTRY_ZK_GET_DATA_FAILURE(13004, "JZkClient.zooKeeper get data failure."),
    SERVICE_REGISTRY_ZK_SET_CHILD_DATA_FAILURE(13005, "JZkClient.zooKeeper set child data failure."),
    SERVICE_REGISTRY_ZK_DELETE_CHILD_DATA_FAILURE(13006, "JZkClient.zooKeeper delete child data failure."),
    SERVICE_REGISTRY_ZK_GET_CHILD_DATA_FAILURE(13007, "JZkClient.zooKeeper get child data failure."),


    // reserved error code from 14000-19999
    INTERNAL_ERROR (19998, "internal error"),
    UNEXPECTED_ERROR (19999, "unexpected error. Please submit a issue");
    
    private final int code;
    
    private final String description;
    
    private ErrorCodes(int code, String description) {
        this.code = code;
        this.description = description;
      }

      public String getDescription() {
         return description;
      }

      public int getCode() {
         return code;
      }

      @Override
      public String toString() {
        return code + ": " + description;
      }
}
