import { AccessToken, OktaAuth } from '@okta/okta-auth-js';

// Based on:
// https://developer.okta.com/blog/2020/11/09/vaadin-spring-boot

const authClient = new OktaAuth({
    issuer: 'https://dev-8673725.okta.com/oauth2/default', // use your own
    clientId: '0oa28ltaypSts9p5H5d6', // Vaadin Fusion Playground
    redirectUri: window.location.origin + '/callback',
    pkce: true
});

const isAuthenticated = async () => {
    return !!(await authClient.tokenManager.get('accessToken'));
};

const signIn = async (username: string, password: string) => {

    // signing in and requesting access to openid (default), email and profile (the rest)
    const authResult = await authClient.signIn({
        username,
        password,
        scopes: ['openid', 'email', 'profile'],
    });

    if (authResult.status === 'SUCCESS') {
        authClient.token.getWithRedirect({
            sessionToken: authResult.sessionToken,
            responseType: 'id_token',
        });
    }
};

const signInWithFacebook = async () => {
    console.log('Trying to sign in with Facebook');
}

const signOut = () => authClient.signOut();

const handleAuthentication = async () => {

    if (authClient.token.isLoginRedirect()) {
        try {
            const tokenResponse = await authClient.token.parseFromUrl();
            const {accessToken, idToken} = tokenResponse.tokens;
            if (!accessToken || !idToken) return false;

            authClient.tokenManager.add('accessToken', accessToken);
            authClient.tokenManager.add('idToken', idToken);

            return true;
        } catch (err) {
            console.warn(`authClient.token.parseFromUrl() errored: ${err}`);
            return false;
        }
    }
    return false;
};

const getAccessToken = async () => {
    const token = (await authClient.tokenManager.get('accessToken')) as AccessToken;

    return token;
};

export {
    isAuthenticated,
    signIn,
    signInWithFacebook,
    signOut,
    handleAuthentication,
    getAccessToken,
};
