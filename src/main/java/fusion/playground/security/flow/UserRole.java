package fusion.playground.security.flow;

public enum UserRole
{
    USER,           // regular user
    BETA_USER,      // early access to new features
    ADMIN,          // access to all, incl. administration
    ENDPOINT_ADMIN; // access to actuator endpoints
}
