import { customElement, html, internalProperty, LitElement } from 'lit-element';
import {signIn, signInWithFacebook} from '../../auth';
import '@vaadin/vaadin-button';

@customElement('facebook-login-view')
export class FacebookLoginView extends LitElement {
    @internalProperty()
    private error = !!new URLSearchParams().get('error');

    render() {
        return html`
        <style>
            facebook-login-view {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
            }
        </style>
        <vaadin-button @click="${()=>this.signInWithFacebook()}">Continue with Facebook</vaadin-button>
        
        ${this.error}
        `;
    }

    async login(e: CustomEvent) {
        try {
            await signIn(e.detail.username, e.detail.password);
        } catch (e) {
            // this.error = true;
        }
    }

    async signInWithFacebook() {

        console.log("clicked Sign in with Facebook");
        try {
            await signInWithFacebook();
        } catch (e) {
            this.error = true;
        }
    }


    // Render in light DOM for password managers
    protected createRenderRoot() {
        return this;
    }
}


// https://developers.facebook.com/docs/facebook-login/web/login-button/
