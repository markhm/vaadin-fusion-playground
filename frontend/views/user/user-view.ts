import { OktaAuth } from '@okta/okta-auth-js';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import { css, customElement, html, LitElement } from 'lit-element';

const authClient = new OktaAuth({
    issuer: 'https://dev-8673725.okta.com/oauth2/default', // use your own
    clientId: '0oa28ltaypSts9p5H5d6', // Vaadin Fusion Playground
    redirectUri: window.location.origin + '/callback',
    pkce: true
});

@customElement('user-view')
export class UserView extends LitElement {

    user: Object = '';

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
          User info: 
      ` : html`` }
    `;

    }
    async connectedCallback() {
        super.connectedCallback();

        this.user = await authClient.getUser();

        console.log('authClient: ' + JSON.stringify(authClient));
        console.log('User: '+JSON.stringify(this.user));
    }

}
