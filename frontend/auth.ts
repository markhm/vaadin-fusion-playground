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
    // Checks if there is a current accessToken in the TokenManger.
    // console.log("authClient.tokenManager.get('accessToken'): "+JSON.stringify(authClient.tokenManager.get('accessToken')));

    return !!(await authClient.tokenManager.get('accessToken'));
};

const signIn = async (username: string, password: string) => {

    // console.log('redirectUri: '+window.location.origin + '/callback');
    // console.log('');
    // console.log('About to sign in.');
    // console.log('username ' + username);
    const authResult = await authClient.signIn({
        username,
        password,
        scopes: ['openid', 'email', 'profile'],
    });

    // console.log('');
    // console.log('authResult: ' + JSON.stringify(authResult));

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
    // console.log("at handleAuthentication()");

    if (authClient.token.isLoginRedirect()) {
        try {
            const tokenResponse = await authClient.token.parseFromUrl();
            const {accessToken, idToken} = tokenResponse.tokens;
            if (!accessToken || !idToken) return false;

            // console.log("adding accessToken to tokenManager()");

            authClient.tokenManager.add('accessToken', accessToken);
            authClient.tokenManager.add('idToken', idToken);

            // console.log('accessToken set correctly...!');
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
