import {css, customElement, html, LitElement, property, internalProperty} from 'lit-element';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';

import { showNotification } from '@vaadin/flow-frontend/a-notification';
import { EndpointError } from '@vaadin/flow-frontend/Connect';

import { OktaAuth } from '@okta/okta-auth-js';
import {UserClaims} from "@okta/okta-auth-js/lib/types";
import User from "../../generated/fusion/playground/data/entity/User";

import * as UserEndpoint from "../../generated/UserEndpoint";

const authClient = new OktaAuth({
    issuer: 'https://dev-8673725.okta.com/oauth2/default', // use your own
    clientId: '0oa28ltaypSts9p5H5d6', // Vaadin Fusion Playground
    redirectUri: window.location.origin + '/callback',
    pkce: true
});

@customElement('user-details-view')
export class UserDetailsView extends LitElement {

    @internalProperty()
    user: User = {
        id: "0",
        username: '',
        oktaUserId: '',

        dateOfBirth: '',
        emailAddress: '',
        emailConfirmed: false
    };

    @internalProperty() oktaUserId : string = '';

    @property({type: Object})
    userClaims: UserClaims = {email: 'undefined@test.com', sub: 'some sub'};

    @property({type: String}) userInfo: string = '';
    @property({type: Object}) idToken: Object = '';
    @property({type: Object}) accessToken: Object = '';

    static get styles() {
        return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
    }

    render() {
        return html`
      <h3>User info</h3>
      ${this.userClaims ? html`              
          <div>User info: ${this.userInfo}</div>
          <div>User email: ${this.userClaims.email}</div>
          <div>User identifier: ${this.userClaims.sub}</div>
          <div>User given_name: ${this.userClaims.given_name}</div>
          <div>User family_name: ${this.userClaims.family_name}</div>
          <div>User preferred_username: ${this.userClaims.preferred_username}</div>
          <br/>
          <div>idToken: ${JSON.stringify(this.idToken)}</div>
          <div>accessToken: ${JSON.stringify(this.accessToken)}</div>
      ` : html`` }
    `;

    }

    async firstUpdated() {

    }

    async connectedCallback() {
        super.connectedCallback();

        let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
        this.oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;

        this.userClaims = await authClient.getUser();
        this.userInfo = JSON.stringify(this.userClaims);

        this.idToken = await authClient.getIdToken() || 'unclear';
        this.accessToken = await authClient.getAccessToken() || 'unclear';

        // console.log('authClient: ' + JSON.stringify(authClient));
        // console.log('User: '+JSON.stringify(this.userClaims));
    }

    async getUserDetails() {
        try {
            this.user = await UserEndpoint.getUserByOktaId(this.oktaUserId);
        } catch (error) {
            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }
    }


}
