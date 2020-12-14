import { OktaAuth } from '@okta/okta-auth-js';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement, property} from 'lit-element';
import {UserClaims} from "@okta/okta-auth-js/lib/types";

const authClient = new OktaAuth({
    issuer: 'https://dev-8673725.okta.com/oauth2/default', // use your own
    clientId: '0oa28ltaypSts9p5H5d6', // Vaadin Fusion Playground
    redirectUri: window.location.origin + '/callback',
    pkce: true
});

@customElement('user-view')
export class UserView extends LitElement {

    @property({type: Object}) user: UserClaims = {email: 'undefined@test.com', sub: 'some sub'};

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
      ${this.user ? html`              
          <div>User info: ${this.userInfo}</div>
          <div>User email: ${this.user.email}</div>
          <div>User identifier: ${this.user.sub}</div>
          <div>User given_name: ${this.user.given_name}</div>
          <div>User family_name: ${this.user.family_name}</div>
          <div>User preferred_username: ${this.user.preferred_username}</div>
          <br/>
          <div>idToken: ${JSON.stringify(this.idToken)}</div>
          <div>accessToken: ${JSON.stringify(this.accessToken)}</div>
      ` : html`` }
    `;

    }
    async connectedCallback() {
        super.connectedCallback();

        this.user = await authClient.getUser();
        this.userInfo = JSON.stringify(this.user);

        this.idToken = await authClient.getIdToken() || 'unclear';
        this.accessToken = await authClient.getAccessToken() || 'unclear';

        // console.log('authClient: ' + JSON.stringify(authClient));
        // console.log('User: '+JSON.stringify(this.user));
    }

}
